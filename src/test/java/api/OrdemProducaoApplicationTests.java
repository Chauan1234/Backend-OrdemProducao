package api;

import api.dto.machine.MachineRequestDTO;
import api.service.MachineService;
import api.dto.product.ProductRequestDTO;
import api.service.ProductService;
import api.dto.productionorder.ProductionOrderSummaryDTO;
import api.model.ProductionOrderStatus;
import api.service.ProductionOrderService;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private ProductionOrderService productionOrderService;

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

    @Test
    void productionOrderSearchWorksWithoutFiltersAndSortsNewestFirst() {
        var fixtures = createProductionOrderFixtures();

        var page = productionOrderService.findAll(null, null, null, null,
                PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt")));
        var ids = page.map(ProductionOrderSummaryDTO::id).getContent();

        assertTrue(ids.contains(fixtures.newerOrderId()));
        assertTrue(ids.contains(fixtures.olderOrderId()));
        assertTrue(ids.indexOf(fixtures.newerOrderId()) < ids.indexOf(fixtures.olderOrderId()));
    }

    @Test
    void productionOrderSearchFiltersByStatus() {
        var fixtures = createProductionOrderFixtures();

        var page = productionOrderService.findAll(ProductionOrderStatus.PENDING, null, null, null,
                PageRequest.of(0, 20));

        assertContainsOrder(page, fixtures.newerOrderId());
        assertDoesNotContainOrder(page, fixtures.olderOrderId());
    }

    @Test
    void productionOrderSearchFiltersByProduct() {
        var fixtures = createProductionOrderFixtures();

        var page = productionOrderService.findAll(null, fixtures.newerProductId(), null, null,
                PageRequest.of(0, 20));

        assertContainsOrder(page, fixtures.newerOrderId());
        assertDoesNotContainOrder(page, fixtures.olderOrderId());
    }

    @Test
    void productionOrderSearchFiltersByMachine() {
        var fixtures = createProductionOrderFixtures();

        var page = productionOrderService.findAll(null, null, fixtures.newerMachineId(), null,
                PageRequest.of(0, 20));

        assertContainsOrder(page, fixtures.newerOrderId());
        assertDoesNotContainOrder(page, fixtures.olderOrderId());
    }

    @Test
    void productionOrderSearchFiltersByDeadline() {
        var fixtures = createProductionOrderFixtures();

        var page = productionOrderService.findAll(null, null, null, fixtures.newerDeadline(),
                PageRequest.of(0, 20));

        assertContainsOrder(page, fixtures.newerOrderId());
        assertDoesNotContainOrder(page, fixtures.olderOrderId());
    }

    @Test
    void productionOrderSearchCombinesAllFilters() {
        var fixtures = createProductionOrderFixtures();

        var page = productionOrderService.findAll(ProductionOrderStatus.PENDING, fixtures.newerProductId(),
                fixtures.newerMachineId(), fixtures.newerDeadline(), PageRequest.of(0, 20));

        assertContainsOrder(page, fixtures.newerOrderId());
        assertDoesNotContainOrder(page, fixtures.olderOrderId());
    }

    @Test
    void productionOrderSearchPaginatesFixturesInCreatedAtOrder() {
        var fixtures = createProductionOrderFixtures();

        var firstPage = productionOrderService.findAll(null, null, null, null,
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt")));
        var secondPage = productionOrderService.findAll(null, null, null, null,
                PageRequest.of(1, 1, Sort.by(Sort.Direction.DESC, "createdAt")));

        assertEquals(fixtures.newerOrderId(), firstPage.getContent().getFirst().id());
        assertEquals(fixtures.olderOrderId(), secondPage.getContent().getFirst().id());
    }

    private ProductionOrderFixtures createProductionOrderFixtures() {
        var newerProductId = UUID.randomUUID();
        var olderProductId = UUID.randomUUID();
        var newerMachineId = UUID.randomUUID();
        var olderMachineId = UUID.randomUUID();
        var newerOrderId = UUID.randomUUID();
        var olderOrderId = UUID.randomUUID();
        var newerDeadline = LocalDate.now().plusDays(100);
        var olderDeadline = LocalDate.now().plusDays(101);

        insertProduct(newerProductId, "Fixture produto novo " + newerProductId);
        insertProduct(olderProductId, "Fixture produto antigo " + olderProductId);
        insertMachine(newerMachineId, "Fixture máquina nova " + newerMachineId);
        insertMachine(olderMachineId, "Fixture máquina antiga " + olderMachineId);
        insertOrder(newerOrderId, newerProductId, newerMachineId, ProductionOrderStatus.PENDING,
                newerDeadline, LocalDateTime.now().plusDays(20));
        insertOrder(olderOrderId, olderProductId, olderMachineId, ProductionOrderStatus.IN_PROGRESS,
                olderDeadline, LocalDateTime.now().plusDays(19));

        return new ProductionOrderFixtures(newerProductId, newerMachineId, newerOrderId, olderOrderId, newerDeadline);
    }

    private void insertProduct(UUID id, String name) {
        jdbcTemplate.update("INSERT INTO tb_product (id, name, description, active) VALUES (?, ?, ?, TRUE)",
                id, name, "Fixture de teste");
    }

    private void insertMachine(UUID id, String name) {
        jdbcTemplate.update("INSERT INTO tb_machine (id, name, status) VALUES (?, ?, 'AVAILABLE')", id, name);
    }

    private void insertOrder(UUID id, UUID productId, UUID machineId, ProductionOrderStatus status,
                             LocalDate deadline, LocalDateTime createdAt) {
        jdbcTemplate.update("""
                INSERT INTO tb_production_order
                    (id, product_id, machine_id, target_quantity, produced_quantity, status, created_at, deadline)
                VALUES (?, ?, ?, 10, 0, ?::production_order_status, ?, ?)
                """, id, productId, machineId, status.name(), Timestamp.valueOf(createdAt), deadline);
    }

    private void assertContainsOrder(Page<ProductionOrderSummaryDTO> page, UUID id) {
        assertTrue(page.getContent().stream().anyMatch(order -> order.id().equals(id)));
    }

    private void assertDoesNotContainOrder(Page<ProductionOrderSummaryDTO> page, UUID id) {
        assertFalse(page.getContent().stream().anyMatch(order -> order.id().equals(id)));
    }

    private record ProductionOrderFixtures(UUID newerProductId, UUID newerMachineId,
                                           UUID newerOrderId, UUID olderOrderId,
                                           LocalDate newerDeadline) {
    }

    @AfterAll
    void dropSpringTestSchema() {
        jdbcTemplate.execute("DROP SCHEMA IF EXISTS ordem_producao_spring_test CASCADE");
    }
}
