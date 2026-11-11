package br.com.fiap.lia.dominio;

/**
 * Ação que muda algo na vida da pessoa, mas sem dinheiro, como avisar um
 * familiar ou agendar uma consulta. Tem risco médio e pede um "sim".
 */

public class AcaoComum extends Acao {

    private final String descricao;

    /**
     * Cria uma ação comum.
     *
     * @param tipo      identificador da ação, por exemplo {@code "avisar_familiar"}
     * @param descricao o que a Lia vai fazer, por exemplo {@code "mandar para o seu filho: \"Cheguei bem\""}
     * @throws IllegalArgumentException se o tipo ou a descrição estiverem vazios
     */
    public AcaoComum(String tipo, String descricao) {
        super(tipo);
        this.descricao = exigirTexto(descricao, "descrição");
    }

    /**
     * Uma ação comum tem sempre risco médio.
     *
     * @return {@link NivelDeRisco#MEDIO}
     */
    @Override
    public NivelDeRisco getNivelDeRisco() {
        return NivelDeRisco.MEDIO;
    }

    /**
     * Uma ação comum muda algo, então a Lia pede um "sim" antes.
     *
     * @return {@link TipoConfirmacao#SIM_OU_NAO}
     */
    @Override
    public TipoConfirmacao exigeConfirmacao() {
        return TipoConfirmacao.SIM_OU_NAO;
    }

    @Override
    public String descrever() {
        return descricao;
    }
}