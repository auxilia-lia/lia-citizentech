package br.com.fiap.lia.aplicacao;

import br.com.fiap.lia.dominio.Acao;
import br.com.fiap.lia.dominio.AcaoComum;
import br.com.fiap.lia.dominio.AcaoConsulta;
import br.com.fiap.lia.dominio.AcaoFinanceira;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Transforma a ação que a IA devolveu, em formato de dados, num objeto {@link Acao}.
 *
 * <p>
 *     A IA responde com algo como {@code {"tipo": "avisar_familiar", "contato": "filho", "mensagem": "Cheguei bem"}}.
 *     A fábrica lê o {@code tipo}, escolhe a classe certa e preenche a descrição com os outros campos.
 * </p>
 */

public class FabricaDeAcoes {
    /** Cria a fábrica. Ela não guarda estado, então pode ser reaproveitada. */
    public FabricaDeAcoes() {
    }

    /**
     * Cria a ação correspondente aos dados da IA.
     *
     * @param dados o tipo da ação e os seus campos
     * @return a ação pronta para a política de confirmação
     * @throws AcaoDesconhecidaException se o tipo não estiver no contrato
     * @throws IllegalArgumentException se faltar algum campo obrigatório
     */

    public Acao criar(Map<String, Object> dados) {
        String tipo = campo(dados, "tipo");
        return switch (tipo) {
            case "consultar_beneficio" -> new AcaoConsulta(tipo, "consultar o seu benefício do INSS");
            case "guiar" ->  new AcaoConsulta(tipo, "te guiar até " + campo(dados, "servico"));
            case "guiar_app" -> new AcaoConsulta(tipo, "te guiar no " + campo(dados, "app"));
            case "parar_guia" -> new AcaoConsulta(tipo, "parar guia");
            case "tirar_foto" -> new AcaoConsulta(tipo, "abrir a câmera para ler o papel");
            case "avisar_familiar" -> new AcaoComum(tipo, "mandar para " + comArtigo(campo(dados, "contato")) + ": \"" + campo(dados, "mensagem") + "\"");
            case "agendar_por_voz" -> new AcaoComum(tipo, "agendar na " + campo(dados, "ubs") + " às " + campo(dados, "horario"));
            case "pagar_boleto" -> new AcaoFinanceira(tipo, new BigDecimal(campo(dados, "valor")), campo(dados, "beneficiario"));
            default -> throw new AcaoDesconhecidaException(tipo);
        };
    }

    /** Lê um campo obrigatório como texto. Números, como o valor, também viram texto. */
    private String campo(Map<String, Object> dados, String nome) {
        Object valor = dados == null ? null : dados.get(nome);
        if (valor == null || String.valueOf(valor).isBlank()) {
            throw new IllegalArgumentException("Falta o campo obrigatório: " + nome);
        }
        return String.valueOf(valor).strip();
    }

    /** "filho" vira "o seu filho"; "filha" vira "a sua filha". */
    private String comArtigo(String contato) {
        return contato.endsWith("a") ? "a sua " + contato : "o seu " + contato;
    }
}
