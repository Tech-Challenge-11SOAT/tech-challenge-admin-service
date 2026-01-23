# Correções da Arquitetura Hexagonal

## Problemas Identificados na Arquitetura Anterior

### Violação 1: Dependência Direta de PasswordEncoder

#### Antes (Incorreto)

```java
// application/service/CriarAdminService.java
@Service
public class CriarAdminService implements CriarAdminUseCase {
    private final PasswordEncoder passwordEncoder; // ❌ Dependência direta de infraestrutura
    
    public CriarAdminService(AdminRepositoryPort adminRepositoryPort,
                             PasswordEncoder passwordEncoder, // ❌ Spring Security
                             LogAdminActionUseCase logAdminActionUseCase) {
        // ...
    }
    
    @Override
    public Admin criar(Admin admin) {
        String senhaCriptografada = passwordEncoder.encode(admin.getSenhaHash());
        // ...
    }
}
```

**Problemas:**
- ❌ A camada de aplicação depende diretamente de uma classe do Spring Security (`org.springframework.security.crypto.password.PasswordEncoder`)
- ❌ Viola o princípio de inversão de dependências (DIP)
- ❌ Dificulta testes unitários (precisa mockar classes do framework)
- ❌ Dificulta trocar a implementação de criptografia (ex: BCrypt → Argon2)
- ❌ Acopla a lógica de negócio ao framework Spring

#### Depois (Correto)

```java
// domain/port/out/PasswordEncoderPort.java
public interface PasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}

// infrastructure/security/PasswordEncoderAdapter.java
@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {
    private final PasswordEncoder passwordEncoder; // ✅ Encapsulado na infraestrutura
    
    public PasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

// application/service/CriarAdminService.java
@Service
public class CriarAdminService implements CriarAdminUseCase {
    private final PasswordEncoderPort passwordEncoderPort; // ✅ Depende apenas da porta
    
    public CriarAdminService(AdminRepositoryPort adminRepositoryPort,
                             PasswordEncoderPort passwordEncoderPort, // ✅ Interface do domínio
                             LogAdminActionUseCase logAdminActionUseCase) {
        // ...
    }
    
    @Override
    public Admin criar(Admin admin) {
        String senhaCriptografada = passwordEncoderPort.encode(admin.getSenhaHash());
        // ...
    }
}
```

---

### Violação 2: Dependência Direta de TokenBlacklistService

#### Antes (Incorreto)

```java
// application/service/AutenticacaoService.java
@Service
public class AutenticacaoService implements AutenticarAdminUseCase, LogoutAdminUseCase {
    private final TokenBlacklistService tokenBlacklistService; // ❌ Dependência direta de infraestrutura
    
    public AutenticacaoService(AdminRepositoryPort adminRepositoryPort,
                               PasswordEncoder passwordEncoder,
                               TokenBlacklistService tokenBlacklistService) { // ❌ Classe de infraestrutura
        // ...
    }
    
    @Override
    public void logout(String token) {
        tokenBlacklistService.blacklistToken(token); // ❌ Chamada direta
    }
}
```

**Problemas:**
- ❌ A camada de aplicação conhece detalhes de implementação da infraestrutura
- ❌ Viola o princípio de inversão de dependências
- ❌ Dificulta testes unitários
- ❌ Se a implementação de blacklist mudar (ex: de memória para Redis), precisa alterar a camada de aplicação

#### Depois (Correto)

```java
// domain/port/out/TokenBlacklistPort.java
public interface TokenBlacklistPort {
    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
}

// infrastructure/security/TokenBlacklistAdapter.java
@Component
public class TokenBlacklistAdapter implements TokenBlacklistPort {
    private final TokenBlacklistService tokenBlacklistService; // ✅ Encapsulado
    
    public TokenBlacklistAdapter(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }
    
    @Override
    public void blacklistToken(String token) {
        tokenBlacklistService.blacklistToken(token);
    }
    
    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistService.isTokenBlacklisted(token);
    }
}

// application/service/AutenticacaoService.java
@Service
public class AutenticacaoService implements AutenticarAdminUseCase, LogoutAdminUseCase {
    private final TokenBlacklistPort tokenBlacklistPort; // ✅ Depende apenas da porta
    
    public AutenticacaoService(AdminRepositoryPort adminRepositoryPort,
                               PasswordEncoderPort passwordEncoderPort,
                               TokenBlacklistPort tokenBlacklistPort) { // ✅ Interface do domínio
        // ...
    }
    
    @Override
    public void logout(String token) {
        tokenBlacklistPort.blacklistToken(token); // ✅ Chamada através da porta
    }
}
```

---

## Diagrama de Dependências

### Antes (Violação da Arquitetura Hexagonal)

```
┌─────────────────────────────────────────────────────────┐
│              Application Layer                          │
│  ┌──────────────────────────────────────────────────┐   │
│  │  CriarAdminService                               │   │
│  │  AutenticacaoService                             │   │
│  │  AlterarSenhaAdminService                        │   │
│  └──────────────────────────────────────────────────┘   │
│           │                    │                        │
│           │                    │                        │
│           ▼                    ▼                        │
│  ┌─────────────────┐  ┌──────────────────────┐          │
│  │ AdminRepository │  │ PasswordEncoder      │ ❌       │
│  │ Port (Domain)   │  │ (Spring Security)    │          │
│  └─────────────────┘  └──────────────────────┘          │
│                              │                          │
│                              ▼                          │
│                    ┌───────────────────────┐            │
│                    │ TokenBlacklistService │ ❌         │
│                    │ (Infrastructure)      │            │
│                    └───────────────────────┘            │
└─────────────────────────────────────────────────────────┘
```

### Depois (Arquitetura Hexagonal Correta)

```
┌─────────────────────────────────────────────────────────┐
│              Application Layer                          │
│  ┌──────────────────────────────────────────────────┐   │
│  │  CriarAdminService                               │   │
│  │  AutenticacaoService                             │   │
│  │  AlterarSenhaAdminService                        │   │
│  └──────────────────────────────────────────────────┘   │
│           │                    │                        │
│           │                    │                        │
│           ▼                    ▼                        │
│  ┌─────────────────┐  ┌──────────────────────┐          │
│  │ AdminRepository │  │ PasswordEncoderPort  │ ✅       │
│  │ Port (Domain)   │  │ (Domain Interface)   │          │
│  └─────────────────┘  └──────────────────────┘          │
│                              │                          │
│                              ▼                          │
│                    ┌──────────────────────┐             │
│                    │ TokenBlacklistPort   │ ✅          │
│                    │ (Domain Interface)   │             │
│                    └──────────────────────┘             │
└─────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────┐
│          Infrastructure Layer                           │
│  ┌──────────────────────────────────────────────────┐   │
│  │  PasswordEncoderAdapter                          │   │
│  │  TokenBlacklistAdapter                           │   │
│  │  AdminAdapter                                    │   │
│  └──────────────────────────────────────────────────┘   │
│           │                    │                        │
│           ▼                    ▼                        │
│  ┌─────────────────┐  ┌──────────────────────┐          │
│  │ PasswordEncoder │  │ TokenBlacklistService│          │
│  │ (Spring)        │  │ (In-Memory)          │          │
│  └─────────────────┘  └──────────────────────┘          │
└─────────────────────────────────────────────────────────┘
```

---


## Referências

- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
