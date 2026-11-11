package br.com.fiap.lia.dominio;

/**
 * Confirmação que a Lia pede antes de executar uma {@link Acao}.
 *
 * <p>
 *     Os nomes são os mesmos do contrato REST com a API da Lia.
 * </p>
 */

public enum TipoConfirmacao {

    /** Executa sem perguntar. Só vale para ações que apenas informam. */
    NENHUMA,

    /** Pede um "sim" antes de executar. */
    SIM_OU_NAO,

    /** Pede duas confirmações e repete o valor envolvido. */
    DUPLA,
}
