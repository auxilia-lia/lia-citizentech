package br.com.fiap.lia.dominio;

/**
 * Ação que só mostra ou informa algo, como consultar o benefício do INSS ou guiar a pessoa na tela.
 * Tem risco baixo e não pede confirmação.
 */

public class AcaoConsulta extends Acao {

    private final String descricao;

    /**
     * Cria uma ação de consulta.
     *
     * @param tipo      identificador da ação, por exemplo {@code "consultar_beneficio"}
     * @param descricao o que a Lia vai fazer, por exemplo {@code "consultar o seu benefício do INSS"}
     * @throws IllegalArgumentException se o tipo ou a descrição estiverem vazios
     */
    public AcaoConsulta(String tipo, String descricao) {
        super(tipo);
        this.descricao = exigirTexto(descricao, "descrição");
    }

    /**
     * Uma consulta tem sempre risco baixo.
     *
     * @return {@link NivelDeRisco#BAIXO}
     */
    @Override
    public NivelDeRisco getNivelDeRisco() {
        return NivelDeRisco.BAIXO;
    }

    /**
     * Uma consulta não muda nada, então não precisa de confirmação.
     *
     * @return {@link TipoConfirmacao#NENHUMA}
     */
    @Override
    public TipoConfirmacao exigeConfirmacao() {
        return TipoConfirmacao.NENHUMA;
    }

    @Override
    public String descrever() {
        return descricao;
    }
}