package br.edu.toolrent.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.edu.toolrent.api.domain.Usuario;
import br.edu.toolrent.api.repository.UsuarioRepository;

/**
 * Regras de acesso do usuario autenticado — US01 (UC01, RN09, RN10).
 *
 * <p>O diagrama de sequencia do UC01 coloca a decisao "usuario ativo?" no
 * service: o filtro JWT so diz quem e a pessoa (parte 2 do diagrama), e a
 * checagem acontece aqui, testavel sem subir servidor.
 *
 * <p>Este mesmo service recebe as regras da US02 (CRUD, RN09 por perfil)
 * quando aquela historia entrar.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
}
