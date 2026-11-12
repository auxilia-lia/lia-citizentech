package br.com.fiap.lia.aplicacao;

import br.com.fiap.lia.dominio.NivelDeRisco;
import br.com.fiap.lia.dominio.TipoConfirmacao;

/**
 * O que a Lia precisa fazer antes de executar uma ação.
 *
 * <p>É a resposta do núcleo para a API da Lia: os campos têm o mesmo sentido
 * do contrato REST ({@code risco}, {@code confirmacao}, {@code nivel} e
 * {@code fala_confirmacao}).</p>
 *
 * @param risco           nível de risco da ação
 * @param confirmacao     confirmação exigida antes de executar
 * @param nivel           nível de autonomia da pessoa nesse serviço: 0 faz junto, 1 só confirma, 2 só acompanha
 * @param falaConfirmacao frase que a Lia fala para pedir a confirmação, ou {@code null} quando não precisa
 */
public record Decisao(NivelDeRisco risco, TipoConfirmacao confirmacao, int nivel, String falaConfirmacao) {
}