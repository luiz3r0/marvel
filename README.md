# Marvel App

## Descrição

O **Marvel App** é um aplicativo Android desenvolvido para listar HQs (comics) e seus personagens, utilizando a API da Marvel. Os usuários podem marcar HQs como favoritas, visualizar detalhes de cada HQ e seus personagens, além de acessar uma seção exclusiva de HQs favoritas. O aplicativo implementa paginação e oferece uma navegação fluida com uma interface amigável e intuitiva.

## Funcionalidades

- Listar HQs da Marvel.
- Exibir personagens de cada HQ.
- Marcar e desmarcar HQs como favoritas.
- Seção dedicada para HQs favoritas.
- Paginação para carregar mais HQs e personagens conforme a navegação.
- Tratamento de erros e mensagens amigáveis.

## Arquitetura

Este projeto foi desenvolvido seguindo a arquitetura **MVVM** (Model-View-ViewModel), garantindo uma separação clara entre as responsabilidades e promovendo um fluxo de dados reativo e modular. O uso de **DataBinding** permite a vinculação eficiente de dados diretamente na interface do usuário, tornando o desenvolvimento mais ágil e minimizando código boilerplate.

Além disso, o projeto implementa princípios da **Clean Architecture** e **SOLID**, o que garante alta coesão, baixo acoplamento e uma fácil escalabilidade e manutenção do código. A injeção de dependência é gerenciada com **Koin**, que simplifica o fornecimento de dependências de forma eficiente e desacoplada.

A estrutura do aplicativo é composta por uma única **Activity** que atua como um controlador de navegação e diversos **Fragments**, responsáveis pela exibição das diferentes telas do aplicativo. Essa abordagem promove um design flexível e reutilizável, além de facilitar a manutenção e a testabilidade da aplicação.

## Tecnologias Utilizadas

- **Linguagens**
  - **Kotlin**: Linguagem de programação moderna e concisa para desenvolvimento Android.
  - **XML**: Linguagem de marcação utilizada para definir layouts e recursos de interface no Android.
- **Arquitetura**
  - **MVVM (Model-View-ViewModel)**: Arquitetura que separa a lógica de negócios da interface de usuário.
  - **DataBinding**: Biblioteca para vinculação de dados à UI, facilitando a atualização reativa da interface.
- **Android Jetpack**
  - **ViewModel**: Gerencia e armazena dados relacionados à UI, permitindo que eles sobrevivam a mudanças de configuração.
  - **LiveData**: Observa mudanças nos dados e atualiza a UI de forma reativa.
  - **Room**: Biblioteca para armazenamento local e gerenciamento de banco de dados SQLite.
  - **Paging**: Facilitador para exibição de grandes conjuntos de dados em páginas.
  - **Navigation**: Simplifica a navegação entre diferentes telas do aplicativo.
  - **Lifecycle**: Gerencia o ciclo de vida dos componentes de UI.
  - **StateFlow**: Emissão de estados e gestão de dados reativos dentro do ciclo de vida do aplicativo.
  - **Flow**: Emissão de uma sequência de valores ao longo do tempo, integrado com coroutines para processamento assíncrono.
  - **ConstraintLayout**: Layout flexível para construção de interfaces complexas, facilitando a definição de restrições entre elementos de UI.
  - **Coroutines**: Biblioteca para simplificar o código assíncrono e gerenciar tarefas em background. Parte do ecossistema Kotlin, integrada ao Jetpack para facilitar a programação assíncrona.
- **Manipulação de Dados e Redes**
  - **Retrofit**: Biblioteca para integração com APIs e manipulação de solicitações HTTP.
  - **Gson**: Biblioteca para conversão entre JSON e objetos Java/Kotlin.
  - **OkHttp**: Cliente HTTP para comunicação com serviços web.
- **Carregamento de Imagens**
  - **Glide**: Biblioteca para carregamento e cache de imagens.
- **Injeção de Dependência**
  - **Koin**: Biblioteca para injeção de dependência, simplificando a configuração e o uso das dependências no Android.

## Requisitos do Projeto

### 1. Listar HQs

- Integrar com a API da Marvel para buscar HQs.
- Paginar os resultados, exibindo 15 HQs por vez.

### 2. Favoritar HQs

- Implementar funcionalidade para marcar/desmarcar HQs como favoritas.
- Armazenar HQs favoritas localmente com Room.
- Exibir as HQs favoritas em uma seção separada.

## Requisitos de Instalação

### Pré-requisitos

- Android Studio (versão mais recente).
- Kotlin 1.8+.
- Gradle 7.0+.

### Passos para configuração

1. **Clone o repositório:**

   ```
   bash
   
   
   Copiar código
   git clone https://github.com/seu-usuario/marvel-comics-app.git
   ```

2. **Abra o projeto no Android Studio.**

3. **Configure a API Key da Marvel:**

   - Acesse [Marvel Developer](https://developer.marvel.com/) e crie uma conta.

   - Gere sua chave pública e privada.

   - Insira as chaves no arquivo `local.properties`:

     ```
     propertiesCopiar códigomarvelPublicKey=your_public_key
     marvelPrivateKey=your_private_key
     ```

4. **Atualize as chaves no arquivo `build.gradle` do módulo:**

   No arquivo `build.gradle` (normalmente localizado em `app/build.gradle`), adicione as chaves da Marvel da seguinte forma:

   ```
   gradleCopiar códigoandroid {
       namespace = "br.com.marvel"
       compileSdk = 34
   
       defaultConfig {
           applicationId = "br.com.marvel"
           minSdk = 26
           targetSdk = 35
           versionCode = 1
           versionName = "1.0"
   
           testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
   
           // Adicione as chaves da Marvel aqui
           buildConfigField("String", "PUBLIC_KEY", "\"public_key\"")
           buildConfigField("String", "PRIVATE_KEY", "\"private_key\"")
       }
   }
   ```

5. **Sincronize o projeto com Gradle.**

6. **Execute o aplicativo em um dispositivo ou emulador Android.**

## Testes

Este projeto inclui testes unitários e de interface:

- **Testes Unitários**: Testes de lógica de negócio usando JUnit e Mockito.
- **Testes de Interface**: Validação do comportamento da UI com Espresso.

### Executando os testes

Para rodar os testes unitários e de interface:

1. Abra o Android Studio.
2. Navegue até o menu "Run" e escolha "Run Tests".
3. Visualize os resultados no painel "Run".

## Tratamento de Erros

O aplicativo implementa tratamento de erros para:

- **Falhas de Conexão com a API**: Detecta e trata falhas de comunicação com o servidor, exibindo mensagens apropriadas ao usuário para informar sobre problemas de conectividade.
- **Exibição de Mensagens Amigáveis ao Usuário**: Fornece feedback claro e amigável ao usuário em caso de erro, para que ele entenda o problema e, se possível, saiba como resolvê-lo.
- **Testes de Conectividade**: O aplicativo inclui testes para verificar a disponibilidade de conexão à Internet. Estes testes asseguram que o aplicativo funcione corretamente em diferentes condições de rede.

## Considerações Finais

Este aplicativo foi desenvolvido com foco em boas práticas de código, como separação de responsabilidades, reatividade e modularidade. Aproveite para explorar o mundo da Marvel enquanto navega pelas HQs e gerencia suas favoritas!

## Licença

Este projeto está licenciado sob a MIT License.
