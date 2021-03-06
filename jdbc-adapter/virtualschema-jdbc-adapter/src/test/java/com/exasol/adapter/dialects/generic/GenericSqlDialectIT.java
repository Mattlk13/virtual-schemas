package com.exasol.adapter.dialects.generic;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assume;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import com.exasol.adapter.dialects.AbstractIntegrationTest;
import com.exasol.adapter.dialects.IntegrationTestConfigurationCondition;

@Tag("integration")
@ExtendWith(IntegrationTestConfigurationCondition.class)
class GenericSqlDialectIT extends AbstractIntegrationTest {
    private static final boolean IS_LOCAL = false;

    @BeforeAll
    static void beforeAll() throws FileNotFoundException, SQLException, ClassNotFoundException {
        Assume.assumeTrue(getConfig().genericTestsRequested());

        final String connectionString = getConfig().getGenericJdbcConnectionString();
        setConnection(connectToExa());
        createGenericJDBCAdapter();
        final String catalogName = "jm3450"; // This only works for the database in our test environment
        final String schemaName = "";
        createVirtualSchema("VS_GENERIC_MYSQL", GenericSqlDialect.getPublicName(), catalogName, schemaName, "",
                getConfig().getGenericUser(), getConfig().getGenericPassword(), "ADAPTER.JDBC_ADAPTER",
                connectionString, IS_LOCAL, getConfig().debugAddress(), "", null, "");
    }

    @Test
    void testVirtualSchema() throws SQLException, ClassNotFoundException, FileNotFoundException {
        final ResultSet result = executeQuery("SELECT * FROM \"customers\" ORDER BY id");
        result.next();
        assertEquals("1", result.getString(1));
    }

    private static void createGenericJDBCAdapter() throws SQLException, FileNotFoundException {
        final String jdbcAdapterPath = getConfig().getJdbcAdapterPath();
        final String jdbcDriverDriver = getConfig().getGenericJdbcDriverPath();
        final List<String> includes = new ArrayList<>();
        includes.add(jdbcAdapterPath);
        includes.add(jdbcDriverDriver);
        createJDBCAdapter(includes);
    }
}
