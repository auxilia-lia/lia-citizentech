package br.com.fiap.lia.dominio;

import java.time.LocalDateTime;

/**
 * Histórico de uma pessoa num serviço: em que nível de autonomia ela está e quantas vezes seguidas concluiu sem ajuda.
 *
 * <p>
 *     No banco, é a tabela que liga {@code Usuario} e {@code Servico}.
 * </p>
 */

public class Progresso {

    private final String servico;
    private int nivelAutonomia;
    private int vezesSemAjuda;
    private LocalDateTime atualizadoEm;

    /**
     * Cria o progresso de um serviço que a pessoa ainda não usou.
     *
     * @param servico o tipo de ação, por exemplo {@code "avisar_familiar"}
     * @throws IllegalArgumentException se o serviço estiver vazio
     */

    public Progresso(String servico) {
        if (servico == null || servico.isBlank()) {
            throw new IllegalArgumentException("O serviço é obrigatório.");
        }
        this.servico = servico.strip();
        this.nivelAutonomia = 0;
        this.vezesSemAjuda = 0;
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Registra o fim de uma tarefa. Sem ajuda, a sequência aumenta; com ajuda, ela volta a zero.
     *
     * @param semAjuda {@code true} se a pessoa concluiu sozinha
     */

    public void registrarConclusao(boolean semAjuda) {
        if (semAjuda) {
            vezesSemAjuda =+ 1;
        } else {
            vezesSemAjuda = 0;
        }
        atualizadoEm = LocalDateTime.now();
    }

    /**
     * Muda o nível de autonomia nesse serviço.
     *
     * @param nivel 0 (faz junto), 1 (só confirma) ou 2 (só acompanha)
     * @throws IllegalArgumentException se o nível estiver fora de 0 a 2
     */

    public void atualizarNivel(int nivel) {
        if (nivel < 0 || nivel > 2) {
            throw new IllegalArgumentException("O nível precisa ser 0, 1 ou 2.");
        }
        this.nivelAutonomia = nivel;
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Devolve o serviço deste progresso.
     *
     * @return o tipo de ação
     */

    public String getServico() {
        return servico;
    }

    /**
     * Devolve o nível de autonomia atual.
     *
     * @return 0, 1 ou 2
     */

    public int getNivelAutonomia() {
        return nivelAutonomia;
    }

    /**
     * Devolve quantas vezes seguidas a pessoas concluiu sem ajuda.
     *
     * @return a sequência atual
     */

    public int getVezesSemAjuda() {
        return vezesSemAjuda;
    }

    /**
     * Devolve quando o progresso mudou pela última vez.
     *
     * @return a data e a hora
     */

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
