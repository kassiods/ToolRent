package br.edu.toolrent.api.service;

/**
 * Quem nao e ADMINISTRADOR tentou usar um endpoint de gestao de usuarios
 * (UC02, RN09).
 *
 * <p>Tratado como acesso negado (403), com mensagem que nao vaza detalhes
 * internos. A decisao acontece no service, nunca no filtro e nunca no app.
 */
public class NaoAdministradorException extends RuntimeException {

    public NaoAdministradorException() {
        super("Apenas administradores podem gerenciar usuários");
    }
}
