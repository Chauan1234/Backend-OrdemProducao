// CAMADA: Application
// RESPONSABILIDADE: inicializar a aplicação Spring Boot.
// REGRAS: carrega a configuração e os componentes da aplicação.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
