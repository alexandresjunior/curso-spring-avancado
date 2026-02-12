# ProCardio - Sistema de Agendamento e Notificações

Este repositório contém o material prático do curso de Spring Boot Avançado. O projeto consiste em uma solução de saúde para agendamento de consultas médicas, integrando mensageria, inteligência artificial para dicas de saúde e uma arquitetura baseada em microserviços.

## 🚀 Arquitetura do Projeto

O ecossistema é composto por quatro serviços principais:

1. **Gateway Service (Porta 8000):** Atua como o ponto de entrada único (API Gateway), roteando as requisições para os microserviços internos.
2. **ProCardio API (Porta 8080):** O núcleo do sistema (Core), responsável pelo gerenciamento de usuários (pacientes), médicos e o agendamento de consultas.
3. **Notificação Service (Porta 8081):** Microsserviço focado no envio de e-mails, processamento de filas e execução de tarefas agendadas (Schedules).
4. **LLM API (Porta 8082):** Integrador com o Google Gemini para gerar dicas de saúde personalizadas baseadas no clima da região do paciente.

---

## 🛠️ Tecnologias e Conceitos Utilizados

### 1. Spring Cloud Gateway

Utilizado para centralizar as chamadas de API. No arquivo `application.yml` do `gateway-service`, estão configuradas as rotas que direcionam o tráficos `/api/auth/**`, `/api/notificacoes/**` e `/api/gemini/**` para seus respectivos serviços.

* **Documentação:** [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)

### 2. Mensageria com RabbitMQ (AMQP)

O projeto utiliza o modelo de publicação/assinatura para comunicação assíncrona. Quando uma consulta é salva na `ProCardio API`, um evento `ConsultaAgendadaEvent` é enviado para a exchange `procardio.v1.eventos`. O `notificacao-service` consome essa mensagem para disparar o e-mail de confirmação.

* **Conceitos Chave:** Exchanges (Topic), Queues, Dead Letter Queues (DLQ).
* **Documentação:** [Spring AMQP / RabbitMQ](https://spring.io/projects/spring-amqp)

### 3. Spring Boot Starter Mail & Thymeleaf

O serviço de notificações utiliza o Spring Mail integrado ao **Mailtrap** para simular o envio de e-mails. O corpo dos e-mails é renderizado dinamicamente usando o motor de templates **Thymeleaf**, permitindo a criação de mensagens HTML personalizadas com dados da consulta e da IA.

* **Documentação:** [Thymeleaf](https://www.thymeleaf.org/)
* **Observação**: deve-se criar uma conta no [Mailtrap](https://mailtrap.io/).

### 4. Segurança com JWT e OAuth2

A autenticação é protegida por **Spring Security**. O sistema suporta:

* **JWT (JSON Web Token):** Para sessões stateless, gerando tokens com validade de 2 horas.
* **Google OAuth2:** Implementação do Login social com Google no `SecurityConfig` da API principal.
* **Documentação:** [Spring Security](https://spring.io/projects/spring-security)

### 5. Spring AI & Integração com Gemini

O serviço `llm-api` demonstra como consumir modelos de linguagem modernos. Ele utiliza o `RestClient` (introduzido no Spring Boot 3.2) para enviar prompts ao Google Gemini, solicitando dicas de saúde de até 3 linhas para cidades específicas.

* **Documentação:** [Spring AI (Referência)](https://spring.io/projects/spring-ai)

### 6. Agendamento de Tarefas (Scheduled Tasks)

No `notificacao-service`, o `NotificacaoScheduler` executa uma rotina automática toda segunda-feira às 08:00 (via expressão cron `0 0 8 * * 1`), que busca todos os usuários e envia uma dica de saúde semanal.

---

## 📋 Como Executar o Projeto

1. **Pré-requisitos:**
* Java 17 ou superior (disponível na página de [Downloads Oficial](https://www.oracle.com/br/java/technologies/downloads/)).
* MySQL instalado e banco `procardio_db` criado (disponível na página de download do [Instalador para Windows](https://dev.mysql.com/downloads/installer/).
* RabbitMQ rodando (pode ser via Docker - Links para instação do [Docker Desktop](https://docs.docker.com/desktop/setup/install/windows-install/) e [RabbitMQ](https://www.rabbitmq.com/docs/download)).


2. **Configuração de Variáveis:**
Certifique-se de configurar as variáveis de ambiente necessárias nos arquivos `application.properties` (ou configure diretamente no seu IDE):
* `MYSQL_USERNAME` e `MYSQL_PASSWORD`.
* `GEMINI_API_KEY` (para o serviço de LLM - disponível na página de criação de [API Keys do Gemini](https://aistudio.google.com/api-keys)).
* `MAILTRAP_USERNAME` e `MAILTRAP_PASSWORD` (disponíveis na página da [Sandbox](https://mailtrap.io/inboxes)).


3. **Ordem de Inicialização:**
1. `procardio-api` (Core)
2. `llm-api`
3. `notificacao-service`
4. `gateway-service`


4. **Testes:**
Importe o arquivo `Procardio API.postman_collection.json` incluído na raiz do repositório no Postman para testar os endpoints de autenticação, cadastro e agendamento.
