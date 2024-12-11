package mmmd.teammmmd_eksamensprojekt2sem.controller;

import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.SubProject;
import mmmd.teammmmd_eksamensprojekt2sem.model.Task;
import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import mmmd.teammmmd_eksamensprojekt2sem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.*;


import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {
    @Autowired // med denne annotation fortæller vi Spring, at den automatisk skal indsætte (injecte) en instans
    //af denne afhængighed. Dvs vi skal ikke oprette instansen manuelt med new. Spring håndterer instansieringen.
    private MockMvc mockMvc;
    //    @Autowired
//    private UserService userService;
    @MockBean //med denne annotation instruere vi Spring Boot i at oprette en Mock-version af UserService, som
    //vi kan manipulere med under testen
    private ProjectService mockProjectService;
    @MockBean
    private UserService mockUserService;
    //    String requestMapping = "/project";
    String requestMapping = "/user/{employeeID}";
    private Project project;
    private int employeeID;

    @BeforeEach
    public void setup() {
        employeeID = 4;
        project = new Project();
        project.setID(999);
        project.setProjectTitle("Test Title");
        project.setProjectDescription("Test Description");
        project.setCustomer(9999);
        Date orderDate = Date.valueOf("2024-12-12");
        Date deliveryDate = Date.valueOf("2025-01-01");
        project.setOrderDate(orderDate);
        project.setDeliveryDate(deliveryDate);
        project.setLinkAgreement("My link");
        project.setCompanyRep(4444);
        project.setStatus(1);
        when(mockProjectService.checkIfProjectNameAlreadyExists(anyString())).thenReturn(false);
        when(mockProjectService.findProjectIDFromDB(any(Project.class))).thenReturn(project.getID());
    }

    @Test
    void createProjectActionSuccess() throws Exception {
        /*
        .param => Vi forventer at få alle disse argumenter med, som er angivet som parameter i endpoint.
        Vi indsætter vores testobjekter som argumenter til endpointet.
        Vi forventer derefter en 300 HTTP statuskode som er en redirect til vores ProjectController
        requestmapping+projectID.
         */
        mockMvc.perform(post("/user/{employeeID}/create-project", employeeID)
                        .param("projectTitle", project.getProjectTitle())
                        .param("projectDescription", project.getProjectDescription())
                        .param("customer", String.valueOf(project.getCustomer()))
                        .param("orderDate", String.valueOf(project.getOrderDate()))
                        .param("deliveryDate", String.valueOf(project.getDeliveryDate()))
                        .param("linkAgreement", project.getLinkAgreement())
                        .param("companyRep", String.valueOf(project.getCompanyRep()))
                        .param("status", String.valueOf(project.getStatus()))
                        .param("employeeID", String.valueOf(employeeID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/user/{employeeID}/{projectID}", employeeID, project.getID()));
    }

    @Test
    void createProjectActionFailNameAlreadyExists() throws Exception {
        when(mockProjectService.checkIfProjectNameAlreadyExists(anyString())).thenReturn(true);
        /*
        Vi instruerer test i, at der allerede foreligger et eksisterende projektnavn, og vi vil redirecte med
        en fejlbesked(flashAttribute) på html og fortæller dette til brugeren.
         */

        mockMvc.perform(post("/user/{employeeID}/create-project", employeeID)
                        .param("projectTitle", project.getProjectTitle())
                        .param("projectDescription", project.getProjectDescription())
                        .param("customer", String.valueOf(project.getCustomer()))
                        .param("orderDate", String.valueOf(project.getOrderDate()))
                        .param("deliveryDate", String.valueOf(project.getDeliveryDate()))
                        .param("linkAgreement", project.getLinkAgreement())
                        .param("companyRep", String.valueOf(project.getCompanyRep()))
                        .param("status", String.valueOf(project.getStatus()))
                        .param("employeeID", String.valueOf(employeeID)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/user/{employeeID}/show-create-project", employeeID))
                .andExpect(flash().attribute("titleAlreadyExistsError", "The selected project title already exists. " +
                        "Please select another title for this project."));

    }

    @Test
    void showCreateProjectMngr() throws Exception {
        /*
        Vi tester om viewet bliver vist, hvis brugeren, der logger ind er en Project Manager.
        Kun PM må lave projekter. Denne boolean rolle bliver angivet i employeeRole tabellen.
         */
        when(mockUserService.getIsEmployeeManagerInfoFromDB(employeeID)).thenReturn(true);

        mockMvc.perform(get("/user/{employeeID}/show-create-project", employeeID)
                        .param("employeeID", String.valueOf(employeeID)))
                .andExpect(status().isOk())
                .andExpect(view().name("createProjectForm"))
                .andExpect(model().attribute("PMEmployees", mockProjectService.findPMEmployees()))
                .andExpect(model().attribute("BCEmployees", mockProjectService.findBCEmployees()))
                .andExpect(model().attribute("statusobjects", mockProjectService.fetchAllStatus()))
                .andExpect(model().attribute("customers", mockProjectService.getListOfCurrentCustomers()))
                .andExpect(model().attribute("employeeID", employeeID));
    }
    @Test
    void showCreateProjectNOTPM() throws Exception {
        /*
        Brugere, som ikke er PM skal ikke være i stand til at lave projekter. Hvis de ikke har den
        fornødne boolean rolle skal de redirectes tilbage til deres dashboard.
         */
        when(mockUserService.getIsEmployeeManagerInfoFromDB(employeeID)).thenReturn(false);

        mockMvc.perform(get("/user/{employeeID}/show-create-project", employeeID)
                .param("employeeID", String.valueOf(employeeID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/user/{employeeID}", employeeID));
    }
    @Test
    void showProject() throws Exception {

        SubProject subProjectTest = new SubProject("TestTitle", "TestDescription", project.getID(), 1);
        Task taskTest = new Task(99, "Test Task", "Test Description", employeeID,
                40.0, Date.valueOf("2024-11-12"),null,
                null, subProjectTest.getSubProjectID(), 1);
        when(mockProjectService.fetchSpecificProject(anyInt())).thenReturn(project);
        when(mockProjectService.showListOfSpecificSubProjects(anyInt())).thenReturn(List.of(subProjectTest));
        when(mockProjectService.tasksWithCalculatedEndDateLaterThanProjectDeadline(employeeID, project.getID()))
                .thenReturn(List.of(taskTest));
        /*
        when() kald => Vi mocker vores metodekald her. Når ovenstående metoder kaldes på vores /{projectID} endpoint
        instruerer vi vores test til at returnere vores testobjekter og -lister. Vi imiterer derfor vores egentlige
        endpoint logik i ProjectController.
         */

        List<SubProject> listSub = mockProjectService.showListOfSpecificSubProjects(project.getID());
        List<Task> listTask = mockProjectService.tasksWithCalculatedEndDateLaterThanProjectDeadline(employeeID, project.getID());

        mockMvc.perform(get("/user/{employeeID}/{projectID}", employeeID, project.getID())
                .param("projectID", String.valueOf(project.getID()))
                .param("employeeID", String.valueOf(employeeID)))
                .andExpect(status().isOk())
                .andExpect(view().name("showProject"))
                .andExpect(model().attribute("project", project))
                .andExpect(model().attribute("listOfSubProjects", listSub))
                .andExpect(model().attribute("employeeID", employeeID))
                .andExpect(model().attribute("isManager", mockUserService.getIsEmployeeManagerInfoFromDB(employeeID)))
                .andExpect(model().attribute("TasksWithEndDateToLate", listTask));
    }

    @Test
    void goToEditProject() throws Exception {
        when(mockProjectService.fetchSpecificProject(project.getID())).thenReturn(project);

        mockMvc.perform(get("/user/{employeeID}/{projectID}/edit", employeeID, project.getID()))
                .andExpect(status().isOk())
                .andExpect(view().name("updateProject"))
                .andExpect(model().attribute("project", project))
                .andExpect(model().attribute("projectCustomer", mockProjectService.getListOfCurrentCustomers()))
                .andExpect(model().attribute("projectPMEmployees", mockProjectService.findPMEmployees()))
                .andExpect(model().attribute("projectBCEmployees", mockProjectService.findBCEmployees()))
                .andExpect(model().attribute("projectStatusAll", mockProjectService.fetchAllStatus()));
    }
    @Test
    void updateProjectAction() throws Exception {
        mockMvc.perform(post("/user/{employeeID}/update-project", employeeID)
                        .param("projectID", String.valueOf(project.getID()))
                        .param("projectTitle", project.getProjectTitle())
                        .param("projectDescription", project.getProjectDescription())
                        .param("customer", String.valueOf(project.getCustomer()))
                        .param("orderDate", String.valueOf(project.getOrderDate()))
                        .param("deliveryDate", String.valueOf(project.getDeliveryDate()))
                        .param("linkAgreement", project.getLinkAgreement())
                        .param("companyRep", String.valueOf(project.getCompanyRep()))
                        .param("status", String.valueOf(project.getStatus()))
                        .param("employeeID", String.valueOf(employeeID)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/user/{employeeID}", employeeID)); //Ændre mig til korrekte html redirect
    }
    @Test
    void deleteProject() throws Exception {
        when(mockProjectService.fetchSpecificProject(project.getID())).thenReturn(project);
        mockMvc.perform(post("/user/{employeeID}/{projectID}/delete", employeeID, project.getID()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/user/{employeeID}", employeeID)); //Ændre mig til korrekte redirect view
    }
    @Test
    void showCreateCustomer() throws Exception {
        mockMvc.perform(get("/user/{employeeID}/show-create-customer", employeeID))
                .andExpect(view().name("createCustomer"));
    }
    @Test
    void createCustomerAction() throws Exception {
        Customer customer = new Customer("Name", "Rep");
//        customer.setCustomerID(9999);

        mockMvc.perform(post("/user/{employeeID}/create-customer", employeeID)
                        .param("companyName", customer.getCompanyName())
                        .param("repName", customer.getRepName())
                        .param("projectID", String.valueOf(project.getID()))
                        .param("employeeID", String.valueOf(employeeID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/user/{employeeID}/{projectID}", employeeID, project.getID())); //Ændre mig til korrekt view
    }
//
//
//
//
//
}
