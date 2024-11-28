package mmmd.teammmmd_eksamensprojekt2sem.controller;

import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import mmmd.teammmmd_eksamensprojekt2sem.model.SubProject;
import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.sql.SQLException;

@RequestMapping("/project")
@Controller
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /*
    #####################################
    #           CRUD Project            #
    #####################################
     */
    /*
    ###########---CREATE---###########
     */

    @GetMapping("/success")
    //TODO: Udelukkende til demokode for at se om metode eksekveres korrekt med redirect. Slet når ikke længere nødvendig sammen med html fil.
    public String showSuccess() {
        return "succes";
    }

    @GetMapping("/show_create_project")
    public String showCreateProject(Model model) {
        model.addAttribute("PMEmployees", projectService.findPMEmployees());
        model.addAttribute("BCEmployees", projectService.findBCEmployees());
        model.addAttribute("statusobjects", projectService.fetchAllStatus());
        model.addAttribute("customers", projectService.getListOfCurrentCustomers());

        return "createProjectForm";
    }

    /*
    ###########---READ---###########
     */
    @GetMapping("/show_all_projects")
    public String showAllProjects(Model model) {
        model.addAttribute("projects", projectService.showAllProjects());
        return "showAllProjectsTest";
        //TODO: Html template bare til eksempelvisning for at se om det virker. Skal formentlig migreres til PM dashboard, når denne er færdig
    }

    /*
    ###########---UPDATE---###########
     */
    @GetMapping("/{name}/edit") //button
    public String goToEditProject(@PathVariable String name, Model model) {
        Project project = projectService.fetchSpecificProject(name);
        model.addAttribute("project", project);
        model.addAttribute("projectCustomer", projectService.getListOfCurrentCustomers());
        model.addAttribute("projectPMEmployees", projectService.findPMEmployees());
        model.addAttribute("projectBCEmployees", projectService.findBCEmployees());
        model.addAttribute("projectStatusAll", projectService.fetchAllStatus());
        return "updateProject";
    }

    @PostMapping("/updateProject")
    public String updateProjectAction(@RequestParam int projectID, @RequestParam String projectTitle, @RequestParam String projectDescription,
                                      @RequestParam int customer, @RequestParam Date orderDate, @RequestParam Date deliveryDate,
                                      @RequestParam(required = false) String linkAgreement, @RequestParam int companyRep, @RequestParam int status) {
        Project project = new Project(projectID, projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status);
        projectService.updateProject(project);
        return "redirect:/project/success"; //TODO: Ændre redirect til PM Dashboard
    }

    /*
    ###########---DELETE---###########
     */
    @PostMapping("/{name}/delete") //Button
    public String deleteProject(@PathVariable String name) throws SQLException {
        Project project = projectService.fetchSpecificProject(name);
        projectService.deleteProject(project);
        return "redirect:/project/success"; //TODO: Ændre redirect til PM Dashboard
    }

    /*
    #####################################
    #           Customers               #
    #####################################
     */
    @GetMapping("/get-customers") //TODO: Demo kode
    public String getCustomers(Model model) {
        model.addAttribute("listofcustomers", projectService.getListOfCurrentCustomers());
        return "customers"; //Husk at slette html //TODO: Slette html
    }

    @GetMapping("/show-create-customer")
    public String showCreateCustomer() {
        return "createCustomer";
    }

    @PostMapping("/create-customer")
    public String createCustomerAction(@RequestParam String companyName, @RequestParam String repName) {
        Customer customer = new Customer(companyName, repName);
        projectService.createCustomer(customer); //TODO: Mangler go back knap, mangler kontrol af eksisterende navn og rep.
        return "succes"; //TODO slet html, bare til verifikation
    }

    @GetMapping("/createsubproject/{projectID}")


//    @PostMapping("/{employeeID}/{projectID}/saveSubProject")
//    public String saveSubProject(@PathVariable int employeeID,
//                                 @PathVariable int projectID,
//                                 @RequestParam String subProjectTitle,
//                                 @RequestParam String subProjectDescription,
//                                 @RequestParam int statusID) {
//        projectService.createSubproject();
//        return "redirect:/project/" + employeeID + "/" + projectID;
//    }

    @PostMapping("/deleteSubProject/{employeeID}/{subProjectID}")
    public String deleteSubProject(@PathVariable int employeeID, @PathVariable int subProjectID) {
        projectService.deleteSubProject(subProjectID);
        return "redirect:/project/" + employeeID;
    }

    @GetMapping("/show_all_subprojects")
    public String showAllSubProjects(Model model) {
        model.addAttribute("subProjects", projectService.showAllSubProjects());
        return "showAllSubProjectsTest";
        //TODO: Html template bare til eksempelvisning for at se om det virker. Skal formentlig migreres til PM dashboard, når denne er færdig
    }
}


