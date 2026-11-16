package br.edu.toolrent.api.web;

import br.edu.toolrent.api.domain.PerfilUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Cadastro de usuario (UC02). Valida FORMATO — regra de negocio,
 * duplicidade e criacao de login ficam no service.
 *
 * <p>A senha e aceita aqui uma unica vez e vai direto para o Supabase
 * Auth: nao e devolvida, nao e logada e nao e guardada (RN10).
 */
public record CriarUsuarioRequest(
        @NotBlank(message = "Informe o nome.")
        String nome,
        @NotBlank(message = "Informe o e-mail.")
        @Email(message = "Informe um e-mail válido.")
        String email,
        @NotNull(message = "Informe o perfil.")
        PerfilUsuario perfil,
        @NotBlank(message = "Informe a senha provisória.")
        @Size(min = 6, message = "A senha provisória precisa de ao menos 6 caracteres.")
        String senha) {
}
