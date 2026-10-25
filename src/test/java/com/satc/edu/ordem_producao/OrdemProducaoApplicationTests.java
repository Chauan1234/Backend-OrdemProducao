package com.satc.edu.ordem_producao;

import com.satc.edu.ordem_producao.machine.MachineService;
import com.satc.edu.ordem_producao.machine.dto.MachineRequestDTO;
import com.satc.edu.ordem_producao.product.ProductService;
import com.satc.edu.ordem_producao.product.dto.ProductRequestDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OrdemProducaoApplicationTests {

    @Autowired
    private ProductService productService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void productCreationReturnsDatabaseGeneratedCode() {
        var response = productService.create(new ProductRequestDTO("Produto de teste", null, true));

        assertNotNull(response.code());
    }

    @Test
    void machineCreationReturnsDatabaseGeneratedCode() {
        var response = machineService.create(new MachineRequestDTO("Máquina de teste"));

        assertNotNull(response.code());
    }

    @AfterAll
    void dropSpringTestSchema() {
        jdbcTemplate.execute("DROP SCHEMA IF EXISTS ordem_producao_spring_test CASCADE");
    }
}
