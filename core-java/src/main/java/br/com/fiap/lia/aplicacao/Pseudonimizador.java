package br.com.fiap.lia.aplicacao;

import java.util.regex.Pattern;

/**
 * Troca dados pessoais por marcas antes de qualquer texto ir para a IA.
 *
 * <p>
 *     E-mail vira {@code [EMAIL]}, CPF vira {@code [CPF]} e telefone vira {@code [TELEFONE]}.
 *     Assim a IA entende o pedido sem receber o dado real (LGPD).
 * </p>
 * <p>
 *     Limitação conhecida: um número de 11 digitos sem pontuação e sem palavra "CPF" por perto é tratado como telefone.
 *     Mesmo assim ele é escondido.
 * </p>
 */

public class Pseudonimizador {

    private static final Pattern EMAIL = Pattern.compile("[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+");

    private static final Pattern CPF_FORMATADO = Pattern.compile("(?<!\\d)\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}(?!\\d)");

    private static final Pattern CPF_SEM_PONTOS = Pattern.compile("(?i)(cpf\\D{0,10}?)(?<!\\d)\\d{11}(?!\\d)");

    private static final Pattern TELEFONE = Pattern.compile("(?<!\\d)(\\+55\\s?)?(\\(?\\d{2}\\)?\\s?)?9?\\d{4}[-\\s]?\\d{4}(?!\\d)");

    /** Cria o pseudonimizador. Ele não guarda estado, então pode ser reaproveitado. */

    public Pseudonimizador() {
    }

    /**
     * Esconde e-mails, CPFs, telefones de um texto.
     *
     * @param texto o texto original, por exemplo {@code "Meu CPF é 123.456.789-00"}
     * @return o texto com as marcas, por exemplo {@code "Meu CPF é [CPF]"};
     *         {@code null} ou vazio voltam como vieram
     */

    public String pseudonimizar(String texto) {
        if (texto == null || texto.isBlank()) {
            return texto;
        }
        String resultado = EMAIL.matcher(texto).replaceAll("[EMAIL]");
        resultado = CPF_FORMATADO.matcher(resultado).replaceAll("[CPF]");
        resultado = CPF_SEM_PONTOS.matcher(resultado).replaceAll("$1[CPF]");
        resultado = TELEFONE.matcher(resultado).replaceAll("[TELEFONE]");
        return resultado;
    }
}
