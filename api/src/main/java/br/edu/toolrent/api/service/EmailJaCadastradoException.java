package br.edu.toolrent.api.service;

/**
 * E-mail ja existente — na tabela {@code usuario} ou no Supabase Auth
 * (UC02, criterio A2).
 *
 * <p>Tratado como conflito (409): o cadastro nao pode prosseguir e a
 * mensagem e clara para o administrador, sem revelar mais nada.
 */
public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException() {
        super("E-mail já cadastrado no sistema");
    }
}
