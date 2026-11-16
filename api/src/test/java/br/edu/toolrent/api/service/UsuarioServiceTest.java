package br.edu.toolrent.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.toolrent.api.domain.PerfilUsuario;
import br.edu.toolrent.api.domain.Usuario;
import br.edu.toolrent.api.repository.UsuarioRepository;

/**
 * Regras da sessao autenticada (UC01) testadas sem subir servidor —
 * a fronteira que a arquitetura do projeto exige.
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private final UUID authUserId = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private Usuario usuarioAtivo() {
        return new Usuario(UUID.randomUUID(), authUserId, "Victor",
                "victor@toolrent.dev", PerfilUsuario.ADMINISTRADOR,
                true, OffsetDateTime.now());
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
}
