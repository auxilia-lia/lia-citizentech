package br.com.fiap.lia.web;

import br.com.fiap.lia.aplicacao.Decisao;
import br.com.fiap.lia.dominio.NivelDeRisco;
import br.com.fiap.lia.dominio.TipoConfirmacao;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resposta de {@code POST /nucleo/decidir}, no formato do contrato.
 *
 * <p>
 *     Exemplo: {@code {"risco": "MEDIO", "confirmacao": "SIM_OU_NAO", "nivel": 1, "fala_confirmacao": "Posso mandar para seu filho: \"Cheguei bem\"?"}}.
 * </p>
 *
 * @param risco nível de risco da ação
 * @param confirmacao confirmação exigida antes de executar
 * @param nivel nível de autonomia da pessoa nesse serviço
 * @param falaConfirmacao frase que a Lia fala para confirmar, ou {@code null} quando não precisa
 */

public record DecidirResponse(NivelDeRisco risco, TipoConfirmacao confirmacao, int nivel, @JsonProperty("fala_confirmacao") String falaConfirmacao) {
    /**
     * Converte a decisão do núcleo para o formato da resposta.
     *
     * @param decisao a decisão tomada pela política de confirmação
     * @return a resposta pronta para virar JSON
     */

    public static DecidirResponse de(Decisao decisao) {
        return new DecidirResponse(decisao.risco(), decisao.confirmacao(), decisao.nivel(), decisao.falaConfirmacao());
    }

}
