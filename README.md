# Delivery Orders API

API REST utilizando Java 17 e Spring Boot para gerenciamento de pedidos de entrega.

---

## Stacks

- **Java 17**
- **Spring Boot 4.0.3**
    - Spring Web
    - Spring Data JPA
    - Spring Data Redis
    - Spring Cache
    - Spring Security
    - Spring Validation
    - Spring Actuator
- **PostgreSQL 18**
- **Redis 7.4**
- **Lombok**
- **MapStruct**

---

## Docker Setup

### Estrutura

```
src/main/resources/application-local.yml.model     # Modelo de configuração local
.env.example                                       # Template de variáveis
.env.local.model                                   # Modelo de variáveis locais
docker-compose.yml                                 # Infraestrutura base (Redis + PostgreSQL)
docker-compose.override.yml                        # Portas expostas para dev (carregado automático)
```

### Primeiros passos

1. Copie `.env.local.model` para `.env.local`
2. Copie `application-local.yml.model` para `src/main/resources/application-local.yml`
3. Suba a infraestrutura

### Acesso local

- **API:** http://localhost:8080/v1
- **Swagger:** http://localhost:8080/swagger-ui/index.html

### Comandos

```bash
# Conferir
docker compose --env-file .env.local config

# Subir infraestrutura
docker compose --env-file .env.local up -d

# Encerrar
docker compose down
```
