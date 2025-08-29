# ConcurrentLabs

ConcurrentLabs é um sistema back-end desenvolvido em Java com Spring Boot para o gerenciamento de reservas de laboratórios de informática. A aplicação foi projetada para lidar com alta concorrência, garantindo controle de acesso a recursos limitados e processando confirmações de forma assíncrona e resiliente.

O sistema utiliza java.util.concurrent.Semaphore para o controle de acesso imediato aos computadores e um sistema de processamento em lote (batch processing) para confirmar e notificar as reservas, garantindo que a aplicação permaneça responsiva sob carga.

## Arquitetura e Fluxo de uma Reserva

O processo de criação de uma reserva é dividido em duas fases principais: solicitação síncrona e confirmação assíncrona. Isso garante que o usuário receba uma resposta rápida enquanto as tarefas mais pesadas são executadas em segundo plano.

## Funcionalidades principais

* Controle de Concorrência por Recurso: Cada laboratório possui seu próprio Semaphore, garantindo que o controle de capacidade seja granular e eficiente.

* Feedback Imediato: O uso de Semaphore.tryAcquire() permite que o sistema dê uma resposta instantânea (sucesso ou falha por capacidade) ao usuário, sem bloquear a thread da requisição.

* Processamento Assíncrono em Lote: As reservas são confirmadas em lotes para otimizar operações de banco de dados e notificações.

* Mecanismo de Gatilho Híbrido: O processamento de lotes é acionado por duas condições:

  1. Tamanho: Quando 5 reservas se acumulam na fila.

  2. Tempo: A cada 30 segundos, para processar reservas remanescentes e evitar que fiquem "esquecidas".

* Transações Isoladas: Cada reserva dentro de um lote é processada em sua própria transação (Propagation.REQUIRES_NEW), garantindo que a falha de uma não afete as outras.

* Notificação por Email: Professores são notificados por email quando suas reservas são confirmadas, realizadas através do org.springframework.mail.javamail.JavaMailSender.


## Componentes e Conceitos Técnicos

1. ReservaService (O Porteiro)
Responsabilidade: Ponto de entrada para novas reservas. Valida os dados e gerencia o acesso concorrente.

    * Técnica: Utiliza um ConcurrentHashMap<Long, Semaphore> para manter um semáforo para cada laboratório. A operação computeIfAbsent garante a criação thread-safe de novos semáforos conforme necessário.
    * Fluxo:

        1. Recebe a requisição de reserva.

        2. Tenta adquirir (tryAcquire()) um "lugar" no semáforo do laboratório.

        3. Se bem-sucedido, cria a Reserva com status PENDENTE e a enfileira no ProcessamentoLoteService.

        4. Se falhar, lança uma CapacidadeExcedidaException, resultando em uma resposta de erro imediata ao usuário.
      
2. ProcessamentoLoteService (A Linha de Montagem)
   Responsabilidade: Orquestrar a confirmação das reservas de forma assíncrona e otimizada.

    * Técnica: Implementa o padrão Produtor-Consumidor.
        1. Produtor: ReservaService adiciona reservas na fila.
        2. Consumidor: Um ScheduledExecutorService consome da fila periodicamente.

    * Estrutura:
        1. BlockingQueue<Reserva>: Fila segura para a comunicação entre threads.
        2. ScheduledExecutorService: Agenda a execução do processamento em intervalos fixos.
        3. @Transactional(propagation = Propagation.REQUIRES_NEW): Garante que cada confirmação de reserva seja atômica e independente.

3. NotificacaoService (O Mensageiro)
   
    * Responsabilidade: Enviar notificações de confirmação para os professores.
    * Técnica: Abstrai a lógica de envio de emails utilizando o JavaMailSender do Spring. Formata uma mensagem clara e informativa para o usuário final.

## Como Executar o Projeto

### Pré Requisitos
* Java Development Kit (JDK) 17 ou superior.
* Maven 3x ou superior.
* Um servidor SMTP (como o do Gmail, SendGrid, ou um local como o MailHog) para o envio de emails.

### Configuração
1. Clone o repositório:
```git
   git clone https://github.com/seu-usuario/ConcurrentLabs.git
   cd ConcurrentLabs
```
2. Configure o application.yml ou application.properties:
   Localizado em src/main/resources/application.properties, configure o acesso ao banco de dados e as credenciais do seu servidor de email.
```yml
# Configuração do Banco de Dados (Ex: H2, PostgreSQL)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Configuração do Servidor de Email
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=seu-email@example.com
spring.mail.password=sua-senha
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Execução
Utilize o Maven Wrapper para compilar e executar a aplicação:

```bash
./mvnw spring-boot:run
```

## Endpoints da API (Exemplo)
### Request
1. Criar uma nova reserva
   * URL: POST /api/reservas
   * Headers: Content-Type: application/json
   * Corpo da Requisição (JSON):

``` json

{
   "laboratorioId": 1,
   "professorId": 101,
   "dataHora": "2025-10-15T14:00:00"
}
```
### Respostas Possíveis:

* 201 Created: Reserva aceita e enfileirada para processamento.

``` json
{
    "id": 44,
    "laboratorioNome": "Math",
    "professorNome": "Eduardo",
    "dataHora": "2025-08-28T21:44:42.3290415",
    "status": "PENDENTE"
}
``` 
* 409 Conflict: Não há computadores disponíveis no momento.

``` json
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
* 404 Not Found: Laboratório ou professor não encontrado.

``` json
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