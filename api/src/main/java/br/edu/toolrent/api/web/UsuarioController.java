package br.edu.toolrent.api.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.toolrent.api.domain.Usuario;
import br.edu.toolrent.api.service.UsuarioService;

import jakarta.validation.Valid;

/**
 * Endpoints de usuario — US01 entrega o {@code /me}, a US02 acrescenta o
 * CRUD (UC02).
 *
 * <p>Controller sem regra: recebe, delega ao {@code service}, responde
 * (arquitetura da API). A identidade sai do JWT, nunca do corpo da
 * requisicao — o {@code sub} e o {@code auth_user_id} do Supabase Auth.
 * Quem pode cada operacao (RN09) e decidido no service.
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

    /**
     * Lista todos os usuarios — apenas ADMINISTRADOR (RN09).
     */
    @GetMapping
    public List<UsuarioResponse> listar(@AuthenticationPrincipal Jwt jwt) {
        UUID solicitante = UUID.fromString(jwt.getSubject());
        return usuarioService.listar(solicitante).stream()
                .map(UsuarioResponse::de)
                .toList();
    }

    /**
     * Cadastra usuario + login no Supabase Auth — 201 com o usuario criado.
     */
    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CriarUsuarioRequest requisicao) {
        UUID solicitante = UUID.fromString(jwt.getSubject());
        Usuario criado = usuarioService.cadastrar(
                solicitante,
                requisicao.nome(),
                requisicao.email(),
                requisicao.perfil(),
                requisicao.senha());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponse.de(criado));
    }

    /**
     * Edita nome e perfil — e-mail e senha nao passam por aqui (RN10).
     */
    @PutMapping("/{id}")
    public UsuarioResponse editar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id,
            @Valid @RequestBody EditarUsuarioRequest requisicao) {
        UUID solicitante = UUID.fromString(jwt.getSubject());
        Usuario editado = usuarioService.editar(
                solicitante, id, requisicao.nome(), requisicao.perfil());
        return UsuarioResponse.de(editado);
    }

    /**
     * Inativa (nunca exclui — RN11) e devolve o usuario com
     * {@code ativo = false}.
     */
    @DeleteMapping("/{id}")
    public UsuarioResponse inativar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id) {
        UUID solicitante = UUID.fromString(jwt.getSubject());
        return UsuarioResponse.de(usuarioService.inativar(solicitante, id));
    }
}
