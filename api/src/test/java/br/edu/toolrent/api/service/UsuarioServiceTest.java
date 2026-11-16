package br.edu.toolrent.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import br.edu.toolrent.api.domain.PerfilUsuario;
import br.edu.toolrent.api.domain.Usuario;
import br.edu.toolrent.api.repository.SupabaseAuthUsuarioRepository;
import br.edu.toolrent.api.repository.UsuarioRepository;

/**
 * Regras da sessao autenticada (UC01) e do CRUD de usuarios (UC02,
 * RN09, RN10, RN11) testadas sem subir servidor — a fronteira que a
 * arquitetura do projeto exige.
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SupabaseAuthUsuarioRepository supabaseAuth;

    @InjectMocks
    private UsuarioService usuarioService;

    private final UUID authUserId = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private Usuario usuarioAtivo() {
        return new Usuario(UUID.randomUUID(), authUserId, "Victor",
                "victor@toolrent.dev", PerfilUsuario.ADMINISTRADOR,
                true, OffsetDateTime.now());
    }

    private Usuario solicitanteAdministrador() {
        Usuario administrador = usuarioAtivo();
        given(usuarioRepository.buscarPorAuthUserId(authUserId))
                .willReturn(Optional.of(administrador));
        return administrador;
    }

    @Test
    @DisplayName("carrega a sessao de um usuario ativo")
    void carregaSessaoDeUsuarioAtivo() {
        given(usuarioRepository.buscarPorAuthUserId(authUserId))
                .willReturn(Optional.of(usuarioAtivo()));

        Usuario encontrado = usuarioService.carregarSessao(authUserId);

        assertThat(encontrado.getEmail()).isEqualTo("victor@toolrent.dev");
        assertThat(encontrado.getPerfil()).isEqualTo(PerfilUsuario.ADMINISTRADOR);
        assertThat(encontrado.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("bloqueia usuario inativo com mensagem especifica")
    void bloqueiaUsuarioInativo() {
        Usuario inativo = new Usuario(UUID.randomUUID(), authUserId, "Victor",
                "victor@toolrent.dev", PerfilUsuario.ADMINISTRADOR,
                false, OffsetDateTime.now());
        given(usuarioRepository.buscarPorAuthUserId(authUserId))
                .willReturn(Optional.of(inativo));

        assertThatThrownBy(() -> usuarioService.carregarSessao(authUserId))
                .isInstanceOf(UsuarioInativoException.class)
                .hasMessage("Conta inativa: contate o administrador do sistema");
    }

    @Test
    @DisplayName("bloqueia quando o token nao corresponde a nenhum usuario")
    void bloqueiaQuandoNaoHaLinhaDeUsuario() {
        given(usuarioRepository.buscarPorAuthUserId(authUserId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.carregarSessao(authUserId))
                .isInstanceOf(UsuarioSemAcessoException.class)
                .hasMessage("Usuário sem acesso ao sistema");
    }

    // ---------------------------------------------------------------
    // RN09 — so ADMINISTRADOR gerencia
    // ---------------------------------------------------------------

    @Test
    @DisplayName("RN09: atendente nao lista usuarios (403)")
    void atendenteNaoListaUsuarios() {
        Usuario atendente = new Usuario(UUID.randomUUID(), authUserId, "Nailan",
                "atendente@toolrent.dev", PerfilUsuario.ATENDENTE,
                true, OffsetDateTime.now());
        given(usuarioRepository.buscarPorAuthUserId(authUserId))
                .willReturn(Optional.of(atendente));

        assertThatThrownBy(() -> usuarioService.listar(authUserId))
                .isInstanceOf(NaoAdministradorException.class)
                .hasMessage("Apenas administradores podem gerenciar usuários");
        verify(usuarioRepository, never()).listarTodos();
    }

    @Test
    @DisplayName("RN09: administrador lista todos, inclusive inativos")
    void administradorListaTodos() {
        solicitanteAdministrador();
        Usuario inativo = new Usuario(UUID.randomUUID(), UUID.randomUUID(),
                "Ferias", "ferias@toolrent.dev", PerfilUsuario.CLIENTE,
                false, OffsetDateTime.now());
        given(usuarioRepository.listarTodos()).willReturn(List.of(inativo));

        List<Usuario> listados = usuarioService.listar(authUserId);

        assertThat(listados).hasSize(1);
        assertThat(listados.get(0).isAtivo()).isFalse();
    }

    // ---------------------------------------------------------------
    // RN10 — senha nasce no Supabase Auth; duplicidade vira 409
    // ---------------------------------------------------------------

    @Test
    @DisplayName("cadastra usuario criando login no Supabase Auth")
    void cadastraUsuarioCriandoLoginNoAuth() {
        solicitanteAdministrador();
        given(usuarioRepository.buscarPorEmail("novo@toolrent.dev"))
                .willReturn(Optional.empty());
        UUID authDoLogin = UUID.randomUUID();
        given(supabaseAuth.criarUsuario(eq("novo@toolrent.dev"), eq("senha123")))
                .willReturn(authDoLogin);
        given(usuarioRepository.inserir(any(Usuario.class)))
                .willAnswer(invocacao -> {
                    Usuario u = invocacao.getArgument(0);
                    return new Usuario(UUID.randomUUID(), u.getAuthUserId(),
                            u.getNome(), u.getEmail(), u.getPerfil(),
                            u.isAtivo(), OffsetDateTime.now());
                });

        Usuario criado = usuarioService.cadastrar(authUserId, "Novo",
                "novo@toolrent.dev", PerfilUsuario.CLIENTE, "senha123");

        assertThat(criado.getId()).isNotNull();
        assertThat(criado.getAuthUserId()).isEqualTo(authDoLogin);
        assertThat(criado.getEmail()).isEqualTo("novo@toolrent.dev");
    }

    @Test
    @DisplayName("RN10: e-mail ja cadastrado nao toca no Auth (409)")
    void emailJaCadastradoNaoTocaNoAuth() {
        solicitanteAdministrador();
        given(usuarioRepository.buscarPorEmail("duplicado@toolrent.dev"))
                .willReturn(Optional.of(usuarioAtivo()));

        assertThatThrownBy(() -> usuarioService.cadastrar(authUserId,
                "Duplicado", "duplicado@toolrent.dev",
                PerfilUsuario.CLIENTE, "senha123"))
                .isInstanceOf(EmailJaCadastradoException.class)
                .hasMessage("E-mail já cadastrado no sistema");
        verify(supabaseAuth, never()).criarUsuario(anyString(), anyString());
    }

    @Test
    @DisplayName("linhas parciais sao compensadas: login criado e removido")
    void linhaParcialCompensaLogin() {
        solicitanteAdministrador();
        given(usuarioRepository.buscarPorEmail("corrida@toolrent.dev"))
                .willReturn(Optional.empty());
        UUID authDoLogin = UUID.randomUUID();
        given(supabaseAuth.criarUsuario(anyString(), anyString()))
                .willReturn(authDoLogin);
        given(usuarioRepository.inserir(any(Usuario.class)))
                .willThrow(new DataIntegrityViolationException("email"));

        assertThatThrownBy(() -> usuarioService.cadastrar(authUserId,
                "Corrida", "corrida@toolrent.dev",
                PerfilUsuario.CLIENTE, "senha123"))
                .isInstanceOf(EmailJaCadastradoException.class);
        verify(supabaseAuth).excluirUsuario(authDoLogin);
    }

    // ---------------------------------------------------------------
    // UC02 — edicao e inativacao
    // ---------------------------------------------------------------

    @Test
    @DisplayName("edita nome e perfil do usuario encontrado")
    void editaNomeEPerfil() {
        solicitanteAdministrador();
        Usuario alvo = new Usuario(UUID.randomUUID(), UUID.randomUUID(),
                "Antigo", "antigo@toolrent.dev", PerfilUsuario.CLIENTE,
                true, OffsetDateTime.now());
        given(usuarioRepository.buscarPorId(alvo.getId()))
                .willReturn(Optional.of(alvo));

        Usuario editado = usuarioService.editar(authUserId, alvo.getId(),
                "Novo Nome", PerfilUsuario.ATENDENTE);

        assertThat(editado.getNome()).isEqualTo("Novo Nome");
        assertThat(editado.getPerfil()).isEqualTo(PerfilUsuario.ATENDENTE);
        verify(usuarioRepository).editar(alvo.getId(), "Novo Nome",
                PerfilUsuario.ATENDENTE);
    }

    @Test
    @DisplayName("editar id inexistente recebe 404")
    void editarIdInexistenteRecebe404() {
        solicitanteAdministrador();
        UUID inexistente = UUID.randomUUID();
        given(usuarioRepository.buscarPorId(inexistente))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.editar(authUserId,
                inexistente, "Nome", PerfilUsuario.CLIENTE))
                .isInstanceOf(UsuarioNaoEncontradoException.class);
    }

    @Test
    @DisplayName("RN11: inativar mantem a linha e devolve ativo=false")
    void inativarMantemLinhaEDevolveInativo() {
        solicitanteAdministrador();
        Usuario alvo = new Usuario(UUID.randomUUID(), UUID.randomUUID(),
                "Cliente", "cliente@teste.com", PerfilUsuario.CLIENTE,
                true, OffsetDateTime.now());
        given(usuarioRepository.buscarPorId(alvo.getId()))
                .willReturn(Optional.of(alvo));

        Usuario inativado = usuarioService.inativar(authUserId, alvo.getId());

        assertThat(inativado.isAtivo()).isFalse();
        verify(usuarioRepository).inativar(alvo.getId());
        verify(usuarioRepository, never()).inserir(any());
    }

    @Test
    @DisplayName("administrador nao inativa a propria conta (A4)")
    void administradorNaoInativaPropriaConta() {
        Usuario solicitante = solicitanteAdministrador();

        assertThatThrownBy(() -> usuarioService.inativar(authUserId,
                solicitante.getId()))
                .isInstanceOf(AutoinativacaoException.class)
                .hasMessage("Não é possível inativar a própria conta");
        verify(usuarioRepository, never()).inativar(any());
    }

    @Test
    @DisplayName("inativar id inexistente recebe 404")
    void inativarIdInexistenteRecebe404() {
        solicitanteAdministrador();
        UUID inexistente = UUID.randomUUID();
        given(usuarioRepository.buscarPorId(inexistente))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.inativar(authUserId, inexistente))
                .isInstanceOf(UsuarioNaoEncontradoException.class);
    }
}
