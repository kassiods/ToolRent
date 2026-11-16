package br.edu.toolrent.api.service;

/**
 * O Supabase Auth recusou a criacao do login por motivo diferente de
 * e-mail duplicado (UC02).
 *
 * <p>Tratado como 502: a falha e do servico de autenticacao externo. O
 * detalhe do provedor fica no log da API (nunca na resposta e nunca com
 * a senha).
 */
public class FalhaAoCriarLoginException extends RuntimeException {

    public FalhaAoCriarLoginException() {
        super("Não foi possível criar o login no Supabase Auth. "
                + "Tente novamente.");
    }
}
