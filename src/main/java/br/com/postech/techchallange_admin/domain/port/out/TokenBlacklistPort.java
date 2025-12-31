package br.com.postech.techchallange_admin.domain.port.out;

/**
 * Porta de saída (Outbound Port) para gerenciamento de blacklist de tokens
 * Interface que define o contrato para adicionar e verificar tokens na blacklist
 * Implementada pela camada de infraestrutura
 */
public interface TokenBlacklistPort {

    /**
     * Adiciona um token à blacklist
     *
     * @param token o token a ser adicionado à blacklist
     */
    void blacklistToken(String token);

    /**
     * Verifica se um token está na blacklist
     *
     * @param token o token a ser verificado
     * @return true se o token estiver na blacklist, false caso contrário
     */
    boolean isTokenBlacklisted(String token);
}
