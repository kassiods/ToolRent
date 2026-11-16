package br.edu.toolrent.api.service;

/**
 * O token e valido, mas nao ha linha de {@code usuario} correspondente no
 * banco — a pessoa autenticou no Supabase Auth e nao e usuario do sistema.
 *
 * <p>Tratado como acesso negado (403), sem revelar detalhes internos.
 */
public class UsuarioSemAcessoException extends RuntimeException {

    public UsuarioSemAcessoException() {
        super("Usuário sem acesso ao sistema");
    }
}
