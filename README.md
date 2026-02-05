# 🏥 Projeto ProCardio - Curso Spring Boot Avançado

Bem-vindo ao repositório do projeto **ProCardio**, desenvolvido durante o curso de Spring Boot Avançado. Este projeto simula um sistema de agendamento de consultas médicas com uma arquitetura de microsserviços, integrando autenticação moderna, inteligência artificial generativa e comunicação assíncrona.

---

## 🏗️ Arquitetura do Projeto

O sistema é composto por 4 microsserviços principais que se comunicam entre si. Abaixo, detalhamos a responsabilidade de cada um:

### 1. 🌐 **Gateway Service** (`gateway-service`)

* **Porta:** `8000`
* **Responsabilidade:** É a porta de entrada única para o sistema (Pattern API Gateway). Ele recebe todas as requisições externas e as roteia para os microsserviços corretos.
* **Tecnologia:** [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway).
* **Rotas Configuradas:**
* `/api/auth/**` → Redireciona para `procardio-api`
* `/api/notificacoes/**` → Redireciona para `notificacao-service`
* `/api/gemini/**` → Redireciona para `llm-api`



### 2. 💓 **ProCardio API** (`procardio-api`)

* **Porta:** `8080`
* **Responsabilidade:** O núcleo do negócio. Gerencia Pacientes, Médicos e Agendamentos.
* **Funcionalidades Principais:**
* **Segurança:** Autenticação híbrida usando JWT e OAuth2 (Login com Google).
* **Persistência:** Uso de JPA/Hibernate com banco de dados MySQL.
* **Mensageria (Produtor):** Publica eventos no RabbitMQ quando uma consulta é agendada.


* **Tecnologias:** Spring Data JPA, Spring Security, Spring AMQP.

### 3. 📧 **Notificação Service** (`notificacao-service`)

* **Porta:** `8081`
* **Responsabilidade:** Enviar e-mails transacionais e informativos para os pacientes.
* **Funcionalidades Principais:**
* **Templates:** Uso do **Thymeleaf** para criar e-mails HTML dinâmicos e bonitos.
* **Comunicação (Consumidor):** Escuta filas do RabbitMQ para reagir a agendamentos em tempo real.
* **Scheduler:** Possui rotinas agendadas (`@Scheduled`) para envios em lote (ex: lembretes semanais).


* **Tecnologias:** Spring Mail, Thymeleaf, Spring AMQP, OpenFeign (para buscar dados de outros serviços).

### 4. 🤖 **LLM API** (`llm-api`)

* **Porta:** `8082`
* **Responsabilidade:** Integração com Inteligência Artificial Generativa.
* **Funcionalidade:** Recebe a localização do paciente e gera, via IA, uma dica de saúde personalizada baseada no clima da região.
* **Tecnologias:** [Google Gemini API](https://ai.google.dev/), Spring `RestClient` (HTTP Client moderno do Spring 3.2+).

---

## 🛠️ Tecnologias e Ferramentas Utilizadas

* **Java 17+**: Linguagem base.
* **Spring Boot 3.x / 4.x**: Framework principal.
* **MySQL**: Banco de dados relacional.
* **RabbitMQ**: Broker de mensagens para comunicação assíncrona.
* **Mailtrap**: Servidor SMTP fake para testes de envio de e-mail.
* **Google Cloud Console**: Para credenciais OAuth2 (Login Social) e Gemini AI.
* **Docker**: (Opcional) Para rodar o RabbitMQ e o MySQL em containers.

---

## 🚀 Como Executar o Projeto

### 1. Pré-requisitos

Certifique-se de configurar as variáveis de ambiente ou ajustar os arquivos `application.properties` de cada serviço com suas credenciais:

* **MySQL:** `MYSQL_USERNAME`, `MYSQL_PASSWORD`
* **Google OAuth:** `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`
* **JWT Secret:** `API_TOKEN_SECRET`
* **Mailtrap:** `MAILTRAP_USERNAME`, `MAILTRAP_PASSWORD`
* **Gemini AI:** `GEMINI_API_KEY`, `GEMINI_API_URL`

### 2. Subindo a Infraestrutura

Se você tiver o Docker instalado, suba o RabbitMQ e o MySQL:

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
docker run -d --name mysql-procardio -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:8

```

### 3. Ordem de Inicialização

Para evitar erros de conexão, inicie as aplicações na seguinte ordem:

1. **Gateway Service** (Porta 8000)
2. **LLM API** (Porta 8082)
3. **ProCardio API** (Porta 8080) - *Vai criar as tabelas no banco.*
4. **Notificação Service** (Porta 8081) - *Vai conectar no RabbitMQ e na API principal.*

---

## 📚 Documentação e Links Úteis

Para aprofundar seus estudos, consulte a documentação oficial das ferramentas utilizadas:

* [Spring Boot Documentation](https://docs.spring.io/spring-boot/index.html)
* [Spring Security OAuth2 Client](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html)
* [Spring AMQP (RabbitMQ)](https://docs.spring.io/spring-amqp/reference/)
* [Thymeleaf com Spring](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html)
* [Google Gemini API Docs](https://ai.google.dev/docs)

---

## 🧪 Testando a API (Postman/Insomnia)

### Fluxo de Agendamento com Notificação

1. **Login:** Faça login via endpoint `/api/auth/login` ou gere um token via fluxo OAuth2.
2. **Agendar:** Envie um POST para `/api/consultas` (via Gateway porta 8000).
3. **Processamento:**
* A `procardio-api` salva a consulta e publica mensagem no RabbitMQ.
* O `notificacao-service` consome a mensagem.
* Ele chama a `llm-api` para pegar uma dica de saúde.
* Ele monta o HTML e envia o e-mail via Mailtrap.


4. **Verificação:** Verifique sua caixa de entrada no Mailtrap.

---

**Desenvolvido por:** Alexandre de Souza Jr.
*Professor de Programação e Desenvolvedor de Software*