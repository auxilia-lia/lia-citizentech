package br.com.fiap.lia.dominio;

import java.util.Objects;

/**
 * Pessoa de confiança que a Lia pode avisar, como um filho ou um neto.
 */

public class Familiar {

    private final Long idFamiliar;
    private final String apelido;
    private final Parentesco parentesco;
    private final boolean ajudaRemota;

    /**
     * Cria um familiar.
     *
     * @param idFamiliar identificador no banco, ou {@code null} se ainda não foi salvo
     * @param apelido como a pessoa chama o familiar, por exemplo {@code "Júnior"}
     * @param parentesco a relação com a pessoa
     * @param ajudaRemota {@code true} se o familiar aceita ajudar à distância
     * @throws IllegalArgumentException se o apelido estiver vazio
     * @throws NullPointerException se o parentesco for {@code null}
     */

    public Familiar(Long idFamiliar, String apelido, Parentesco parentesco, boolean ajudaRemota) {
        if (apelido == null || apelido.isBlank()) {
            throw new IllegalArgumentException("O apelido do familiar é obrigatório.");
        }
        this.idFamiliar = idFamiliar;
        this.apelido = apelido.strip();
        this.parentesco = Objects.requireNonNull(parentesco, "O parentesco é obrigatório.");
        this.ajudaRemota = ajudaRemota;
    }

    /**
     * Devolve o identificador do banco.
     *
     * @return o id, ou {@code null} se ainda não foi salvo
     */

    public Long getIdFamiliar() {
        return idFamiliar;
    }

    /**
     * Devolve como a pessoa chama o familiar.
     *
     * @return o apelido
     */

    public String getApelido() {
        return apelido;
    }

    /**
     * Devolve a relação com a pessoa.
     *
     * @return o parentesco
     */

    public Parentesco getParentesco() {
        return parentesco;
    }

    /**
     * Informa se o familiar aceita ajudar à distância.
     *
     * @return {@code true} se aceita
     */

    public boolean isAjudaRemota() {
        return ajudaRemota;
    }
}
