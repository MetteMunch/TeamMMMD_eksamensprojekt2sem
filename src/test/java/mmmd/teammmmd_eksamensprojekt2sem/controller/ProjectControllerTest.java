package mmmd.teammmmd_eksamensprojekt2sem.controller;

import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.SubProject;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


import java.sql.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {
    @Autowired // med denne annotation fortæller vi Spring, at den automatisk skal indsætte (injecte) en instans
    //af denne afhængighed. Dvs vi skal ikke oprette instansen manuelt med new. Spring håndterer instansieringen.
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @MockBean //med denne annotation instruere vi Spring Boot i at oprette en Mock-version af UserService, som
    //vi kan manipulere med under testen
    private ProjectService mockProjectService;
    @MockBean
    private UserService mockUserService;
    //    String requestMapping = "/project";
    String requestMapping = "/user/{employeeID}";
    private Project project;
    private int employeeID;
    private SubProject subProject;

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
        subProject.setSubProjectTitle("Test SubTitle");
        subProject.setSubProjectDescription("Test SubDescription");
        subProject.setSubProjectID(1);
        subProject.setStatusID(1);
        when(mockProjectService.checkIfProjectNameAlreadyExists(anyString())).thenReturn(false);
        when(mockProjectService.findProjectIDFromDB(any(Project.class))).thenReturn(project.getID());
    }

    @Test
    void createProjectActionSuccess() throws Exception {
        int employeeID = 4;
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
//    @Test
//    void createProjectActionFailNameAlreadyExists() throws Exception {
//        when(projectService.checkIfProjectNameAlreadyExists(anyString())).thenReturn(true);
//
//        mockMvc.perform(post(requestMapping+"/create_project")
//                        .param("projectTitle", project.getProjectTitle())
//                        .param("projectDescription", project.getProjectDescription())
//                        .param("customer", String.valueOf(project.getCustomer()))
//                        .param("orderDate", String.valueOf(project.getOrderDate()))
//                        .param("deliveryDate", String.valueOf(project.getDeliveryDate()))
//                        .param("linkAgreement", project.getLinkAgreement())
//                        .param("companyRep", String.valueOf(project.getCompanyRep()))
//                        .param("status", String.valueOf(project.getStatus())))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl(requestMapping+"/show_create_project"))
//                .andExpect(flash().attribute("titleAlreadyExistsError", "The selected project title already exists. " +
//                        "Please select another title for this project."));
//
//    }
//    @Test
//    void showCreateProject() throws Exception {
//        mockMvc.perform(get(requestMapping+"/show_create_project"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("createProjectForm"))
//                .andExpect(model().attribute("PMEmployees", projectService.findPMEmployees()))
//                .andExpect(model().attribute("BCEmployees", projectService.findBCEmployees()))
//                .andExpect(model().attribute("statusobjects", projectService.fetchAllStatus()))
//                .andExpect(model().attribute("customers", projectService.getListOfCurrentCustomers()));
//    }
//    @Test
//    void showAllProjects() throws Exception {
//        mockMvc.perform(get(requestMapping+"/show_all_projects"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("projects", projectService.showAllProjects()))
//                .andExpect(view().name("showAllProjectsTest")); //Ret mit navn til korrekte redirect html
//    }
//    @Test
//    void goToEditProject() throws Exception {
//        mockMvc.perform(get(requestMapping+"/"+project.getProjectTitle()+"/edit"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("updateProject"))
//                .andExpect(model().attribute("project", project))
//                .andExpect(model().attribute("projectCustomer", projectService.getListOfCurrentCustomers()))
//                .andExpect(model().attribute("projectPMEmployees", projectService.findPMEmployees()))
//                .andExpect(model().attribute("projectBCEmployees", projectService.findBCEmployees()))
//                .andExpect(model().attribute("projectStatusAll", projectService.fetchAllStatus()));
//    }
//    @Test
//    void updateProjectAction() throws Exception {
//        mockMvc.perform(post(requestMapping+"/updateProject")
//                        .param("projectID", String.valueOf(project.getID()))
//                        .param("projectTitle", project.getProjectTitle())
//                        .param("projectDescription", project.getProjectDescription())
//                        .param("customer", String.valueOf(project.getCustomer()))
//                        .param("orderDate", String.valueOf(project.getOrderDate()))
//                        .param("deliveryDate", String.valueOf(project.getDeliveryDate()))
//                        .param("linkAgreement", project.getLinkAgreement())
//                        .param("companyRep", String.valueOf(project.getCompanyRep()))
//                        .param("status", String.valueOf(project.getStatus())))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl(requestMapping+"/success")); //Ændre mig til korrekte html redirect
//    }
//    @Test
//    void deleteProject() throws Exception {
//        mockMvc.perform(post(requestMapping+"/{name}/delete", project.getProjectTitle()))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl(requestMapping+"/success")); //Ændre mig til korrekte redirect view
//    }
//    @Test
//    void showCreateCustomer() throws Exception {
//        mockMvc.perform(get(requestMapping+"/show-create-customer"))
//                .andExpect(view().name("createCustomer"));
//    }
//    @Test
//    void createCustomerAction() throws Exception {
//        Customer customer = new Customer("Name", "Rep");
//        mockMvc.perform(post(requestMapping+"/create-customer")
//                        .param("companyName", customer.getCompanyName())
//                        .param("repName", customer.getRepName()))
//                .andExpect(view().name("succes")); //Ændre mig til korrekt view
//    }
//
//


    @Test
    void createSubProjectActionSucces() throws Exception {
        int projectID = 1;
        employeeID = 1;
        mockMvc.perform(get("/{projectID}/create-subproject", projectID)
                .param("subProjectTitle", subProject.getSubProjectTitle())
                .param("subProjectDescription", subProject.getSubProjectDescription())
                .param("subProject", String.valueOf(subProject.getProjectID()))
                .param("subProjectStatus", String.valueOf(subProject.getStatusID())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/user/{employeeID}/{projectID}", employeeID, subProject.getSubProjectID()));
    }
}

