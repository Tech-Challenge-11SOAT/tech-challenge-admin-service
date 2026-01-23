package br.com.postech.techchallange_admin.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BusinessExceptionTest {

    @Test
    void deveCriarExcecaoComMensagem() {
        String mensagem = "Erro de negócio";
        BusinessException exception = new BusinessException(mensagem);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(mensagem);
    }

    @Test
    void deveSerInstanciaDeRuntimeException() {
        BusinessException exception = new BusinessException("Teste");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    void deveLancarExcecaoQuandoChamada() {
        String mensagem = "Erro de negócio";
        
        assertThatThrownBy(() -> {
            throw new BusinessException(mensagem);
        })
        .isInstanceOf(BusinessException.class)
        .hasMessage(mensagem);
    }
}
