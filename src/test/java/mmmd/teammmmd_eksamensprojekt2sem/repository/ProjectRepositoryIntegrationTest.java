package mmmd.teammmmd_eksamensprojekt2sem.repository;

import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Status;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
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
    public void setUp() throws SQLException {
        Connection connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("RUNSCRIPT FROM 'classpath:h2createinsert.sql'");
    }

    @Test
    public void testDatabaseConnection() throws Exception {
        Connection connection = connectionManager.getConnection(); //Her tester vi om der er forbindelse til H2 databasen
        assertNotNull(connection, "Forbindelse til H2-testdatabasen burde ikke være null");
    }
    /*
    #####################################
    #           CRUD Customer           #
    #####################################
     */
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
        int expectedNumOfCustomers = 3; //2 customers i script fil. Smider 3 ind på expect for at testen skal fejle. Hvis testen passer, er alt godt.
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
    @Test
    void lookUpCustomerIDFromDB() {
        //Arrange
        Customer customer = new Customer("TechCorp", "Emily White"); //Eksisterer i forvejen i H2.

        //Act
        int expectedCustomerID = 1; //Customer er den første, der bliver indsat i h2, må derfor være nr. 1.
        int actualcustomerId = projectRepository.lookUpCustomerIDFromDB(customer);

        //Assert
        assertEquals(expectedCustomerID, actualcustomerId);
    }
    @Test
    void lookUpCustomerIDFromDBFail() {
        //Arrange
        Customer customer = new Customer("TechCorp", "Emily White"); //Eksisterer i forvejen i H2.

        //Act
        int expectedCustomerID = 2; //Customer er den første, der bliver indsat i h2, må derfor være nr. 1. Sætter 2 for at faile testen.
        int actualcustomerId = projectRepository.lookUpCustomerIDFromDB(customer);

        //Assert
        assertNotEquals(expectedCustomerID, actualcustomerId);
    }

    /*
    #####################################
    #           CRUD Project            #
    #####################################
     */
    @Test
    void createProject(){
        //Arrange
        Project newProject = new Project("Project Title Test", "Project Description", 1, Date.valueOf("2024-12-12"), Date.valueOf("2025-01-01"), "Link", 2, 1 );
        int expectedNumOfProjects = 3; //2 eksisterende projekter. +1 for newProject.

        //Act
        projectRepository.createProject(newProject); //Liste skulle gerne vokse med +1 efter createProject(), da projektet tilføjes til databasen.
        List<Project> list = projectRepository.showAllProjects();
        int actualNumOfProjects = list.size();

        //Assert
        assertEquals(expectedNumOfProjects, actualNumOfProjects);
    }
    @Test
    void showAllProjects() {
        //Arrange
        List<Project> list = projectRepository.showAllProjects();
        int expectedNumOfProjects = 2; //Eksisterer 2 projekter i h2 sql insert fil.

        //Act
        int actualNumOfProjects = list.size();

        //Assert
        assertEquals(expectedNumOfProjects, actualNumOfProjects);
    }
    @Test
    void updateProject() {
        //Arrange
        Project newProject = new Project("Project Title Test", "Project Description",
                1, Date.valueOf("2024-12-12"), Date.valueOf("2025-01-01"), "Link",
                2, 1 ); //Vi laver et helt nyt projekt for at bruge vores createMetode.
        //Act
        projectRepository.createProject(newProject); //Tilføjer det til databasen.
        projectRepository.setProjectID(newProject); //Bemærk, ikke en almindelige setter metode. Laver et lookup i DB for at finde autogenererede ID.

        Project updateProject = projectRepository.fetchSpecificProject("Project Title Test"); //Vi henter vores projekt i DB efter det er oprettet.
        updateProject.setProjectDescription("Updated Description"); //Vi opdaterer beskrivelsen.
        projectRepository.updateProject(updateProject); //Vores update tager et Project objekt, så når vi har opdateret de ønskede attributter sender vi det nye og opdaterede objekt til DB.

        Project newProjectUpdated = projectRepository.fetchSpecificProject("Project Title Test"); //Vi finder det opdaterede projekt frem ved at søge på dets titel.

        //Assert
        assertEquals(newProject.getID(), updateProject.getID()); //Tjek af samme ID.
        assertNotEquals(newProject.getProjectDescription(), updateProject.getProjectDescription());
        assertEquals("Updated Description", newProjectUpdated.getProjectDescription()); //Vi tjekker om opdatering er gået igennem.
    }
    @Test
    void deleteProject() throws Exception {
        //Arrange
        Project newProject = new Project("Project Title Test", "Project Description",
                1, Date.valueOf("2024-12-12"), Date.valueOf("2025-01-01"), "Link",
                2, 1 );

        //Act
        projectRepository.createProject(newProject);
        projectRepository.setProjectID(newProject);
        List<Project> list = projectRepository.showAllProjects();
        int actualSizeAfterInsertion = list.size();
        int expectedSizeAfterInsertion = 2+1; //2 projekter eksisterer allerede i h2. +1 efter insertion.
            assertEquals(expectedSizeAfterInsertion, actualSizeAfterInsertion);
        projectRepository.deleteProject(newProject);
        List<Project> listDeletion = projectRepository.showAllProjects();
        int actualSizeAfterDeletion = listDeletion.size();
        int expectedSizeAfterDeletion = 3-1; //3 projekter før deletion. -1 efter deletion.

        //Assert
        assertEquals(actualSizeAfterDeletion, expectedSizeAfterDeletion);
    }
    @Test
    void fetchSpecificProject() {
        //Arrange
        Project newProject = new Project("Project Title Test", "Project Description",
                1, Date.valueOf("2024-12-12"), Date.valueOf("2025-01-01"), "Link",
                2, 1 );

        //Act
        projectRepository.createProject(newProject);
        Project fetchProject = projectRepository.fetchSpecificProject(newProject.getProjectTitle());
        //Assert
        assertNotNull(fetchProject);
        assertEquals("Project Title Test", fetchProject.getProjectTitle());
        assertEquals("Project Description", fetchProject.getProjectDescription());
    }
    @Test
    void checkIfProjectNameAlreadyExists() {
        //Arrange
        Project newProject = new Project("Project Title Test", "Project Description",
                1, Date.valueOf("2024-12-12"), Date.valueOf("2025-01-01"), "Link",
                2, 1 );
        //Act
        projectRepository.createProject(newProject);
        projectRepository.setProjectID(newProject);

        //Assert
        assertTrue(projectRepository.checkIfProjectNameAlreadyExists(newProject.getProjectTitle()));
    }
    @Test
    void checkIfProjectNameAlreadyExistsFail() {
        //Arrange
        Project newProject = new Project("Project Title Test", "Project Description",
                1, Date.valueOf("2024-12-12"), Date.valueOf("2025-01-01"), "Link",
                2, 1 );
        //Act
        projectRepository.createProject(newProject);
        projectRepository.setProjectID(newProject);

        //Assert
        assertFalse(projectRepository.checkIfProjectNameAlreadyExists(newProject.getProjectTitle()+" 2")); //Indsætter +2 for: Project Title Test 2
    }
    @Test
    void setProjectID() {
        //Arrange
        Project newProject = new Project("Project Title Test", "Project Description",
                1, Date.valueOf("2024-12-12"), Date.valueOf("2025-01-01"), "Link",
                2, 1 ); //Default constructor ID=-1;

        //Act
        projectRepository.createProject(newProject);
        projectRepository.setProjectID(newProject); //Vi laver lookup i DB og retter til autogenererede ID.

        //Assert
        assertNotEquals(-1, newProject.getID());
        assertEquals(3, newProject.getID()); //Vi forventer ID=3, da der i forvejen eksisterer to projekter.
    }
    @Test
    void fetchAllStatus() {
        //Arrange
        List<Status> list;
        int expectedSize = 4; //Der eksisterer 4 status i DB.

        //Act
        list = projectRepository.fetchAllStatus();


        //Assert
        assertEquals(expectedSize, list.size());
    }
    @Test
    void findPMEmployees() {
        //Arrange
        List<Employee> listPM;

        //Act
        int expectedSize = 2; //2 PM oprettes i h2.
        listPM = projectRepository.findPMEmployees();

        //Assert
        assertEquals(expectedSize, listPM.size());
    }
    @Test
    void findBCEmployees() {
        //Arrange
        List<Employee> listBC;

        //Act
        int expectedSize = 2; //2 Business Consultants eksisterer i h2
        listBC = projectRepository.findBCEmployees();
        //Assert
        assertEquals(expectedSize, listBC.size());
    }


}
