package br.edu.toolrent.api.web;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.edu.toolrent.api.service.AutoinativacaoException;
import br.edu.toolrent.api.service.ChaveSecretaNaoConfiguradaException;
import br.edu.toolrent.api.service.EmailJaCadastradoException;
import br.edu.toolrent.api.service.FalhaAoCriarLoginException;
import br.edu.toolrent.api.service.NaoAdministradorException;
import br.edu.toolrent.api.service.UsuarioInativoException;
import br.edu.toolrent.api.service.UsuarioNaoEncontradoException;
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

    @ExceptionHandler(NaoAdministradorException.class)
    public ResponseEntity<Map<String, String>> naoAdministrador(
            NaoAdministradorException excecao) {
        return resposta(HttpStatus.FORBIDDEN, excecao.getMessage());
    }

    @ExceptionHandler(AutoinativacaoException.class)
    public ResponseEntity<Map<String, String>> autoinativacao(
            AutoinativacaoException excecao) {
        return resposta(HttpStatus.FORBIDDEN, excecao.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> usuarioNaoEncontrado(
            UsuarioNaoEncontradoException excecao) {
        return resposta(HttpStatus.NOT_FOUND, excecao.getMessage());
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> emailJaCadastrado(
            EmailJaCadastradoException excecao) {
        return resposta(HttpStatus.CONFLICT, excecao.getMessage());
    }

    @ExceptionHandler(ChaveSecretaNaoConfiguradaException.class)
    public ResponseEntity<Map<String, String>> chaveSecretaNaoConfigurada(
            ChaveSecretaNaoConfiguradaException excecao) {
        return resposta(HttpStatus.SERVICE_UNAVAILABLE, excecao.getMessage());
    }

    @ExceptionHandler(FalhaAoCriarLoginException.class)
    public ResponseEntity<Map<String, String>> falhaAoCriarLogin(
            FalhaAoCriarLoginException excecao) {
        return resposta(HttpStatus.BAD_GATEWAY, excecao.getMessage());
    }

    // Formato invalido nos DTOs — 400 com a mensagem do campo, para a
    // tela apontar o problema (UC02, A1)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> camposInvalidos(
            MethodArgumentNotValidException excecao) {
        String mensagem = excecao.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getDefaultMessage() == null
                        ? erro.getField()
                        : erro.getDefaultMessage())
                .collect(Collectors.joining(" "));
        return resposta(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> corpoIlegivel(
            HttpMessageNotReadableException excecao) {
        return resposta(HttpStatus.BAD_REQUEST,
                "Dados inválidos na requisição");
    }

    private static ResponseEntity<Map<String, String>> resposta(
            HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("mensagem", mensagem));
    }
}
