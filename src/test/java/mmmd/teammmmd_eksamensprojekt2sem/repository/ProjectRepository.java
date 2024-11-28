package mmmd.teammmmd_eksamensprojekt2sem.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("testh2")
public class ProjectRepository {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ConnectionManager connectionManager;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testDatabaseConnection() throws Exception {
        Connection connection = connectionManager.getConnection(); //Her tester vi om der er forbindelse til H2 databasen
        assertNotNull(connection, "Forbindelse til H2-testdatabasen burde ikke være null");
    }
}
