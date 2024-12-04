//package mmmd.teammmmd_eksamensprojekt2sem.repository;
//
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//@ActiveProfiles("testh2") //Disse test laves op imod h2 in-memory
////databasen og ikke vores rigtige database. Når testen køres fortæller
////denne annotation, at oplysninger fra application-testh2.properties
////er gældende. Dvs der oprettes en h2 in memory database og køres den
////oplyste sql create/insert fil, så der er data i test databasen
//
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ConnectionManager connectionManager;
//
//    @BeforeEach
//    public void setUp() {
//    }
//
//    @AfterEach
//    public void tearDown() {
//    }
//
//    @Test
//    public void testDatabaseConnection() throws Exception {
//        Connection connection = connectionManager.getConnection(); //Her tester vi om der er forbindelse til H2 databasen
//        assertNotNull(connection, "Forbindelse til H2-testdatabasen burde ikke være null");
//    }
//
//    @Test
//    public void validateLoginPositiveResult() throws Exception {
//        boolean expectedResult = true;
//        boolean actualResult = userRepository.validateLogin("johnwa", "password123");
//        assertEquals(expectedResult, actualResult);
//    }
//
//    @Test
//    public void validateLoginNegativeResult() throws Exception {
//        boolean expectedResult = false;
//        boolean actualResult = userRepository.validateLogin("johnwa", "pass123");
//        assertEquals(expectedResult, actualResult);
//    }
//
//    @Test
//    public void getEmployeeIdFromDBWhereUsernameIsTrue() throws SQLException {
//        int expectedResult = 4;
//        int actualResult = userRepository.getEmployeeIdFromDB("jamesha");
//        assertEquals(expectedResult, actualResult);
//    }
//
//    @Test
//    public void getEmployeeIdFromDBWhereUsernameIsFalse() throws SQLException {
//        int expectedResult = 0;
//        int actualResult = userRepository.getEmployeeIdFromDB("jamesh");
//        assertEquals(expectedResult, actualResult);
//    }
//
//    @Test
//    public void getIsEmployeeManagerInfoFromDB() throws SQLException {
//        boolean expectedResult = true;
//        boolean actualResult = userRepository.getIsEmployeeManagerInfoFromDB(4);
//    }
//
//}
