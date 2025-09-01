# 🚀 ConcurrentLabs

> Sistema back-end em **Java + Spring Boot** para gerenciamento de reservas de laboratórios de informática, com **alta concorrência**, **processamento assíncrono em lote** e **notificações automáticas por e-mail**.

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.4-6DB33F?logo=springboot" />
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-3.5.4-6DB33F?logo=spring" />
  <img src="https://img.shields.io/badge/Spring%20Validation-3.5.4-6DB33F?logo=spring" />
  <img src="https://img.shields.io/badge/Spring%20Web-3.5.4-6DB33F?logo=spring" />
  <img src="https://img.shields.io/badge/Spring%20Mail-3.5.4-6DB33F?logo=spring" />
  <img src="https://img.shields.io/badge/Spring%20Actuator-3.5.4-6DB33F?logo=spring" />
  <img src="https://img.shields.io/badge/H2-Database-blue?logo=h2" />
  <img src="https://img.shields.io/badge/PostgreSQL-Database-4169E1?logo=postgresql" />
  <img src="https://img.shields.io/badge/Lombok-Enabled-green?logo=java" />
  <img src="https://img.shields.io/badge/Logstash%20Encoder-7.4-FF6600?logo=logstash" />
  <img src="https://img.shields.io/badge/Spring%20Boot-Test-6DB33F?logo=spring" />
  <img src="https://img.shields.io/badge/Fake%20Requisitions-0.1.9-blueviolet" />
</p>

---

## 🧭 Sumário

