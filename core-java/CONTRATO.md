# Contrato do núcleo Java com a API da Lia

Este arquivo descreve como a API da Lia (Python) conversa com o núcleo em Java: os endereços, o JSON de entrada e de saída e os erros.

## Onde o núcleo entra

```
App (celular) ──> API da Lia (Python, FastAPI, porta 8000) ──> IA (Gemini)
                          │
                          └──> Núcleo Java (Spring Boot, porta 8080)
```

- O app fala só com a API em Python. O app nunca chama o núcleo.
- Quando a IA devolve uma ação, a API consulta o núcleo antes de responder ao app. O núcleo diz o risco da ação, a confirmação exigida, o nível de autonomia da pessoa e a frase que a Lia fala para confirmar.
- O núcleo não usa IA, não guarda dados e não tem chave nenhuma.
- Se o núcleo estiver fora do ar, a Lia continua funcionando: a API segue sem o risco e a confirmação.

## Endereços

O núcleo sobe em `http://localhost:8080`.

| Método | Endereço | Para quê |
|---|---|---|
| `GET` | `/nucleo/saude` | Saber se o núcleo está no ar |
| `POST` | `/nucleo/decidir` | Decidir o risco e a confirmação de uma ação |
| `POST` | `/nucleo/pseudonimizar` | Esconder CPF, telefone e e-mail de um texto |

Os pedidos `POST` levam o cabeçalho `Content-Type: application/json`.

## POST /nucleo/decidir

Entrada:

```json
{
  "canal": "app",
  "acao": {"tipo": "avisar_familiar", "contato": "filho", "mensagem": "Cheguei bem"},
  "autonomia": {"avisar_familiar": 1}
}
```

| Campo | Obrigatório | O que é |
|---|---|---|
| `canal` | Sim | Por onde o pedido chegou: `"app"` ou `"ligacao"` |
| `acao` | Sim | A ação que a IA devolveu: o `tipo` e os campos dele (tabela abaixo) |
| `autonomia` | Não | O nível da pessoa em cada serviço. A chave é o `tipo` da ação e o valor é 0, 1 ou 2 |

Saída (status 200):

```json
{
  "risco": "MEDIO",
  "confirmacao": "SIM_OU_NAO",
  "nivel": 1,
  "fala_confirmacao": "Posso mandar para o seu filho: \"Cheguei bem\"?"
}
```

| Campo | Valores | O que é |
|---|---|---|
| `risco` | `BAIXO`, `MEDIO`, `ALTO` | O risco da ação |
| `confirmacao` | `NENHUMA`, `SIM_OU_NAO`, `DUPLA` | A confirmação que a Lia pede antes de executar |
| `nivel` | 0, 1, 2 | O nível de autonomia da pessoa nesse tipo de ação |
| `fala_confirmacao` | texto ou `null` | A frase que a Lia fala para confirmar. É `null` quando não precisa confirmar |

Regras:

- O risco e a confirmação vêm da classe da ação: consulta é `BAIXO` e `NENHUMA`; ação comum é `MEDIO` e `SIM_OU_NAO`; ação financeira é `ALTO` e `DUPLA`.
- O `nivel` é `autonomia[tipo]`. Se o tipo não estiver em `autonomia`, ou se `autonomia` não vier, o nível é 0.
- Os níveis são: 0, a Lia faz junto; 1, a Lia só confirma; 2, a Lia só acompanha.
- O nível nunca dispensa a confirmação de uma ação comum ou financeira.
- A Lia nunca paga sozinha. No caso de dinheiro, ela orienta e pede a confirmação dupla.

## Ações reconhecidas

| `tipo` | Campos obrigatórios | Classe | Risco | Confirmação |
|---|---|---|---|---|
| `consultar_beneficio` | nenhum | `AcaoConsulta` | `BAIXO` | `NENHUMA` |
| `guiar` | `servico` | `AcaoConsulta` | `BAIXO` | `NENHUMA` |
| `guiar_app` | `app` | `AcaoConsulta` | `BAIXO` | `NENHUMA` |
| `parar_guia` | nenhum | `AcaoConsulta` | `BAIXO` | `NENHUMA` |
| `tirar_foto` | nenhum | `AcaoConsulta` | `BAIXO` | `NENHUMA` |
| `avisar_familiar` | `contato`, `mensagem` | `AcaoComum` | `MEDIO` | `SIM_OU_NAO` |
| `agendar_por_voz` | `ubs`, `horario` | `AcaoComum` | `MEDIO` | `SIM_OU_NAO` |
| `pagar_boleto` | `valor`, `beneficiario` | `AcaoFinanceira` | `ALTO` | `DUPLA` |

- Campos a mais, como `objetivo` e `abrir` em `guiar_app` ou `conseguiu` em `parar_guia`, são aceitos e ignorados pelo núcleo.
- Em `pagar_boleto`, o `valor` vai como número (`150`) ou como texto com ponto (`"150.50"`), e precisa ser maior que zero.

Frases de confirmação:

| Ação | `fala_confirmacao` |
|---|---|
| `avisar_familiar` | `Posso mandar para o seu filho: "Cheguei bem"?` |
| `agendar_por_voz` | `Posso agendar na UBS Planalto às 14h?` |
| `pagar_boleto` | `Isso envolve dinheiro. Você quer pagar R$ 150,00 para Energisa? Vou te perguntar duas vezes antes de seguir.` |

## POST /nucleo/pseudonimizar

Troca os dados pessoais de um texto por marcas, antes de o texto ir para a IA.

Entrada:

```json
{"texto": "Meu CPF é 123.456.789-00"}
```

Saída (status 200):

```json
{"texto": "Meu CPF é [CPF]"}
```

As marcas são `[CPF]`, `[TELEFONE]` e `[EMAIL]`.

## GET /nucleo/saude

Saída (status 200):

```json
{"ok": true}
```

## Erros

| Status | Quando acontece | Corpo |
|---|---|---|
| 422 | O `tipo` da ação não está na tabela de ações reconhecidas | `{"erro": "acao desconhecida"}` |
| 400 | Faltou um campo obrigatório, o canal não existe ou um nível não é 0, 1 nem 2 | `{"erro": "<o motivo>"}` |

Exemplos de motivo no erro 400: `Falta o campo obrigatório: mensagem` e `Canal desconhecido: whatsapp`.

## Como a API da Lia chama o núcleo

Em `api-lia/main.py`, depois de `validar_acao(...)`:

1. Chamar `POST http://localhost:8080/nucleo/decidir` com prazo de 2 segundos.
2. Juntar `risco`, `confirmacao` e `fala_confirmacao` à resposta de `/conversa`.
3. Se o núcleo não responder ou devolver erro, seguir sem esses campos.

## Testar com curl

Com o núcleo no ar, no Git Bash:

```
curl http://localhost:8080/nucleo/saude

curl -X POST http://localhost:8080/nucleo/decidir -H "Content-Type: application/json" -d '{"canal":"app","acao":{"tipo":"avisar_familiar","contato":"filho","mensagem":"Cheguei bem"},"autonomia":{"avisar_familiar":1}}'

curl -X POST http://localhost:8080/nucleo/pseudonimizar -H "Content-Type: application/json" -d '{"texto":"Meu CPF: 123.456.789-00"}'

curl -i -X POST http://localhost:8080/nucleo/decidir -H "Content-Type: application/json" -d '{"canal":"app","acao":{"tipo":"dancar"}}'
```

O último mostra o erro 422.