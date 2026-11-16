package br.edu.toolrent.api.service;

import java.util.UUID;

/**
 * Nao ha linha de {@code usuario} com o id informado (UC02).
 *
 * <p>Tratado como 404. A mensagem nao confirma existencia de dados
 * internos alem do proprio id pedido.
 */
public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(UUID id) {
        super("Usuário não encontrado: " + id);
    }
}
