package br.com.fiap.lia;

import br.com.fiap.lia.aplicacao.*;
import br.com.fiap.lia.dominio.Acao;
import br.com.fiap.lia.dominio.Canal;
import br.com.fiap.lia.dominio.Pedido;
import br.com.fiap.lia.dominio.Consentimento;
import br.com.fiap.lia.dominio.Familiar;
import br.com.fiap.lia.dominio.Finalidade;
import br.com.fiap.lia.dominio.Parentesco;
import br.com.fiap.lia.dominio.Usuario;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Demonstração do núcleo da Lia no console, sem servidor e sem IA.
 *
 * <p>
 *     Roda os 4 casos do contrato: consulta, ação comum, dinheiro e pedido ambíguo. A resposta da IA é simulada com um
 *     {@link Map}, no mesmo formato que a IA devolve de verdade.
 * </p>
 */

public class Main {
    private static final FabricaDeAcoes FABRICA = new FabricaDeAcoes();
    private static final PoliticaDeConfirmacao POLITICA = new PoliticaDeConfirmacao();
    private static final Pseudonimizador PSEUDONIMIZADOR = new Pseudonimizador();
    private static final GestorDeAutonomia AUTONOMIA = new GestorDeAutonomia();
    private static Usuario usuario;

    private Main() {
    }

    /**
     * Roda os 4 casos e os extras de autonomia, pseudonimização e consentimento.
     *
     * @param args não usado
     */

    public static void main(String[] args) {
        System.out.println("=== Núcleo da Lia: 4 casos, sem servidor e sem IA ===");

        usuario = new Usuario(1L, "Dona Maria", 1952);
        usuario.adicionarFamiliar(new Familiar(1L, "Júnior", Parentesco.FILHO, true));
        usuario.registrarConsentimento(new Consentimento(1L, Finalidade.USO_IA_NUVEM, true, "v1", LocalDateTime.now()));
        System.out.println("Atendendo: " + usuario.getNomePreferido() + ", " + usuario.getIdade() + " anos" +
                " | familiar de confiança: " + usuario.getFamiliares().get(0).getApelido() + " | autorizou o uso de IA: " + usuario.aceitou(Finalidade.USO_IA_NUVEM));

        System.out.println("\n--- Caso 1: consulta ---");
        atender("quero ver meu dinheiro do INSS", Canal.LIGACAO, Map.of("tipo", "consultar_beneficio"));

        System.out.println("\n--- Caso 2: ação comum ---");
        atender("avisa meu filho que cheguei", Canal.APP, Map.of("tipo", "avisar_familiar", "contato",
                "filho", "mensagem", "Cheguei bem"));

        System.out.println("\n--- Extra: a pessoa mandou a mensagem sozinha ---");
        int nivel = AUTONOMIA.registrarConclusao("avisar_familiar", true);
        System.out.println("Autonomia em avisar_familiar subiu para " + nivel + " (só confirma)");
        atender("avisa meu filho que cheguei", Canal.APP, Map.of("tipo", "avisar_familiar", "contato",
                "filho", "mensagem", "Cheguei bem"));

        System.out.println("\n--- Caso 3: dinheiro ---");
        atender("paga esse boleto de 150 reais", Canal.APP, Map.of("tipo", "pagar_boleto","valor", 150,
                "beneficiario", "Energisa"));

        System.out.println("\n--- Caso 4: pedido ambíguo ---");
        atender("manda aquilo pra ela", Canal.APP, null);

        System.out.println("\n--- Extra: CPF escondido antes de ir para a IA ---");
        atender("meu cpf é 123.456.789-00, vê meu benefício", Canal.LIGACAO, Map.of("tipo", "consultar_beneficio"));

        System.out.println("\n--- Extra: Dona Maria retira a autorização (LGPD) ---");
        usuario.registrarConsentimento(new Consentimento(2L, Finalidade.USO_IA_NUVEM, false, "v1", LocalDateTime.now()));
        atender("quero ver meu dinheiro do INSS", Canal.LIGACAO, Map.of("tipo", "consultar_beneficio"));
    }

    /**
     * Atende um pedido do começo ao fim: esconde dados pessoais, transforma a resposta da IA numa ação e decide a confirmação.
     *
     * @param texto o que a pessoa disse
     * @param canal por onde o pedido chegou
     * @param respostaDaIa a ação que a IA devolveu, ou {@code null} se ela não entendeu
     */

    private static void atender(String texto, Canal canal, Map<String, Object> respostaDaIa) {
        System.out.println("Pessoa (" + canal.getCodigo() + "): " + texto);

        // Sem autorização nada vai para a IA
        if (!usuario.aceitou(Finalidade.USO_IA_NUVEM)) {
            System.out.println("Lia: Para entender o seu pedido, eu preciso da sua autorização" + " para usar a inteligência artificial. Você autoriza?");
            return;
        }

        // Para dados pessoais não irem para a IA
        String textoSeguro = PSEUDONIMIZADOR.pseudonimizar(texto);
        if (!textoSeguro.equals(texto)) {
            System.out.println("Vai para a IA: " + textoSeguro);
        }
        Pedido pedido = new Pedido(textoSeguro, canal);

        // Resposta da IA vira uma ação
        if (respostaDaIa != null) {
            pedido.associarAcao(FABRICA.criar(respostaDaIa));
        }

        // Quando não tiver ação a Lia pergunta ao invés de adivinhar
        if (!pedido.temAcao()) {
            System.out.println("Lia: Não entendi direito. O que você quer mandar, e para quem?");
            return;
        }

        // A política decide a confirmação, seja qual for o tipo de ação (polimorfismo)
        Acao acao = pedido.getAcao().get();
        Decisao decisao = POLITICA.decidir(acao, AUTONOMIA.nivelDe(acao.getTipo()));

        // Resultado
        System.out.println("Ação: " + acao.getClass().getSimpleName() + " (" + acao.getTipo() + ")");
        System.out.println("Risco: " + decisao.risco() + ", " + decisao.risco().getDescricao());
        System.out.println("Confirmação: " + decisao.confirmacao() + " | nível de autonomia: " + decisao.nivel());
        if (decisao.falaConfirmacao() == null) {
            System.out.println("Lia: Certo, vou " + acao.descrever() + ".");
        } else {
            System.out.println("Lia: " + decisao.falaConfirmacao());
        }
    }
}
