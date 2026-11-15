package br.edu.toolrent.api.web;

import br.edu.toolrent.api.domain.PerfilUsuario;

/**
 * Resposta do {@code GET /api/usuarios/me}: os dados do perfil autenticado.
 *
 * <p>Nao existe campo de senha neste DTO — nem aqui, nem em lugar nenhum
 * (RN10): a senha vive so no Supabase Auth.
 */
public record UsuarioSessaoResponse(
        String id,
        String nome,
        String email,
        PerfilUsuario perfil) {
}
