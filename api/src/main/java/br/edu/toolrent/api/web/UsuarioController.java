package br.edu.toolrent.api.web;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.toolrent.api.domain.Usuario;
import br.edu.toolrent.api.service.UsuarioService;

/**
 * Endpoints de usuario — US01 entrega o {@code /me}, a US02 acrescenta o CRUD.
 *
 * <p>Controller sem regra: recebe, delega ao {@code service}, responde
 * (arquitetura da API). A identidade sai do JWT, nunca do corpo da
 * requisicao — o {@code sub} e o {@code auth_user_id} do Supabase Auth.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Perfil de quem fez login — abre a tela certa por perfil (US01).
     */
    @GetMapping("/me")
    public UsuarioSessaoResponse me(@AuthenticationPrincipal Jwt jwt) {
        Usuario usuario = usuarioService.carregarSessao(UUID.fromString(jwt.getSubject()));
        return new UsuarioSessaoResponse(
                usuario.getId().toString(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil());
    }
}
