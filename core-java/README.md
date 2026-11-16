# Núcleo da Lia em Java

Entrega de Domain Driven Design – Java da Global Solution 2026.2 (CitizenTech), equipe AuxiLia.

A Lia é um assistente de voz para quem não consegue usar o celular, o gov.br ou o INSS sozinho. Este projeto é o núcleo de regras dela: ele decide o **risco** de cada ação, a **confirmação** que a Lia pede antes de agir, o **nível de autonomia** da pessoa e esconde CPF, telefone e e-mail dos textos.

| Integrante | RM |
|---|---|
| João Victor Melo Santos | 566640 |
| Murilo Jeronimo Ferreira Nunes | 560641 |
| Vinicius Kozonoe Guaglini | 567264 |
| Yan Lucas Gonçalves da Silva | 567046 |

## Como rodar

Precisa do JDK 25 ou mais novo. O Maven não precisa estar instalado: o projeto traz o Maven Wrapper (`mvnw`).

### 1. Os 4 casos, sem servidor

No IntelliJ, abra `src/main/java/br/com/fiap/lia/Main.java` e rode o `main`.

Pelo terminal, dentro da pasta `core-java`:

```
./mvnw -q compile
java -cp target/classes br.com.fiap.lia.Main
```

O `Main` atende a Dona Maria, uma usuária fictícia, e mostra:

| Caso | Pedido | Resultado |
|---|---|---|
| 1. Consulta | "quero ver meu dinheiro do INSS" | `AcaoConsulta`, risco `BAIXO`, sem confirmação |
| 2. Ação comum | "avisa meu filho que cheguei" | `AcaoComum`, risco `MEDIO`, pede um "sim" |
| 3. Dinheiro | "paga esse boleto de 150 reais" | `AcaoFinanceira`, risco `ALTO`, confirmação dupla |
| 4. Pedido ambíguo | "manda aquilo pra ela" | Nenhuma ação: a Lia pergunta o que e para quem |

Ele mostra também três extras: a autonomia subindo de 0 para 1, um CPF sendo escondido e a Lia parando de usar a IA quando a pessoa retira a autorização (LGPD).

### 2. O servidor

No IntelliJ, rode a classe `CoreJavaApplication`. Pelo terminal:

```
./mvnw spring-boot:run
```

No cmd ou no PowerShell do Windows, use `mvnw.cmd` no lugar de `./mvnw`.

O núcleo sobe em `http://localhost:8080`, com três endereços:

| Método | Endereço | Para quê |
|---|---|---|
| `GET` | `/nucleo/saude` | Saber se o núcleo está no ar |
| `POST` | `/nucleo/decidir` | Decidir o risco e a confirmação de uma ação |
| `POST` | `/nucleo/pseudonimizar` | Esconder CPF, telefone e e-mail de um texto |

O JSON de cada um, os erros e os comandos de teste estão no [CONTRATO.md](CONTRATO.md).

## Onde o núcleo entra na Lia

```
App (celular) ──> API da Lia (Python, FastAPI, porta 8000) ──> IA (Gemini)
                          │
                          └──> Núcleo Java (Spring Boot, porta 8080)
```

Quando a IA devolve uma ação, a API em Python consulta o núcleo antes de responder ao app. Se o núcleo estiver fora do ar, a Lia continua funcionando.

## Organização do código

Tudo fica em `src/main/java/br/com/fiap/lia`:

| Pacote | O que tem | Depende do Spring? |
|---|---|---|
| `dominio` | As regras e os dados da Lia: `Acao` e as três filhas, `Pedido`, `Usuario`, `Familiar`, `Consentimento`, `Progresso` e os enums | Não |
| `aplicacao` | Os serviços que usam o domínio: `FabricaDeAcoes`, `PoliticaDeConfirmacao`, `GestorDeAutonomia` e `Pseudonimizador` | Não |
| `config` | `NucleoConfig`, que apresenta os serviços ao Spring | Sim |
| `web` | `NucleoController`, os DTOs do JSON e o `TratadorDeErros` | Sim |

Na raiz do pacote ficam o `Main` (os 4 casos) e a `CoreJavaApplication` (o servidor).

Decisões de arquitetura:

- **Domínio em Java puro.** `dominio` e `aplicacao` não têm nenhuma anotação do Spring. Por isso o `Main` roda sem servidor, e as regras não mudam se a forma de expor mudar.
- **O controller não decide nada.** Ele recebe o JSON, chama os serviços e devolve a resposta. Toda regra fica no domínio.
- **`Usuario` é a raiz do agregado.** Familiares e consentimentos só entram por ele, e as listas saem como cópias que não podem ser alteradas.
- **Mesmos nomes do banco.** As classes e os campos seguem o modelo de dados do projeto (Database Design).

## Orientação a objetos no código

| Pilar | Onde aparece |
|---|---|
| Abstração | `Acao` é abstrata: diz o que toda ação precisa responder (`getNivelDeRisco()`, `exigeConfirmacao()` e `descrever()`), sem dizer como |
| Herança | `AcaoConsulta`, `AcaoComum` e `AcaoFinanceira` estendem `Acao` e aproveitam o `tipo` e a validação dela |
| Polimorfismo | `PoliticaDeConfirmacao.decidir()` chama `acao.exigeConfirmacao()` e `acao.getNivelDeRisco()` sem saber qual das três filhas recebeu. Cada filha responde do seu jeito |
| Encapsulamento | Os campos são `private`, e `final` sempre que possível. Os construtores validam os dados, então não existe objeto inválido. `Usuario` e `GestorDeAutonomia` só entregam cópias das suas coleções |

## Dados e privacidade

- Nenhum dado pessoal real. A Dona Maria, o familiar dela e o CPF dos exemplos são fictícios.
- O `Pseudonimizador` troca CPF, telefone e e-mail por `[CPF]`, `[TELEFONE]` e `[EMAIL]` antes de o texto ir para a IA.
- O atendimento só segue para a IA se a pessoa tiver autorizado (`Consentimento` com a finalidade `USO_IA_NUVEM`). No `Main`, a resposta da IA é simulada.
- O núcleo não usa chave de IA nem guarda dados.