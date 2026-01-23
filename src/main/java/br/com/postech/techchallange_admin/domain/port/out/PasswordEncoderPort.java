package br.com.postech.techchallange_admin.domain.port.out;

/**
 * Porta de saída (Outbound Port) para criptografia de senhas
 * Interface que define o contrato para codificação e validação de senhas
 * Implementada pela camada de infraestrutura
 */
public interface PasswordEncoderPort {

    /**
     * Codifica uma senha em texto plano para um hash seguro
     *
     * @param rawPassword a senha em texto plano
     * @return o hash da senha codificada
     */
    String encode(String rawPassword);

    /**
     * Verifica se uma senha em texto plano corresponde ao hash codificado
     *
     * @param rawPassword a senha em texto plano a ser verificada
     * @param encodedPassword o hash codificado para comparação
     * @return true se a senha corresponder ao hash, false caso contrário
     */
    boolean matches(String rawPassword, String encodedPassword);
}
