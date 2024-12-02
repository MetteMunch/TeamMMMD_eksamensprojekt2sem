package mmmd.teammmmd_eksamensprojekt2sem.repository;

import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("testh2")
class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ConnectionManager connectionManager;

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void fetchInternalProjectCustomerEXISTSINDB() {
        //Arrange
        String intTitle = "Internal Project";
        String intRep = "Internal";
        Customer internal = new Customer(intTitle, intRep); //Simulerer, at internal project kunde allerede eksisterer i database

        //Act
        projectRepository.createCustomer(internal); //Internal tilføjes til databasen.
        Customer fetchInternal = projectRepository.fetchInternalProjectCustomer();

        //Assert
        assertNotNull(fetchInternal);
        assertEquals(intTitle, fetchInternal.getCompanyName());
        assertEquals(intRep, fetchInternal.getRepName());
    }
    @Test
    void fetchInternalProjectCustomerDOESNOTEXISTINDB() {
        //Arrange
        String intTitle = "Internal Project";
        String intRep = "Internal";

        //Act
        Customer fetchInternal = projectRepository.fetchInternalProjectCustomer();

        //Assert
        assertNotNull(fetchInternal);
        assertEquals(fetchInternal.getCompanyName(), intTitle);
        assertEquals(fetchInternal.getRepName(), intRep);
    }
}