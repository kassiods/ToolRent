package br.edu.toolrent.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.edu.toolrent.api.domain.PerfilUsuario;
import br.edu.toolrent.api.domain.Usuario;
import br.edu.toolrent.api.repository.SupabaseAuthUsuarioRepository;
import br.edu.toolrent.api.repository.UsuarioRepository;

/**
 * Regras de acesso do usuario autenticado — US01 (UC01, RN09, RN10) e
 * gestao de usuarios — US02 (UC02, RN09, RN10, RN11).
 *
 * <p>O diagrama de sequencia do UC01 coloca a decisao "usuario ativo?" no
 * service: o filtro JWT so diz quem e a pessoa (parte 2 do diagrama), e a
 * checagem acontece aqui, testavel sem subir servidor. O CRUD da US02
 * segue o mesmo contrato: quem pode (RN09) e o que e proibido sao
 * decisoes deste metodo, nunca do app.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final SupabaseAuthUsuarioRepository supabaseAuth;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          SupabaseAuthUsuarioRepository supabaseAuth) {
        this.usuarioRepository = usuarioRepository;
        this.supabaseAuth = supabaseAuth;
    }

    /**
     * Carrega o usuario dono do token para o {@code GET /api/usuarios/me}.
     *
     * @param authUserId o claim {@code sub} do JWT — identidade vinda do
     *                   Supabase Auth, nunca do corpo da requisicao
     * @return o usuario ativo correspondente
     * @throws UsuarioSemAcessoException se nao ha linha de usuario para esse
     *                                   {@code auth_user_id}
     * @throws UsuarioInativoException   se o usuario esta inativado
     */
    public Usuario carregarSessao(UUID authUserId) {
        Usuario usuario = usuarioRepository.buscarPorAuthUserId(authUserId)
                .orElseThrow(UsuarioSemAcessoException::new);

        if (!usuario.isAtivo()) {
            throw new UsuarioInativoException();
        }
        return usuario;
    }

    // ---------------------------------------------------------------
    // US02 — CRUD de usuarios (UC02)
    // ---------------------------------------------------------------

    /**
     * Lista todos os usuarios — inclusive inativos, para o administrador
     * ver o estado real de cada conta.
     */
    public List<Usuario> listar(UUID solicitanteAuthUserId) {
        exigirAdministrador(solicitanteAuthUserId);
        return usuarioRepository.listarTodos();
    }

    /**
     * Cadastra um usuario novo: linha na tabela {@code usuario} + login no
     * Supabase Auth (RN10 — a senha nasce e fica la, nunca aqui).
     *
     * <p>Se a linha nao puder ser gravada apos o login ter sido criado, o
     * login e removido: nenhum dado parcial fica para tras (garantia
     * minima do UC02).
     */
    public Usuario cadastrar(UUID solicitanteAuthUserId, String nome,
                             String email, PerfilUsuario perfil,
                             String senha) {
        exigirAdministrador(solicitanteAuthUserId);

        if (usuarioRepository.buscarPorEmail(email).isPresent()) {
            throw new EmailJaCadastradoException();
        }

        UUID authUserId = supabaseAuth.criarUsuario(email, senha);
        Usuario novo = new Usuario(null, authUserId, nome, email,
                perfil, true, null);
        try {
            return usuarioRepository.inserir(novo);
        } catch (DataIntegrityViolationException excecao) {
            // unique de email na tabela — remove o login criado acima
            supabaseAuth.excluirUsuario(authUserId);
            throw new EmailJaCadastradoException();
        } catch (RuntimeException excecao) {
            supabaseAuth.excluirUsuario(authUserId);
            throw excecao;
        }
    }

    /**
     * Edita nome e perfil. E-mail nao muda: ele e a chave de login no
     * Supabase Auth (RN10) e alteracao de identidade nao e criterio da
     * UC02.
     */
    public Usuario editar(UUID solicitanteAuthUserId, UUID id,
                          String nome, PerfilUsuario perfil) {
        exigirAdministrador(solicitanteAuthUserId);
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        usuarioRepository.editar(id, nome, perfil);
        usuario.setNome(nome);
        usuario.setPerfil(perfil);
        return usuario;
    }

    /**
     * Inativa em vez de excluir (RN11): usuario referenciado por locacao,
     * pagamento ou manutencao preserva o historico.
     *
     * <p>Nao confunde com o login: inativar mexe so na tabela do sistema;
     * o Supabase Auth continua aceitando a senha, mas a API bloqueia a
     * sessao (UC01, A2).
     */
    public Usuario inativar(UUID solicitanteAuthUserId, UUID id) {
        Usuario solicitante = exigirAdministrador(solicitanteAuthUserId);

        // solicitante.getId() e a linha da tabela; o sub so serve para
        // achar essa linha — comparar sub com id nunca daria certo
        if (solicitante.getId().equals(id)) {
            throw new AutoinativacaoException();
        }

        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        usuarioRepository.inativar(id);
        usuario.setAtivo(false);
        return usuario;
    }

    /**
     * RN09: so ADMINISTRADOR gerencia usuarios. A checagem e do servico —
     * o filtro JWT apenas autentica, o app nunca decide.
     *
     * @return o proprio solicitante, ja carregado, para quem precisar da
     *         linha dele (ex.: impedir autoinativacao)
     */
    private Usuario exigirAdministrador(UUID solicitanteAuthUserId) {
        Usuario solicitante = carregarSessao(solicitanteAuthUserId);
        if (solicitante.getPerfil() != PerfilUsuario.ADMINISTRADOR) {
            throw new NaoAdministradorException();
        }
        return solicitante;
    }
}
