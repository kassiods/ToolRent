package br.edu.toolrent.api.web;

import br.edu.toolrent.api.domain.PerfilUsuario;
import br.edu.toolrent.api.domain.Usuario;

/**
 * Resposta do CRUD de usuarios (UC02). Nao ha campo de senha em nenhum
 * ponto de contrato — senha nem existe aqui (RN10).
 */
public record UsuarioResponse(
        String id,
        String nome,
        String email,
        PerfilUsuario perfil,
        boolean ativo) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId().toString(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.isAtivo());
    }
}
