package br.com.fiap.lia.aplicacao;

import java.util.HashMap;
import java.util.Map;

/**
 * Guarda o nível de autonomia da pessoa em cada serviço e faz esse nível subir quando ela consegue concluir a tarefa sozinha.
 *
 * <p>
 *     É a autonomia progressiva da Lia: primeiro ela faz junto, depois só confirma, depois só acompanha.
 * </p>
 */

public class GestorDeAutonomia {

    /** Nível 0: a Lia faz junto com a pessoa. */
    public static final int FAZ_JUNTO = 0;

    /** Nível 1: a pessoa faz e a Lia só confirma. */
    public static final int SO_CONFIRMA = 1;

    /** Nível 2: a pessoa faz sozinha e a Lia só acompanha. */
    public static final int SO_ACOMPANHA = 2;

    private final Map<String, Integer> niveis = new HashMap<>();

    /** Cria um gestor vazio: todo serviço começa em {@link #FAZ_JUNTO}. */
    public GestorDeAutonomia() {
    }

    /** Cria um gestor a partir dos níveis que a pessoa já tem.
     *
     * @param niveisIniciais nível por serviço, por exemplo {@code {"avisar_familiar": 1}}
     * @throws IllegalArgumentException se algum nível estiver fora de 0 a 2
     */

    public GestorDeAutonomia(Map<String, Integer> niveisIniciais) {
        if (niveisIniciais != null) {
            niveisIniciais.forEach(this::definirNivel);
        }
    }

    /**
     * Consulta o nível da pessoa num serviço.
     *
     * @param servico o tipo de ação, por exemplo {@code "avisar_familiar"}
     * @return o nível (0, 1 ou 2); {@link #FAZ_JUNTO} se o serviço ainda não foi usado
     */
    public int nivelDe(String servico) {
        return niveis.getOrDefault(servico, FAZ_JUNTO);
    }

    /**
     * Registra que a pessoa terminou uma tarefa. Se ela concluiu sozinha, o nível sobe um degrau, até o máximo de {@link #SO_ACOMPANHA}.
     *
     * @param servico o tipo de ação
     * @param concluiuSozinha {@code true} se a pessoa terminou sem ajuda
     * @return o nível depois do registro
     */

    public int registrarConclusao(String servico, boolean concluiuSozinha) {
        int atual = nivelDe(servico);
        if (concluiuSozinha && atual < SO_ACOMPANHA) {
            definirNivel(servico, atual + 1);
        }
        return nivelDe(servico);
    }

    /**
     * Devolve uma cópia dos níveis que não pode ser alterada por fora.
     *
     * @return o nível de cada serviço
     */

    public Map<String, Integer> getNiveis() {
        return Map.copyOf(niveis);
    }

    private void definirNivel(String servico, Integer nivel) {
        if (servico == null || servico.isBlank()) {
            throw new IllegalArgumentException("O serviço é obrigatório.");
        }
        if (nivel == null || nivel < FAZ_JUNTO || nivel > SO_ACOMPANHA) {
            throw new IllegalArgumentException("O nível de " + servico + " precisa ser 0, 1 ou 2.");
        }
        niveis.put(servico, nivel);
    }
}
