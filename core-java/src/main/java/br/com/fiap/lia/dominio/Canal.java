package br.com.fiap.lia.dominio;

/**
 * Por onde o pedido chegou até a Lia.
 */

public enum Canal {

    /** Pelo aplicativo no celular. */
    APP("app"),

    /** Por uma ligação comum. */
    LIGACAO("ligacao");

    private final String codigo;

    Canal(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Devolve o código usado no JSON do contrato.
     *
     * @return {@code "app"} ou {@code "ligacao"}
     */

    public String getCodigo() {
        return codigo;
    }

    /**
     * Converte o código do JSON no canal correspondente.
     *
     * @param codigo {@code "app"} ou {@code "ligacao"}, sem diferenciar maiúscula
     * @return o canal
     * @throws IllegalArgumentException se o código não for de nenhum canal
     */

    public static Canal deCodigo(String codigo) {
        for (Canal canal : values()) {
            if (canal.codigo.equalsIgnoreCase(codigo)) {
                return canal;
            }
        }
        throw new IllegalArgumentException("Canal desconhecido: " + codigo);
    }
}
