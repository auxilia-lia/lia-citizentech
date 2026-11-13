package br.com.fiap.lia.dominio;

/**
 * Para que a pessoa autoriza o uso dos seus dados (LGPD).
 */

public enum Finalidade {

    /** Mandar o pedido, já sem dados pessoais, para a IA na nuvem */
    USO_IA_NUVEM,
    /** Guardar o histórico de pedidos */
    GUARDAR_HISTORICO,
    /** Avisar um familiar quando a Lia precisar */
    AVISAR_FAMILIAR
}
