package br.com.fiap.lia.web;

import java.util.Map;

/**
 * Corpo do pedido que a API da Lia manda para {@code POST /nucleo/decidir}.
 *
 * <p>
 *     Exemplo: {@code {"canal": "app", "acao": {"tipo": "avisar_familiar", "contato": "filho", "mensagem": "Cheguei bem"}, "autonomia": {"avisar_familiar": 1}}}.
 * </p>
 *
 * @param canal por onde o pedido chegou: {@code "app"} ou {@code "ligacao"}
 * @param acao a ação que a IA devolveu, com o {@code tipo} e os seus campos
 * @param autonomia o nível da pessoa em cada serviço (0, 1 ou 2)
 */

public record DecidirRequest(String canal, Map<String, Object> acao, Map<String, Integer> autonomia) {
}
