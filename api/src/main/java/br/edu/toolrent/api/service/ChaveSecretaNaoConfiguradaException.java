package br.edu.toolrent.api.service;

/**
 * A {@code SUPABASE_SECRET_KEY} nao esta configurada no .env da API e o
 * cadastro de login no Supabase Auth nao pode acontecer (UC02, RN10).
 *
 * <p>Tratado como 503: e configuracao do ambiente, nao regra — a mensagem
 * aponta a variavel sem revelar valor algum.
 */
public class ChaveSecretaNaoConfiguradaException extends RuntimeException {

    public ChaveSecretaNaoConfiguradaException() {
        super("Cadastro de login indisponível: SUPABASE_SECRET_KEY não "
                + "configurada no .env da API (ver api/.env.example)");
    }
}
