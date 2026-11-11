package br.com.fiap.lia.dominio;

/**
 * Algo que a Lia pode fazer pela pessoa, como consultar um benefício, avisar um familiar ou pagar um boleto.
 *
 * <p>
 *     É abstrata porque não existe ação "genérica": toda ação é uma consulta, uma ação comum ou uma ação financeira.
 *     Cada filha responde do seu jeito, qual é o seu risco e que confirmação exige (polimorfismo), e o resto do sistema
 *     trata apenas como {@code Acao}.
 * </p>
 */

public abstract class Acao {

    private final String tipo;

    /**
     * Cria a ação com o tipo usado pela IA.
     *
     * @param tipo identificador da ação no contrato, por exemplo {@code "avisar_familiar"}
     * @throws IllegalArgumentException se o tipo estiver vazio
     */
    protected Acao(String tipo) {
        this.tipo = exigirTexto(tipo, "tipo");
    }

    /**
     * Devolve o identificador da ação no contrato com a API da Lia.
     *
     * @return o tipo, por exemplo {@code "consultar_beneficio"}
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Informa o nível de risco desta ação.
     *
     * @return o nível de risco
     */
    public abstract NivelDeRisco getNivelDeRisco();

    /**
     * Informa a confirmação que esta ação exige antes de ser executada.
     *
     * @return o tipo de confirmação
     */
    public abstract TipoConfirmacao exigeConfirmacao();

    /**
     * Descreve a ação em linguagem simples, para a Lia falar com a pessoa.
     *
     * @return a descrição, por exemplo {@code "pagar R$ 150,00 para Energisa"}
     */
    public abstract String descrever();

    /**
     * Garante que um texto obrigatório foi preenchido.
     *
     * @param valor o texto recebido
     * @param campo o nome do campo, usado na mensagem de erro
     * @return o próprio texto, sem espaços nas pontas
     * @throws IllegalArgumentException se o texto estiver vazio
     */
    protected static String exigirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O campo " + campo + " é obrigatório.");
        }
        return valor.strip();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + tipo + ", risco " + getNivelDeRisco() + "]";
    }
}
