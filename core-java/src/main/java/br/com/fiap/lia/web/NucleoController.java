package br.com.fiap.lia.web;

import br.com.fiap.lia.aplicacao.Decisao;
import br.com.fiap.lia.aplicacao.FabricaDeAcoes;
import br.com.fiap.lia.aplicacao.GestorDeAutonomia;
import br.com.fiap.lia.aplicacao.PoliticaDeConfirmacao;
import br.com.fiap.lia.aplicacao.Pseudonimizador;
import br.com.fiap.lia.dominio.Acao;
import br.com.fiap.lia.dominio.Canal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Os três endereços do núcleo que a API da Lia, em Python, chama.
 *
 * <p>
 *     O controller não decide nada sozinho: ele só recebe o JSON, chama as ferramentas de {@code aplicacao} e devolve a resposta no formato do contrato.
 * </p>
 */

@RestController
@RequestMapping("/nucleo")
public class NucleoController {
    private final FabricaDeAcoes fabrica;
    private final PoliticaDeConfirmacao politica;
    private final Pseudonimizador pseudonimizador;

    /**
     * Recebe as ferramentas do núcleo. Quem preenche os três é o Spring, com os beans da {@code NucleoConfig}.
     *
     * @param fabrica fábrica de ações
     * @param politica política de confirmação
     * @param pseudonimizador pseudonimizador do CPF, telefone e e-mail
     */

    public NucleoController(FabricaDeAcoes fabrica, PoliticaDeConfirmacao politica, Pseudonimizador pseudonimizador) {
        this.fabrica = fabrica;
        this.politica = politica;
        this.pseudonimizador = pseudonimizador;
    }

    /**
     * Responde se o núcleo está no ar.
     *
     * @return {@code {"ok": true}}
     */

    @GetMapping("/saude")
    public Map<String, Boolean> saude() {
        return Map.of("ok", true);
    }

    /**
     * Esconde CPF, telefone e e-mail de um texto.
     *
     * @param pedido o texto original
     * @return o texto com {@code [CPF]}, {@code [TELEFONE]} e {@code [EMAIL]} no lugar dos dados
     */

    @PostMapping("/pseudonimizar")
    public TextoDto pseudonimizar(@RequestBody TextoDto pedido) {
        return new TextoDto(pseudonimizador.pseudonimizar(pedido.texto()));
    }

    /**
     * Decide o risco e a confirmação da ação que a IA devolveu.
     *
     * @param pedido o canal, a ação e os níveis de autonomia da pessoa
     * @return o risco, a confirmação, o nível e a frase de confirmação
     */

    @PostMapping("/decidir")
    public DecidirResponse decidir(@RequestBody DecidirRequest pedido) {
        Canal.deCodigo(pedido.canal());
        GestorDeAutonomia gestor = new GestorDeAutonomia(pedido.autonomia());
        Acao acao = fabrica.criar(pedido.acao());
        Decisao decisao = politica.decidir(acao, gestor.nivelDe(acao.getTipo()));
        return DecidirResponse.de(decisao);
    }
}
