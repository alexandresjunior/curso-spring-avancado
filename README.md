# curso-spring-avancado
Material das aulas do curso de Spring Boot Avançado

Aqui está o material completo para a **Aula 3**, dividido entre o guia de configuração (Markdown) e a implementação do código (Java/Spring).

---

### Guia de Configuração do Google Cloud (Passo a Passo)

Para habilitar o "Login com Google", é necessário registrar a aplicação no Google Cloud Platform. Siga estes passos antes de rodar o código.

#### 1. Criar o Projeto

1. Acesse o [Google Cloud Console](https://console.cloud.google.com/).
2. Clique no seletor de projetos (topo esquerdo) e selecione **"New Project"**.
3. Nomeie como `ProCardio-API` e clique em **Create**.

#### 2. Configurar a Tela de Consentimento (OAuth Consent Screen)

1. No menu lateral, vá em **APIs & Services > OAuth consent screen**.
2. Selecione **External** (para testes) e clique em **Create**.
3. Preencha:
* **App Name:** ProCardio
* **User support email:** Seu email.
* **Developer contact information:** Seu email.


4. Clique em **Save and Continue** até chegar na tela final (não precisa adicionar escopos extras por enquanto, o padrão `email` e `profile` já vem marcado).

#### 3. Criar Credenciais

1. No menu lateral, vá em **APIs & Services > Credentials**.
2. Clique em **+ CREATE CREDENTIALS** > **OAuth client ID**.
3. Em **Application type**, escolha **Web application**.
4. Em **Name**, coloque `ProCardio Spring Boot`.
5. **Authorized redirect URIs** (Muito Importante):
* Adicione: `http://localhost:8080/login/oauth2/code/google`
* *Nota:* Esta é a URL padrão que o Spring Security usa para receber o código de autorização do Google.


6. Clique em **Create**.

#### 4. Obter ID e Secret

* Uma janela aparecerá com o **Your Client ID** e **Your Client Secret**.
* Copie esses valores, você precisará deles no `application.properties`.

---
