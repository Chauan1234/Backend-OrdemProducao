package api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// CAMADA: Config
// RESPONSABILIDADE: configurar os metadados da documentação OpenAPI.
// REGRAS: a documentação descreve a API de ordem de produção sem alterar suas rotas.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ordemProducaoOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Ordem de Produção - Backend")
                .description("API REST para gerenciamento de produtos, máquinas, ordens e apontamentos de produção."));
    }
}