- [Visão Geral](#-visão-geral)
- [Arquitetura e Fluxo](#-arquitetura-e-fluxo)
- [Funcionalidades](#-funcionalidades)
- [Componentes Técnicos](#-componentes-técnicos)
- [Como Executar](#-como-executar)
   - [Pré-requisitos](#-pré-requisitos)
   - [Configuração](#-configuração)
   - [Execução](#-execução)
- [Exemplos de API](#-exemplos-de-api)
- [Dados de Teste (FakeRequisitions)](#-dados-de-teste-fakerequisitions)
- [Roadmap](#-roadmap)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

---

## 🔎 Visão Geral

ConcurrentLabs é um sistema em **Java 17** com **Spring Boot** para **gerenciamento de reservas de laboratórios de informática**.  
A aplicação foi projetada para **alta concorrência**, com:

- Controle de acesso imediato via `java.util.concurrent.Semaphore`;
- **Processamento assíncrono em lote** para confirmação das reservas;
- **Notificações por e-mail** aos professores quando a reserva é confirmada;
- Observabilidade com **Spring Actuator** e logs estruturados com **Logstash Encoder**.

---

## 🏗️ Arquitetura e Fluxo

O fluxo da reserva ocorre em **duas fases**:

1. **Solicitação síncrona (rápida)**
   - `Semaphore.tryAcquire()` retorna imediatamente **sucesso** (enfileira a reserva como `PENDENTE`) ou **falha** (capacidade esgotada).

2. **Confirmação assíncrona (em lote)**
   - Um **Scheduler** consome uma fila e **confirma as reservas em lotes**, enviando e-mail de notificação.

**Gatilhos do processamento em lote:**
- 📌 **Tamanho**: quando **5 reservas** se acumulam na fila;
- ⏰ **Tempo**: a cada **30 segundos**, para evitar pedidos “encalhados”.

Cada confirmação roda em **transação isolada** (`REQUIRES_NEW`) para que falhas não afetem o lote inteiro.

---

## ✅ Funcionalidades

- **Controle de Concorrência por Laboratório** com `Semaphore` por recurso.
- **Feedback Imediato** ao usuário (aceita/enfileira ou rejeita por capacidade).
- **Processamento Assíncrono em Lote** com `ScheduledExecutorService`.
- **Transações Isoladas** para robustez.
- **Notificação por E-mail** via `JavaMailSender`.
- **Logs Estruturados** prontos para observabilidade (Logstash).
- **Perfis de Banco**: H2 (memória) e PostgreSQL.

---

## ⚙️ Componentes Técnicos

1. **ReservaService** 🛂
   - Ponto de entrada de novas reservas.
   - Mantém `ConcurrentHashMap<Long, Semaphore>` por laboratório.
   - Fluxo:
      1. Recebe a requisição;
      2. `tryAcquire()` no semáforo do laboratório;
      3. Em caso de sucesso, cria `Reserva` com status **PENDENTE** e enfileira;
      4. Em caso de falha, lança `CapacidadeExcedidaException`.

2. **ProcessamentoLoteService** 🔄
   - Padrão **Produtor–Consumidor**: fila (`BlockingQueue`) + `ScheduledExecutorService`.
   - Confirma em lote e envia notificações.
   - `@Transactional(propagation = REQUIRES_NEW)` por item.

3. **NotificacaoService** 📧
   - Envio de e-mails de confirmação para professores.
   - Abstrai `JavaMailSender` com templates simples e mensagens claras.

---

## ▶️ Como Executar

### 📌 Pré-requisitos
- **Java 17+**
- **Maven 3+**
- **SMTP** (Gmail, SendGrid, MailHog etc.)

### 📌 Configuração

1) **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/ConcurrentLabs.git
cd ConcurrentLabs
```
2) Ajuste o application.yml ou application.properties em src/main/resources:
```
# --- Banco de Dados (ex.: H2 em memória) ---
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

# --- E-mail ---
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=seu-email@example.com
spring.mail.password=sua-senha
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# --- Atuator (opcional) ---
management.endpoints.web.exposure.include=health,info,metrics
```
Para PostgreSQL, ajuste spring.datasource.url, username, password e o dialect correspondente.

--- 

### ▶️ Execução

```bash
# Executar com Maven Wrapper
./mvnw spring-boot:run
```

## 📡 Exemplos de API
### **Criar Reserva**
- **POST** /api/reservas
- **Headers:** Content-Type: application/json
```json
{
  "laboratorioId": 1,
  "professorId": 101,
  "dataHora": "2025-10-15T14:00:00"
}
```

### **Respostas**
- 201 Created
```json
{
   "id": 44,
   "laboratorioNome": "Math",
   "professorNome": "Eduardo",
   "dataHora": "2025-08-28T21:44:42.3290415",
   "status": "PENDENTE"
}
```
- 409 Conflict: Não há computadores disponíveis no momento.

```json
  {
  "timestamp": "2025-08-28T21:45:25.7759378",
  "status": 409,
  "error": "Conflict",
  "message": "Capacidade 6 laborátorio excedida, não há computadores disponiveis. Tente novamente mais tarde!",
  "path": "/reservas",
  "errorCode": "LAB_CAPACIDADE_EXCECIDA",
  "details": null
  }
 ```
- 404 Not Found: Laboratório ou professor não encontrado.
```json
  {
  "timestamp": "2025-08-28T21:45:54.5706885",
  "status": 404,
  "error": "Not Found",
  "message": "Professor não encontrado pelo id 489",
  "path": "/reservas",
  "errorCode": "RECURSO_NAO_ENCONTRADO",
  "details": null
  }
 ```

## 🧪 Dados de Teste (FakeRequisitions)

Para facilitar testes, debugging e demos, o projeto inclui um controlador para popular o banco com
dados falsos usando FakeRequisitions . Ele gera múltiplos JSONs aleatórios e envia POSTs para os 
próprios endpoints (ex.: /professores, /laboratorios), automatizando o cadastro. Há dois endpoints 
GET de conveniência — edite os campos conforme suas necessidades de teste. ``` generate/generateLabs```,
``` generate/generateProfessor```.

## 🗺️ Roadmap
- Expor métricas customizadas no Actuator (reservas por minuto, fila, tempo médio).
- Feature flags para alternar entre confirmação imediata vs. em lote.
- Documentação OpenAPI/Swagger UI.
- Estratégias de retry/backoff para e-mail.
- Cache para consultas de disponibilidade.

## 🤝 Contribuição

- Contribuições são bem-vindas!
- Abra uma issue para bugs/idéias.
- Envie um pull request com descrição clara e testes relevantes.


### License

[MIT](LICENSE) © JoaoFXs