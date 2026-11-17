package com.senai.cloud_io.controller;

// ============================================================================
// SIMPLES: O organizador de erros.
// Pega qualquer erro e responde sempre no mesmo formato: {"erro": "mensagem"}.
// Roda sozinho quando algo dá errado.
// ============================================================================

import com.senai.cloud_io.config.ApiException;                    // import: o erro ApiException
import java.util.LinkedHashMap;                                                     // import: mapa que mantém a ordem
import java.util.Map;                                                               // import: tipo mapa
import org.slf4j.Logger;                                                            // import: Logger = escreve no terminal
import org.slf4j.LoggerFactory;                                                     // import: fábrica de Logger
import org.springframework.http.HttpStatus;                                         // import: códigos HTTP
import org.springframework.http.ResponseEntity;                                     // import: resposta HTTP com corpo
import org.springframework.http.converter.HttpMessageNotReadableException;          // import: erro de JSON ilegível
import org.springframework.web.HttpRequestMethodNotSupportedException;              // import: erro de método HTTP errado
import org.springframework.web.bind.MethodArgumentNotValidException;                // import: erro de validação (@Valid)
import org.springframework.web.bind.MissingServletRequestParameterException;        // import: erro de parâmetro faltando
import org.springframework.web.bind.annotation.ExceptionHandler;                    // import: marca um método que trata um tipo de erro
import org.springframework.web.bind.annotation.RestControllerAdvice;                // import: marca a classe que trata erros de todos os controllers
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;  // import: erro de tipo errado na URL
import org.springframework.web.servlet.resource.NoResourceFoundException;           // import: erro de rota inexistente

/** Padroniza todas as respostas de erro: {"erro": "...", "campos": {...}}. */
@RestControllerAdvice
public class ErroHandler {
    private static final Logger log = LoggerFactory.getLogger(ErroHandler.class);

    public record ErroResposta(String erro, Map<String, String> campos) {}

    private ResponseEntity<ErroResposta> resposta(HttpStatus status, String mensagem, Map<String, String> campos) {
        return ResponseEntity.status(status).body(new ErroResposta(mensagem, campos));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErroResposta> api(ApiException e) {
        return resposta(e.getStatus(), e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> validacao(MethodArgumentNotValidException e) {
        Map<String, String> campos = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fe -> campos.putIfAbsent(fe.getField(), fe.getDefaultMessage()));
        return resposta(HttpStatus.BAD_REQUEST, "Verifique os campos informados.", campos);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResposta> parametroAusente(MissingServletRequestParameterException e) {
        return resposta(HttpStatus.BAD_REQUEST, "Falta o parâmetro '" + e.getParameterName() + "'.", null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> corpoInvalido(HttpMessageNotReadableException e) {
        return resposta(HttpStatus.BAD_REQUEST, "O corpo da requisição está inválido ou incompleto.", null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tipoInvalido(MethodArgumentTypeMismatchException e) {
        String mensagem = "O valor '" + e.getValue() + "' é inválido para o parâmetro '" + e.getName() + "'.";
        return resposta(HttpStatus.BAD_REQUEST, mensagem, null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResposta> metodoNaoSuportado(HttpRequestMethodNotSupportedException e) {
        return resposta(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não suportado para este endpoint.", null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> rotaInexistente(NoResourceFoundException e) {
        return resposta(HttpStatus.NOT_FOUND, "Recurso não encontrado.", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> erroGenerico(Exception e) {
        log.error("Erro inesperado no controller", e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor.", null);
    }
}

