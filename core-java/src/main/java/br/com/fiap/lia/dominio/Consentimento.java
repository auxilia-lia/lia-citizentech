package br.com.fiap.lia.dominio;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Resposta da pessoa a um pedido de autorização de uso de dados (LGPD).
 *
 * <p>
 *     Uma resposta nunca muda: se a pessoa mudar de ideia, registra-se um novo consentimeto, e o mais recente é o que vale.
 * </p>
 *
 * @param idConsentimento identificador do banco, ou {@code null} se ainda não foi salvo
 * @param finalidade para que os dados serão usados
 * @param aceito {@code true} se a pessoa autorizou
 * @param versaoTermo versão do termo que a pessoa ouviu, por exemplo {@code "v1"}
 * @param dataHora quando a pessoa respondeu
 */

public record Consentimento(Long idConsentimento, Finalidade finalidade, boolean aceito, String versaoTermo, LocalDateTime dataHora) {

    /**
     * Valida os campos obrigatórios.
     *
     * @throws NullPointerException se a finalidade, a versão ou a data forem {@code null}
     */

    public Consentimento {
        Objects.requireNonNull(finalidade, "A finalidade é obrigatória.");
        Objects.requireNonNull(versaoTermo, "A versão do termo é obrigatória.");
        Objects.requireNonNull(dataHora, "A data e a hora são obrigatórias.");
    }
}
