package mmmd.teammmmd_eksamensprojekt2sem.service;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks //med denne annotation instruerer vi Spring Boot i at oprette en Mock-version af UserService klassen, og
    //automatisk injicere dens afhængigheder og parametre, som annoteret med @Mock. Vi kan så manipulere med denne Mock
    //af klassen under testen
    private UserService userService;

    @Mock
    private HttpSession session; //Vi mocker session for at isolere testkoden uden at bruge et servermiljø. Tests er hurtigere på denne måde.


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Dette Initialiser mocks og injecter mocks
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void redirectUserLoginAttributesCorrectLogin() {
        //Hvis brugeren er korrekt logget ind, så vil vi ikke komme ind i metodens if-blokke, men derimod bare
        //returnere null

        //Arrange
        int employeeID = 3;
        int sessionUserID = 3;
        when(session.getAttribute("userID")).thenReturn(sessionUserID);

        //Act
        String expectedResult = null;
        String actualResult = userService.redirectUserLoginAttributes(session,employeeID);

        //Assert
        assertEquals(expectedResult,actualResult);
    }

    @Test
    void redirectUserLoginAttributesWrongUserLoggedIn() {
        //Arrange
        int employeeID = 2;
        int sessionUserID = 3;
        when(session.getAttribute("userID")).thenReturn(sessionUserID);

        //Act
        String expectedResult = "redirect:/user/"+sessionUserID;
        String actualResult = userService.redirectUserLoginAttributes(session,employeeID);

        //Assert
        assertEquals(expectedResult,actualResult);
    }

    @Test
    void redirectUserLoginAttributesWrongUserNotLoggedIn() {
        //Arrange
        int employeeID = 2;
        when(session.getAttribute("userID")).thenReturn(null);

        //Act
        String expectedResult = "redirect:/user/loginpage";
        String actualResult = userService.redirectUserLoginAttributes(session,employeeID);

        //Assert
        assertEquals(expectedResult,actualResult);

    }
}