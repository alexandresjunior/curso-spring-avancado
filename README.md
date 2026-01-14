# curso-spring-avancado
Material das aulas do curso de Spring Boot Avançado

---

## Guia de Configuração do Google Cloud (Passo a Passo)

Para habilitar o "Login com Google", é necessário registrar a aplicação no Google Cloud Platform. Siga estes passos antes de rodar o código.

### 1. Criar o Projeto

1. Acesse o [Google Cloud Console](https://console.cloud.google.com/).
2. Clique no seletor de projetos (topo esquerdo) e selecione **"New Project"**.
3. Nomeie como `ProCardio-API` e clique em **Create**.

### 2. Configurar a Tela de Consentimento (OAuth Consent Screen)

1. No menu lateral, vá em **APIs & Services > OAuth consent screen**.
2. Selecione **External** (para testes) e clique em **Create**.
3. Preencha:
* **App Name:** ProCardio
* **User support email:** Seu email.
* **Developer contact information:** Seu email.


4. Clique em **Save and Continue** até chegar na tela final (não precisa adicionar escopos extras por enquanto, o padrão `email` e `profile` já vem marcado).

### 3. Criar Credenciais

1. No menu lateral, vá em **APIs & Services > Credentials**.
2. Clique em **+ CREATE CREDENTIALS** > **OAuth client ID**.
3. Em **Application type**, escolha **Web application**.
4. Em **Name**, coloque `ProCardio Spring Boot`.
5. **Authorized redirect URIs** (Muito Importante):
* Adicione: `http://localhost:8080/login/oauth2/code/google`
* *Nota:* Esta é a URL padrão que o Spring Security usa para receber o código de autorização do Google.


6. Clique em **Create**.

### 4. Obter ID e Secret

* Uma janela aparecerá com o **Your Client ID** e **Your Client Secret**.
* Copie esses valores, você precisará deles no `application.properties`.

---

## Protocolos de Segurança e 2FA

### 1. OAuth 2.0 e OpenID Connect (OIDC)

Muitos desenvolvedores confundem os dois, mas eles têm propósitos distintos:

* **OAuth 2.0 (Autorização):** É como um "crachá de visitante". Ele permite que uma aplicação acesse recursos em outra aplicação em nome do usuário, sem compartilhar a senha.
    * *Exemplo:* O LinkedIn acessando seus contatos do Gmail para sugerir conexões. O LinkedIn não tem sua senha do Google, apenas um token de acesso limitado.
* **OpenID Connect (Autenticação):** É uma camada construída *sobre* o OAuth 2.0. Ele serve para dizer à aplicação **quem** é o usuário.
    * *Exemplo:* O botão "Entrar com Google". O Google diz ao ProCardio: "Este é o usuário João, email joao@gmail.com".

#### Fluxos Principais (Grant Types)

1.  **Authorization Code:** O mais seguro e comum. O usuário é redirecionado para o servidor de autorização (ex: Google), loga lá, e o Google devolve um "código" para o backend da sua aplicação. Seu backend troca esse código pelo Token final. (Foi isso que o Spring Security fez magicamente na Aula 3).
2.  **Client Credentials:** Usado para comunicação **Máquina-para-Máquina** (Microsserviços). Não existe usuário humano. O serviço A se autentica no serviço B usando um ID e Segredo.

---

### 2. Autenticação de Dois Fatores (2FA / MFA)

O 2FA adiciona uma camada extra de segurança baseada em "algo que você tem" (seu celular) além de "algo que você sabe" (sua senha).

#### Algoritmo TOTP (Time-based One-Time Password)
Usaremos o padrão **TOTP**, o mesmo utilizado pelo Google Authenticator e Authy.
* O servidor e o app do celular compartilham um **Segredo (Secret Key)**.
* A cada 30 segundos, ambos usam esse segredo + o horário atual para gerar um código numérico de 6 dígitos.
* Se o código gerado pelo usuário for igual ao calculado pelo servidor, o acesso é liberado.
