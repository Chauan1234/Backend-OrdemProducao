package api;

import api.controller.ApiExceptionHandler;
import api.service.ProdutoNaoEncontradoException;
import api.service.RegraDeNegocioException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void validationReturnsStructuredFieldsAndLegacyMessage() throws Exception {
        mockMvc.perform(post("/test/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name: must not be blank"))
                .andExpect(jsonPath("$.campos[0].campo").value("name"))
                .andExpect(jsonPath("$.campos[0].mensagem").value("must not be blank"));
    }

    @Test
    void businessConflictKeepsStatusAndOmitsFields() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("regra violada"))
                .andExpect(jsonPath("$.campos").doesNotExist());
    }

    @Test
    void notFoundKeepsLegacyMessage() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto não encontrado: código 7"))
                .andExpect(jsonPath("$.campos").doesNotExist());
    }

    @Test
    void typeMismatchReturnsBadRequestWithoutFields() throws Exception {
        mockMvc.perform(get("/test/uuid/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos").doesNotExist());
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @PostMapping("/body")
        void body(@Valid @RequestBody TestBody body) {
        }

        @GetMapping("/conflict")
        void conflict() {
            throw new RegraDeNegocioException("regra violada");
        }

        @GetMapping("/not-found")
        void notFound() {
            throw new ProdutoNaoEncontradoException("código 7");
        }

        @GetMapping("/uuid/{id}")
        void uuid(@PathVariable java.util.UUID id) {
        }
    }

    record TestBody(@NotBlank String name) {
    }
}
