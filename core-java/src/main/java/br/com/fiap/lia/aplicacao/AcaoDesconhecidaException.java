package br.com.fiap.lia.aplicacao;

/**
 * Lançada quando a IA devolve um tipo de ação que o núcleo não conhece.
 *
 * <p>
 *     Na API, vira a resposta HTTP 422 {@code {"erro": "acao desconhecida"}}.
 * </p>
 */

public class AcaoDesconhecidaException extends RuntimeException {
    /**
     * Cria a exceção com o tipo recebido.
     *
     * @param tipo o tipo que não foi reconhecido
     */
    public AcaoDesconhecidaException(String tipo) {
        super("Ação desconhecida: " + tipo);
    }
}
