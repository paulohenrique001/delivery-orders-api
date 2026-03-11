# Delivery Orders API
API REST para gerenciamento de pedidos de entrega, com autenticação JWT, cache, rate limiting e documentação OpenAPI.

---

## Tecnologias
- **Java 17** + **Spring Boot 4.0.3**
  - Spring Web, Data JPA, Data Redis, Cache, Security, OAuth2, Validation, Actuator
- **PostgreSQL 18**
- **Redis 7.4**
- **springdoc-openapi 3.0.2**
- **Bucket4j** — rate limiting por IP via Redis
- **MapStruct** — mapeamento de DTOs
- **Lombok**
- **Testcontainers** — testes de integração com containers reais

---

## Como executar

### Pré-requisitos
- Docker e Docker Compose

### 1. Configurar variáveis de ambiente
```bash
cp .env.model .env
```

> `.env.model` valores padrão para ambiente local.  
> `.env.example` todas as chaves disponíveis sem valores.

### 2. Subir e executar
Usando o perfil **stack** sobe infraestrutura + aplicação:
```bash
docker compose --profile stack up -d
```

Ambiente local, recomenda-se usar **infra**, para rodar a aplicação pela IDE:
```bash
docker compose --profile infra up -d
```

> Em seguida, execute a aplicação com o profile `local`.
> OBS: O arquivo application-model.yaml serve como referência das configurações disponíveis.

### Acessos
| Recurso | URL |
|---|---|
| API | http://localhost:8080 |
| Health | http://localhost:8080/actuator/health |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |

---

## Decisões técnicas

### Autenticação
Utiliza-se JWT stateless via Spring OAuth2 Resource Server, o token é gerado no login com o usuário configurado no projeto e enviado como **Bearer Token** nas requisições protegidas pelo Spring Security.

### Cache
Redis como cache de segundo nível para consultas, salvo no Redis com prefixo `dop::` e TTL padrão configurado de cinco minutos, além disso o cache é invalidado automaticamente nas operações de escrita.

### Rate Limiting
Bucket4j com Redis aplicado por IP via filtro antes da autenticação.

### Tratamento de erros
`GlobalExceptionHandler` centralizado que gera respostas padronizadas com `ErrorResponse` contendo mensagem e lista de detalhes para erros de validação ou demais informações.

---

## Testes
Os testes de integração sobem PostgreSQL e Redis automaticamente via **Testcontainers**, é necessário ter o Docker em execução para que funcione corretamente.
```bash
./mvnw test
```

- **Unitários:** regras de negócio e transições de status
- **Integração:** endpoints completos com banco e cache reais
