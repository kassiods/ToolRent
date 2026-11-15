package br.edu.toolrent.api.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.edu.toolrent.api.service.UsuarioInativoException;
import br.edu.toolrent.api.service.UsuarioSemAcessoException;

/**
 * Converte as excecoes de regra em respostas HTTP — em português, sem
 * stack trace, conforme a tabela de erros da arquitetura.
 *
 * <p>O {@code service} nao conhece HTTP: ele lanca a excecao semanticamente
 * e aqui ela vira status e mensagem.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UsuarioSemAcessoException.class)
    public ResponseEntity<Map<String, String>> usuarioSemAcesso(
            UsuarioSemAcessoException excecao) {
        return resposta(HttpStatus.FORBIDDEN, excecao.getMessage());
    }

    @ExceptionHandler(UsuarioInativoException.class)
    public ResponseEntity<Map<String, String>> usuarioInativo(
            UsuarioInativoException excecao) {
        return resposta(HttpStatus.FORBIDDEN, excecao.getMessage());
    }

    private static ResponseEntity<Map<String, String>> resposta(
            HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("mensagem", mensagem));
    }
}
