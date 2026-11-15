package br.com.fiap.lia.config;

import br.com.fiap.lia.aplicacao.FabricaDeAcoes;
import br.com.fiap.lia.aplicacao.PoliticaDeConfirmacao;
import br.com.fiap.lia.aplicacao.Pseudonimizador;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Diz ao Spring quais ferramentas do núcleo ele deve criar e entregar a quem precisar.
 *
 * <p>
 *     As classes de {@code dominio} e {@code aplicacao} continuam em Java puro, sem nenhuma anotação do Spring: é só aqui que o Spring fica sabendo delas.
 * </p>
 */

@Configuration
public class NucleoConfig {
    /** Cria a configuração. Quem chama é o próprio Spring, ao subir o servidor. */

    public NucleoConfig() {
    }

    /**
     * Fábrica que transforma a resposta da IA em uma ação
     *
     * @return a fábrica, criada uma vez só
     */

    @Bean
    public FabricaDeAcoes fabricaDeAcoes() {
        return new FabricaDeAcoes();
    }

    /**
     * Política que decide a confirmação de cada ação.
     *
     * @return a política, criada uma vez só
     */

    @Bean
    public PoliticaDeConfirmacao politicaDeConfirmacao() {
        return new PoliticaDeConfirmacao();
    }

    /**
     * Pseudonimizador que esconde CPF, telefone e e-mail.
     *
     * @return o pseudonimizador, criado uma vez só
     */

    @Bean
    public Pseudonimizador pseudonimizador() {
        return new Pseudonimizador();
    }
}
