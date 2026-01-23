package br.com.postech.techchallange_admin.infrastructure.rest.controller;

import br.com.postech.techchallange_admin.domain.exception.BusinessException;
import br.com.postech.techchallange_admin.infrastructure.rest.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBusinessException_deveRetornar400ComMensagem() {
        String mensagem = "Erro de negócio";
        BusinessException exception = new BusinessException(mensagem);

        ResponseEntity<ErrorResponse> response = handler.handleBusinessException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(mensagem);
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Requisição Inválida");
    }

    @Test
    void handleValidationException_deveRetornar400ComErroDeValidacao() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "Erro de validação");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Erro de Validação");
        assertThat(response.getBody().message()).isEqualTo("Erro de validação");
    }

    @Test
    void handleGenericException_deveRetornar500ComMensagemGenerica() {
        Exception exception = new RuntimeException("Erro interno");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().error()).isEqualTo("Erro Interno");
        assertThat(response.getBody().message()).isEqualTo("Ocorreu um erro inesperado, tente novamente.");
    }
}
