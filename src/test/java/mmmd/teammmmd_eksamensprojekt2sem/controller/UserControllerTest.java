package mmmd.teammmmd_eksamensprojekt2sem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import mmmd.teammmmd_eksamensprojekt2sem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
//Når vi skal teste i en Controller, så vil vi benytte en Mock (Mockito) for at lave en simuleret version
//af en afhængighed (Mocking), i dette tilfælde Service klassen. På den måde kan vi "kontrollere" den falske version af
// Service objektet... hvordan den skal opføre sig, og hvad den skal returnere (Stubbing), uden at køre
// Service klassens egentlige kode.


public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @MockBean
    private UserService userService;

    @MockBean
    private ProjectService projectService;


    @BeforeEach
    public void setup() {
    }

    @Test
    public void testLoginValidationCorrectCredentials() throws Exception {
        //Arrange
        String username = "validUser";
        String password = "validPass";
        int employeeID = 1;

        when(userService.validateLogin(username,password)).thenReturn(true);
        when(userService.getEmployeeIDFromDB(username)).thenReturn(employeeID);

        //Act & Assert
       mockMvc.perform(post("/user/loginvalidation")
               .param("username",username)
               .param("password", password))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/user/"+employeeID));

    }

    @Test
    public void testLoginValidationWrongCredentials() throws Exception {
        //Arrange
        String username = "validUser";
        String password = "unvalidPass";
        int employeeID = 2;

        when(userService.validateLogin(username,password)).thenReturn(false);

        //Act & Assert
        mockMvc.perform(post("/user/loginvalidation")
                        .param("username",username)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/loginpage?error=true"));
    }

    @Test
    public void testShowDashboardEmployee() throws Exception {
        //Arrange
        int employeeID = 1;
        HttpSession session = mock(HttpSession.class);

        when(userService.redirectUserLoginAttributes(session,employeeID)).thenReturn(null);
        when(userService.getIsEmployeeManagerInfoFromDB(employeeID)).thenReturn(false);

        when(projectService.showAllProjectsSpecificEmployee(employeeID)).thenReturn(Collections.emptyList());
        when(projectService.showAllTasksSpecificEmployee(employeeID)).thenReturn(Collections.emptyList());
        when(projectService.showAllProjectSpecificProjectManager(employeeID)).thenReturn(Collections.emptyList());
        when(projectService.showAllTasksSpecificProjectManager(employeeID)).thenReturn(Collections.emptyList());
        when(projectService.listOfTasksSpecificPMWithNoAssignedEmployee(employeeID)).thenReturn(Collections.emptyList());
        when(userService.getEmployee(employeeID)).thenReturn(new Employee());

        //Act & Assert
        mockMvc.perform(get("/user/"+employeeID))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("dashboardEmployee"))
                .andExpect(model().attributeExists("projects", "employee", "tasks", "PMprojects", "PMtasks",
                        "tasksNotAssigned", "employeeID"));
    }

    @Test
    public void testShowDashboardProjectmanager() throws Exception {
        //Arrange
        int employeeID = 1;
        HttpSession session = mock(HttpSession.class);

        when(userService.redirectUserLoginAttributes(session,employeeID)).thenReturn(null);
        when(userService.getIsEmployeeManagerInfoFromDB(employeeID)).thenReturn(true);

        when(projectService.showAllProjectsSpecificEmployee(employeeID)).thenReturn(Collections.emptyList());
        when(projectService.showAllTasksSpecificEmployee(employeeID)).thenReturn(Collections.emptyList());
        when(projectService.showAllProjectSpecificProjectManager(employeeID)).thenReturn(Collections.emptyList());
        when(projectService.showAllTasksSpecificProjectManager(employeeID)).thenReturn(Collections.emptyList());
        when(projectService.listOfTasksSpecificPMWithNoAssignedEmployee(employeeID)).thenReturn(Collections.emptyList());
        when(userService.getEmployee(employeeID)).thenReturn(new Employee());

        //Act & Assert
        mockMvc.perform(get("/user/"+employeeID))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("dashboardProjectManager"))
                .andExpect(model().attributeExists("projects", "employee", "tasks", "PMprojects", "PMtasks",
                        "tasksNotAssigned", "employeeID"));
    }
}
