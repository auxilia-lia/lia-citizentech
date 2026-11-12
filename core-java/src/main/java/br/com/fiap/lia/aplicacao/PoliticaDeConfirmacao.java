package br.com.fiap.lia.aplicacao;

import br.com.fiap.lia.dominio.Acao;
import br.com.fiap.lia.dominio.TipoConfirmacao;

import java.util.Objects;

/**
 * Decide como a Lia confirma uma ação antes de executá-la.
 *
 * <p>Quem diz o tipo de confirmação é a própria ação ({@link Acao#exigeConfirmacao()}).
 * A política junta essa resposta com o nível de autonomia da pessoa e monta a
 * frase que a Lia vai falar. O nível de autonomia nunca dispensa a confirmação
 * de uma ação comum ou financeira.</p>
 */
public class PoliticaDeConfirmacao {

    /** Cria a política. Ela não guarda estado, então pode ser reaproveitada. */
    public PoliticaDeConfirmacao() {
    }

    /**
     * Analisa uma ação e devolve a decisão.
     *
     * @param acao a ação que a IA sugeriu
     * @param nivelDeAutonomia o nível da pessoa nesse serviço (0, 1 ou 2)
     * @return o risco, a confirmação exigida, o nível e a frase de confirmação
     * @throws NullPointerException se a ação for {@code null}
     */
    public Decisao decidir(Acao acao, int nivelDeAutonomia) {
        Objects.requireNonNull(acao, "A ação é obrigatória.");
        TipoConfirmacao confirmacao = acao.exigeConfirmacao();
        return new Decisao(acao.getNivelDeRisco(), confirmacao, nivelDeAutonomia, montarFala(acao, confirmacao));
    }

    private String montarFala(Acao acao, TipoConfirmacao confirmacao) {
        return switch (confirmacao) {
            case NENHUMA -> null;
            case SIM_OU_NAO -> "Posso " + acao.descrever() + "?";
            case DUPLA -> "Isso envolve dinheiro. Você quer " + acao.descrever()
                    + "? Vou te perguntar duas vezes antes de seguir.";
        };
    }
}