package br.edu.toolrent.api.service;

/**
 * Usuario existe e autenticou, mas esta com {@code ativo = false} —
 * inativado por um administrador (RN11).
 *
 * <p>A mensagem e especifica, diferente da de credencial invalida: quem tem
 * conta inativa precisa saber que a conta existe e foi suspensa
 * (criterio da US01).
 */
public class UsuarioInativoException extends RuntimeException {

    public UsuarioInativoException() {
        super("Conta inativa: contate o administrador do sistema");
    }
}
