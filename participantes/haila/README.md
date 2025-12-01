# API de Teste Backend - Participante: Haila Mello Ricardo

Este repositório contém a solução do case técnico de **Teste Backend**, desenvolvendo uma API HTTP simples (com pontos a melhorar), com endpoints de transações e extrato de clientes, pronta para ser executada via Docker com load balancing.

---

## Funcionalidades

- **POST /clientes/{id}/transacoes**: Registrar transações de crédito (`c`) ou débito (`d`), respeitando o limite do cliente.
- **GET /clientes/{id}/extrato**: Consultar saldo total e últimas 10 transações.
- **Validações implementadas**:
    - Saldo de débito não pode ultrapassar o limite do cliente.
    - Campos do payload validados conforme especificação.
    - Retorno HTTP 422 para requisições inválidas e 404 para clientes inexistentes.

- **Clientes pré-cadastrados**:
  | ID | Limite  | Saldo Inicial |
  |----|-------- |---------------|
  | 1  | 100000  | 0             |
  | 2  | 80000   | 0             |
  | 3  | 1000000 | 0             |
  | 4  | 10000000| 0             |
  | 5  | 500000  | 0             |

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- PostgreSQL 15
- Docker / Docker Compose
- HAProxy (Load Balancer round-robin)
- Maven

---

## Arquitetura Docker

A aplicação foi conteinerizada e organizada em **4 serviços**:

1. **db**: Banco PostgreSQL com dados persistidos.
2. **app1**: Primeira instância da API.
3. **app2**: Segunda instância da API (para load balancing).
4. **haproxy**: Load balancer que distribui requisições para as instâncias da API usando round-robin na porta **9999**.

**Limites de recursos aplicados**:
- CPU: máximo 1.5 unidades no total
- Memória: máximo 550MB no total

---

**Build da API (opcional se usar imagens públicas):**

sudo docker build -t haila-api .

**Subir a stack no Docker Swarm:**

sudo docker swarm init
sudo docker stack deploy -c docker-compose.yml haila

**Testar a API via Postman ou curl:**

Endpoint de transações:

POST http://localhost:9999/clientes/1/transacoes
{
  "valor": 1000,
  "tipo": "c",
  "descricao": "teste"
}


Endpoint de extrato:

GET http://localhost:9999/clientes/1/extrato


Todas as requisições passam pelo HAProxy, que distribui as requisições entre app1 e app2 automaticamente.