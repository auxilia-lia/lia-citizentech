package br.com.fiap.lia.dominio;

/**
 * Relação entre um {@link Familiar} e a pessoa atendida pela Lia.
 */

public enum Parentesco {

    /** Filho ou filha */
    FILHO,
    /** Neto ou neta */
    NETO,
    /** Irmão ou irmã */
    IRMAO,
    /** Vizinho ou vizinha de confiança */
    VIZINHO,
    /** Qualquer outra pessoa de confiança */
    OUTRO
}
