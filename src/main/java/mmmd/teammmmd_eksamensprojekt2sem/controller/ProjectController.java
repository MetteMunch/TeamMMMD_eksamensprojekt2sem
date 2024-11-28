package mmmd.teammmmd_eksamensprojekt2sem.controller;

import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/create_project") //CREATE
    public String createProjectAction(@RequestParam String projectTitle, @RequestParam String projectDescription,
                                      @RequestParam int customer, @RequestParam Date orderDate, @RequestParam Date deliveryDate,
                                      @RequestParam(required = false)String linkAgreement, @RequestParam int companyRep, @RequestParam int status, RedirectAttributes redirectAttributes) {
        if (projectService.checkIfProjectNameAlreadyExists(projectTitle)) { //returnerer true, hvis navnet allerede eksisterer i DB.
            redirectAttributes.addFlashAttribute("titleAlreadyExistsError", "The selected project title already exists. " +
                    "Please select another title for this project.");
            return "redirect:/project/show_create_project";
        }
        else {
            Project project = new Project(projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status);
            projectService.createProject(project); // Projekt oprettes i DB
            projectService.setProjectID(project); // Projekt ID sættes i tilfælde af, at objektets ID benyttes andre steder
            //TODO: Kræver et kundenummer på 99 for internal projects. I html er der en select form, hvor internal project=99. Skal akkomoderes i SQL scripts ved næste merge.
            //TODO: Tilføj gå tilbage eller return to PM Dashboard i html
            return "redirect:/project/success"; //TODO:korriger redirect til Project Manager dashboard, når denne er færdig
        }
    }
    @GetMapping("/success") //TODO: Udelukkende til demokode for at se om metode eksekveres korrekt med redirect. Slet når ikke længere nødvendig sammen med html fil.
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
        return "showAllProjectsTest"; //TODO: Husk at rette showAllProjectsTest() ved sletning af demo html.
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
                                @RequestParam(required = false)String linkAgreement, @RequestParam int companyRep, @RequestParam int status) {
        Project project = new Project(projectID,projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status);
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

}


