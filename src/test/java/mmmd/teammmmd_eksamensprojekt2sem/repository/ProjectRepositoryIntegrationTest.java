package mmmd.teammmmd_eksamensprojekt2sem.repository;

import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("testh2")
public class ProjectRepositoryIntegrationTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ConnectionManager connectionManager;

    @BeforeEach
    public void resetH2BeforeEachTest() throws SQLException {

    }

    @Test
    public void testDatabaseConnection() throws Exception {
        Connection connection = connectionManager.getConnection(); //Her tester vi om der er forbindelse til H2 databasen
        assertNotNull(connection, "Forbindelse til H2-testdatabasen burde ikke være null");
    }
    @Test
    void getListOfCurrentCustomers() throws Exception {
        List<Customer> customersToReturn = projectRepository.getListOfCurrentCustomers();
        int expectedNumOfCustomers = 2; //2 customers i script fil
        int actualNumOfCustomers = customersToReturn.size();
        assertNotNull(customersToReturn);
        assertEquals(expectedNumOfCustomers, actualNumOfCustomers);
    }
    @Test
    void getListOfCurrentCustomersFail() throws Exception {
        List<Customer> customersToReturn = projectRepository.getListOfCurrentCustomers();
        int expectedNumOfCustomers = 3; //2 customers i script fil
        int actualNumOfCustomers = customersToReturn.size();
        assertNotNull(customersToReturn);
        assertNotEquals(expectedNumOfCustomers, actualNumOfCustomers);
    }
    @Test
    void createCustomer() throws Exception {
        //Arrange
        Customer insertCustomer = new Customer("Test Corp", "Test Rep");
        projectRepository.createCustomer(insertCustomer);

        //Act
        List<Customer> list = projectRepository.getListOfCurrentCustomers();
        int expectedSize = 3; // 2 customers i script fil. +1 efter insertion.
        int actualSize = list.size();

        //Assert
        assertEquals(expectedSize, actualSize);


    }
}
