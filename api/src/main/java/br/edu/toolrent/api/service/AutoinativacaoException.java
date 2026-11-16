package br.edu.toolrent.api.service;

/**
 * O administrador tentou inativar a propria conta (UC02, criterio A4).
 *
 * <p>Impedido de proposito: sair sem saida travaria o proprio acesso ao
 * sistema. Tratado como 403, com mensagem propria — distinto do 403 de
 * perfil (RN09) para permitir teste objetivo.
 */
public class AutoinativacaoException extends RuntimeException {

    public AutoinativacaoException() {
        super("Não é possível inativar a própria conta");
    }
}
