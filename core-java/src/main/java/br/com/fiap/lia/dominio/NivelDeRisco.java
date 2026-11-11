package br.com.fiap.lia.dominio;

/**
 * Nível de risco de uma {@link Acao} que a Lia pode executar.
 *
 * <p>
 *     Quanto maior o risco, mais forte é a confirmação que a Lia pede antes de agir.
 * </p>
 */

public enum NivelDeRisco {

    /** Só mostra ou informa algo, sem mudar nada. */
    BAIXO("só informa, não muda nada"),

    /** Muda algo na vida da pessoa, mas sem envolver dinheiro. */
    MEDIO("muda algo, mas sem dinheiro"),

    /** Envolve dinheiro. */
    ALTO("envolve dinheiro");

    private final String descricao;

    NivelDeRisco(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Explica o nível em linguagem simples.
     *
     * @return a descrição do nível de risco
     */
    public String getDescricao() {
        return descricao;
    }
}
