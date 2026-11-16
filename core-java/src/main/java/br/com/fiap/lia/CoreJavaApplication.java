package br.com.fiap.lia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada do servidor: sobe o Spring Boot com os endereços do núcleo na porta 8080.
 *
 * <p>
 *     Para ver os 4 casos sem servidor, roda a classe {@link Main}.
 * </p>
 */

@SpringBootApplication
public class CoreJavaApplication {

	/** Cria a aplicação. Quem chama é o próprio Spring. */
	public CoreJavaApplication() {
	}

	/**
	 * Sobe o servidor.
	 *
	 * @param args argumentos da linha de comando, repassados ao Spring
	 */

	public static void main(String[] args) {
		SpringApplication.run(CoreJavaApplication.class, args);
	}
}
