package br.com.fiap.lia.web;

/**
 * Um texto solto, usado na entrada e na saída de {@code POST /nucleo/pseudonimizar}.
 *
 * <p>
 *     Exemplo: {@code {"texto": "Meu CPF é [CPF]"}}.
 * </p>
 *
 * @param texto o texto
 */

public record TextoDto(String texto) {
}
