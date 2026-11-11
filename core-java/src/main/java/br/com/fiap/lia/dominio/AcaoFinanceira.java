package br.com.fiap.lia.dominio;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Ação que envolve dinheiro, como pagar um boleto. Tem risco alto e pede
 * confirmação dupla, repetindo o valor.
 *
 * <p>A Lia nunca paga sozinha: esta ação serve para orientar a pessoa e
 * garantir que ela confirme duas vezes antes de qualquer pagamento.</p>
 */

public class AcaoFinanceira extends Acao {

    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");

    private final BigDecimal valor;
    private final String beneficiario;

    /**
     * Cria uma ação financeira.
     *
     * @param tipo         identificador da ação, por exemplo {@code "pagar_boleto"}
     * @param valor        valor em reais; precisa ser maior que zero
     * @param beneficiario quem recebe o dinheiro, por exemplo {@code "Energisa"}
     * @throws IllegalArgumentException se o valor não for positivo ou algum texto estiver vazio
     */
    public AcaoFinanceira(String tipo, BigDecimal valor, String beneficiario) {
        super(tipo);
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor precisa ser maior que zero.");
        }
        this.valor = valor;
        this.beneficiario = exigirTexto(beneficiario, "beneficiário");
    }

    /**
     * Devolve o valor da ação.
     *
     * @return o valor em reais
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * Devolve quem recebe o dinheiro.
     *
     * @return o beneficiário
     */
    public String getBeneficiario() {
        return beneficiario;
    }

    /**
     * Formata o valor em reais, do jeito que a Lia fala.
     *
     * @return o valor formatado, por exemplo {@code "R$ 150,00"}
     */
    public String getValorFormatado() {
        // O Java separa "R$" e o número com um espaço especial (U+00A0); trocamos por um espaço comum.
        return NumberFormat.getCurrencyInstance(BRASIL).format(valor).replace('\u00A0', ' ');
    }

    /**
     * Uma ação com dinheiro tem sempre risco alto.
     *
     * @return {@link NivelDeRisco#ALTO}
     */
    @Override
    public NivelDeRisco getNivelDeRisco() {
        return NivelDeRisco.ALTO;
    }

    /**
     * Dinheiro exige confirmação dupla, repetindo o valor.
     *
     * @return {@link TipoConfirmacao#DUPLA}
     */
    @Override
    public TipoConfirmacao exigeConfirmacao() {
        return TipoConfirmacao.DUPLA;
    }

    @Override
    public String descrever() {
        return "pagar " + getValorFormatado() + " para " + beneficiario;
    }
}