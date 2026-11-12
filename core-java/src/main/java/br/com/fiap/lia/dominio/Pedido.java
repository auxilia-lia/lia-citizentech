package br.com.fiap.lia.dominio;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Um pedido que a pessoa fez para a Lia, por voz ou por texto.
 */

public class Pedido {

    private final String texto;
    private final Canal canal;
    private final LocalDateTime dataHora;
    private Acao acao;

    /**
     * Cria um pedido recebido agora.
     *
     * @param texto o que a pessoa disse
     * @param canal por onde o pedido chegou
     * @throws IllegalArgumentException se o texto estiver vazio
     * @throws NullPointerException se o canal for {@code null}
     */

    public Pedido(String texto, Canal canal) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("O texto do pedido é obrigatório.");
        }
        this.texto = texto.strip();
        this.canal = Objects.requireNonNull(canal, "O canal é obrigatório.");
        this.dataHora = LocalDateTime.now();
    }

    /**
     * Liga o pedido à ação que a IA entendeu.
     *
     * @param acao a ação
     * @throws NullPointerException se a ação for {@code null}
     */

    public void associarAcao(Acao acao) {
        this.acao = Objects.requireNonNull(acao, "A ação é obrigatória.");
    }

    /**
     * Informa se a Lia já entendeu o que fazer.
     *
     * @return {@code true} se o pedido tem uma ação
     */

    public boolean temAcao() {
        return acao != null;
    }

    /**
     * Devolve a ação do pedido, se tiver.
     *
     * @return a ação, ou um {@link Optional} vazio quando o pedido é ambíguo
     */

    public Optional<Acao> getAcao() {
        return Optional.ofNullable(acao);
    }

    /**
     * Devolve o texto do pedido.
     *
     * @return o texto
     */

    public String getTexto() {
        return texto;
    }

    /**
     * Devolve o canal do pedido.
     *
     * @return o canal
     */

    public Canal getCanal() {
        return canal;
    }

    /**
     * Devolve quando o pedido foi feito.
     *
     * @return a data do pedido
     */

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
