package br.com.fiap.lia.web;

import br.com.fiap.lia.aplicacao.AcaoDesconhecidaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Transforma os erros do núcleo em respostas HTTP claras, no formato do contrato.
 *
 * <p>
 *     Sem esta classe, qualquer exceção vira um erro 500 genérico. Com ela, a API da Lia sabe exatamente o que deu errado.
 * </p>
 */

@RestControllerAdvice
public class TratadorDeErros {

    /** Cria o tratador. Quem chama é o próprio Spring, ao subir o servidor. */
    public TratadorDeErros() {
    }

    /**
     * A IA devolveu um tipo de ação que não está no contrato.
     *
     * @param erro a exceção lançada pela fábrica de ações
     * @return status 422 com {@code {"erro": "acao desconhecida"}}
     */

    @ExceptionHandler(AcaoDesconhecidaException.class)
    public ResponseEntity<Map<String, String>> acaoDesconhecida(AcaoDesconhecidaException erro) {
        return ResponseEntity.status(422).body(Map.of("erro", "acao desconhecida"));
    }

    /**
     * O pedido veio com algum dado faltando ou errado.
     *
     * @param erro a exceção com a explicação do problema
     * @return status 400 com {@code {"erro": "<a explicação>"}}
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> pedidoInvalido(IllegalArgumentException erro) {
        return ResponseEntity.badRequest().body(Map.of("erro", erro.getMessage()));
    }
}
