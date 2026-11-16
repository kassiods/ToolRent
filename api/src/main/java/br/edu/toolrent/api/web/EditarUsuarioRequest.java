package br.edu.toolrent.api.web;

import br.edu.toolrent.api.domain.PerfilUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Edicao de usuario (UC02): nome e perfil. E-mail e senha nao constam —
 * e-mail e a chave de login no Supabase Auth e senha nunca sai de la
 * (RN10).
 */
public record EditarUsuarioRequest(
        @NotBlank(message = "Informe o nome.")
        String nome,
        @NotNull(message = "Informe o perfil.")
        PerfilUsuario perfil) {
}
