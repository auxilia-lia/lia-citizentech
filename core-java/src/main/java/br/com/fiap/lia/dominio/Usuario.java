package br.com.fiap.lia.dominio;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Pessoa atendida pela Lia. É a raiz do agregado: os familiares e os consentimentos só são alterados por meio dela.
 *
 * <p>
 *     CPF e telefone não ficam aqui de propósito: a Lia não guarda esses dados.
 * </p>
 */

public class Usuario {
    private final Long idUsuario;
    private final String nomePreferido;
    private final int anoNascimento;
    private final LocalDate dataCadastro;
    private VelocidadeFala velocidadeFala;
    private final List<Familiar> familiares = new ArrayList<>();
    private final List<Consentimento> consentimentos = new ArrayList<>();

    /**
     * Cadastra uma pessoa hoje, com fala em velocidade normal.
     *
     * @param idUsuario identificador no banco, ou {@code null} se ainda não foi salvo
     * @param nomePreferido como a pessoa gosta de ser chamada, por exemplo {@code "Dona Maria"}
     * @param anoNascimento ano em que a pessoa nasceu
     * @throws IllegalArgumentException se o nome estiver vazio ou o ano for impossível
     */

    public Usuario(Long idUsuario, String nomePreferido, int anoNascimento) {
        if (nomePreferido == null || nomePreferido.isBlank()) {
            throw new IllegalArgumentException("O nome preferido é obrigatório.");
        }
        if (anoNascimento < 1900 || anoNascimento > Year.now().getValue()) {
            throw new IllegalArgumentException("Ano de nascimento inválido: " + anoNascimento);
        }
        this.idUsuario = idUsuario;
        this.nomePreferido = nomePreferido.strip();
        this.anoNascimento = anoNascimento;
        this.dataCadastro = LocalDate.now();
        this.velocidadeFala = VelocidadeFala.NORMAL;
    }

    /**
     * Adiciona uma pessoa de confiança.
     *
     * @param familiar o familiar
     * @throws NullPointerException se o familiar for {@code null}
     */

    public void adicionarFamiliar(Familiar familiar) {
        familiares.add(Objects.requireNonNull(familiar, "O familiar é obrigatório."));
    }

    /**
     * Guarda uma resposta de consentimento.
     *
     * @param consentimento a resposta da pessoa
     * @throws NullPointerException se o consentimento for {@code null}
     */

    public void registrarConsentimento(Consentimento consentimento) {
        consentimentos.add(Objects.requireNonNull(consentimento, "O consentimento é obrigatório."));
    }

    /**
     * Informa se a pessoa autorizou a finalidade. Vale a resposta mais recente, sem nenhuma resposta, a autorização não existe.
     *
     * @param finalidade a finalidade consultada
     * @return {@code true} se a última resposta para essa finalidade foi "sim"
     */

    public boolean aceitou(Finalidade finalidade) {
        boolean resposta = false;
        for (Consentimento consentimento : consentimentos) {
            if (consentimento.finalidade() == finalidade) {
                resposta = consentimento.aceito();
            }
        }
        return resposta;
    }

    /**
     * Calcula a idade aproximada, pelo ano de nascimento.
     *
     * @return a idade em anos
     */

    public int getIdade() {
        return Year.now().getValue() - anoNascimento;
    }

    /**
     * Muda a velocidade que a Lia fala com a pessoa.
     *
     * @param velocidadeFala a nova velocidade
     * @throws NullPointerException se a velocidade for {@code null}
     */

    public void setVelocidadeFala(VelocidadeFala velocidadeFala) {
        this.velocidadeFala = Objects.requireNonNull(velocidadeFala, "A velocidade é obrigatória.");
    }

    /**
     * Devolve o identificador no banco.
     *
     * @return o id, ou {@code null} se ainda não foi salvo
     */

    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Devolve como a pessoa gosta de ser chamada.
     *
     * @return o nome preferido
     */

    public String getNomePreferido() {
        return nomePreferido;
    }

    /**
     * Devolve o ano de nascimento.
     *
     * @return o ano
     */

    public int getAnoNascimento() {
        return anoNascimento;
    }

    /**
     * Devolve o dia do cadastro.
     *
     * @return a data
     */

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    /**
     * Devolve a velocidade de fala.
     *
     * @return a velocidade
     */

    public VelocidadeFala getVelocidadeFala() {
        return velocidadeFala;
    }

    /**
     * Devolve os familiares, em uma lista que não pode ser alterada por fora.
     *
     * @return os familiares
     */

    public List<Familiar> getFamiliares() {
        return List.copyOf(familiares);
    }

    /**
     * Devolve os consentimentos, em uma lista que não pode ser alterada por fora.
     *
     * @return os consentimentos, do mais antigo para o mais recente
     */

    public List<Consentimento> getConsentimentos() {
        return List.copyOf(consentimentos);
    }
}
