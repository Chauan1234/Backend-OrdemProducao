package api.migration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UuidMigrationTest {

    private static final String SCHEMA = "ordem_producao_upgrade_test";
    private static final String DB_HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String DB_PORT = System.getenv().getOrDefault("DB_PORT", "5434");
    private static final String DB_NAME = System.getenv().getOrDefault("DB_NAME", "ordem_producao_test");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "postgres");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "postgres");
    private static final String JDBC_URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    private static int productCodeBefore;
    private static int productIdBefore;
    private static int machineIdBefore;

    @BeforeAll
    static void migrateLegacySchemaAndSeedVerificationRows() throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute("DROP SCHEMA IF EXISTS " + SCHEMA + " CASCADE");
            statement.execute("CREATE SCHEMA " + SCHEMA);
        }

        flyway(MigrationVersion.fromVersion("2")).migrate();

        try (Connection connection = schemaConnection(); Statement statement = connection.createStatement()) {
            try (ResultSet result = statement.executeQuery("""
                    INSERT INTO tb_product (name, description, active)
                    VALUES ('upgrade-check-product', 'preserve this product', TRUE)
                    RETURNING id, code
                    """)) {
                result.next();
                productIdBefore = result.getInt("id");
                productCodeBefore = result.getInt("code");
            }
            try (ResultSet result = statement.executeQuery("""
                    INSERT INTO tb_machine (name, status)
                    VALUES ('upgrade-check-machine', 'AVAILABLE')
                    RETURNING id
                    """)) {
                result.next();
                machineIdBefore = result.getInt("id");
            }
            try (ResultSet result = statement.executeQuery("""
                    INSERT INTO tb_production_order (product_id, machine_id, target_quantity)
                    VALUES (%d, %d, 7)
                    RETURNING id
                    """.formatted(productIdBefore, machineIdBefore))) {
                result.next();
                int orderId = result.getInt("id");
                statement.executeUpdate("INSERT INTO tb_production_record (production_order_id, quantity, notes) "
                        + "VALUES (" + orderId + ", 3, 'preserve this record')");
            }
        }
    }

    @AfterAll
    static void dropUpgradeTestSchema() throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute("DROP SCHEMA IF EXISTS " + SCHEMA + " CASCADE");
        }
    }

    @Test
    void upgradesExistingRowsAndPreservesNamesDefaultsAndCodeSequences() throws SQLException {
        flyway(MigrationVersion.fromVersion("3")).migrate();

        try (Connection connection = schemaConnection(); Statement statement = connection.createStatement()) {
            assertEquals("uuid", columnType(statement, "tb_product", "id"));
            assertEquals("uuid", columnType(statement, "tb_machine", "id"));
            assertEquals("uuid", columnType(statement, "tb_production_order", "id"));
            assertEquals("uuid", columnType(statement, "tb_production_order", "product_id"));
            assertEquals("uuid", columnType(statement, "tb_production_order", "machine_id"));
            assertEquals("uuid", columnType(statement, "tb_production_record", "id"));
            assertEquals("uuid", columnType(statement, "tb_production_record", "production_order_id"));

            var expectedConstraints = Set.of(
                    "tb_product_pkey", "tb_machine_pkey", "tb_production_order_pkey", "tb_production_record_pkey",
                    "tb_production_order_product_id_fkey", "tb_production_order_machine_id_fkey",
                    "tb_production_record_production_order_id_fkey");
            assertTrue(constraints(statement).containsAll(expectedConstraints));
            assertTrue(indexes(statement).containsAll(Set.of(
                    "idx_tb_production_order_product_id", "idx_tb_production_order_machine_id",
                    "idx_tb_production_order_status", "idx_tb_production_record_order_id")));

            for (var table : Set.of("tb_product", "tb_machine", "tb_production_order", "tb_production_record")) {
                assertTrue(columnDefault(statement, table, "id").contains("gen_random_uuid"));
            }

            try (ResultSet result = statement.executeQuery("""
                    SELECT p.code, p.name, m.name, o.target_quantity, r.quantity, r.notes
                    FROM tb_product p
                    JOIN tb_production_order o ON o.product_id = p.id
                    JOIN tb_machine m ON m.id = o.machine_id
                    JOIN tb_production_record r ON r.production_order_id = o.id
                    WHERE p.name = 'upgrade-check-product'
                    """)) {
                assertTrue(result.next());
                assertEquals(productCodeBefore, result.getInt("code"));
                assertEquals("upgrade-check-product", result.getString("name"));
                assertEquals("upgrade-check-machine", result.getString(3));
                assertEquals(7, result.getInt("target_quantity"));
                assertEquals(3, result.getInt("quantity"));
                assertEquals("preserve this record", result.getString("notes"));
                assertFalse(result.next());
            }

            UUID productId = insertUuid(statement, """
                    INSERT INTO tb_product (name, active) VALUES ('post-upgrade-product', TRUE) RETURNING id
                    """);
            int newCode;
            try (ResultSet result = statement.executeQuery(
                    "SELECT code FROM tb_product WHERE id = '" + productId + "'")) {
                result.next();
                newCode = result.getInt("code");
            }
            assertTrue(newCode > productCodeBefore);

            UUID machineId = insertUuid(statement, """
                    INSERT INTO tb_machine (name, status) VALUES ('post-upgrade-machine', 'AVAILABLE') RETURNING id
                    """);
            UUID orderId = insertUuid(statement, """
                    INSERT INTO tb_production_order (product_id, machine_id, target_quantity)
                    VALUES ('%s', '%s', 1) RETURNING id
                    """.formatted(productId, machineId));
            UUID recordId = insertUuid(statement, """
                    INSERT INTO tb_production_record (production_order_id, quantity)
                    VALUES ('%s', 1) RETURNING id
                    """.formatted(orderId));
            assertNotNull(productId);
            assertNotNull(machineId);
            assertNotNull(orderId);
            assertNotNull(recordId);
        }
    }

    private static Flyway flyway(MigrationVersion target) {
        return Flyway.configure()
                .dataSource(JDBC_URL, DB_USER, DB_PASSWORD)
                .locations("classpath:db/migration")
                .schemas(SCHEMA)
                .defaultSchema(SCHEMA)
                .createSchemas(true)
                .target(target)
                .load();
    }

    private static Connection schemaConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL + "?currentSchema=" + SCHEMA, DB_USER, DB_PASSWORD);
    }

    private static String columnType(Statement statement, String table, String column) throws SQLException {
        try (ResultSet result = statement.executeQuery("SELECT data_type FROM information_schema.columns "
                + "WHERE table_schema = '" + SCHEMA + "' AND table_name = '" + table
                + "' AND column_name = '" + column + "'")) {
            assertTrue(result.next());
            return result.getString(1);
        }
    }

    private static String columnDefault(Statement statement, String table, String column) throws SQLException {
        try (ResultSet result = statement.executeQuery("SELECT column_default FROM information_schema.columns "
                + "WHERE table_schema = '" + SCHEMA + "' AND table_name = '" + table
                + "' AND column_name = '" + column + "'")) {
            assertTrue(result.next());
            return result.getString(1);
        }
    }

    private static Set<String> constraints(Statement statement) throws SQLException {
        var names = new HashSet<String>();
        try (ResultSet result = statement.executeQuery("SELECT conname FROM pg_constraint "
                + "WHERE connamespace = to_regnamespace('" + SCHEMA + "')")) {
            while (result.next()) {
                names.add(result.getString(1));
            }
        }
        return names;
    }

    private static Set<String> indexes(Statement statement) throws SQLException {
        var names = new HashSet<String>();
        try (ResultSet result = statement.executeQuery("SELECT indexname FROM pg_indexes WHERE schemaname = '"
                + SCHEMA + "'")) {
            while (result.next()) {
                names.add(result.getString(1));
            }
        }
        return names;
    }

    private static UUID insertUuid(Statement statement, String sql) throws SQLException {
        try (ResultSet result = statement.executeQuery(sql)) {
            assertTrue(result.next());
            return result.getObject(1, UUID.class);
        }
    }
}
