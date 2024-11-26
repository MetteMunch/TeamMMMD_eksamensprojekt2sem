package mmmd.teammmmd_eksamensprojekt2sem.controller;

import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@RequestMapping("/project")
@Controller
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    @GetMapping("/get-customers") //TODO: Demo kode
    public String getCustomers(Model model) {
        model.addAttribute("listofcustomers", projectService.getListOfCurrentCustomers());
        return "customers"; //Husk at slette html
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

    /*
    #####################################
    #           CRUD Project            #
    #####################################
     */
    @PostMapping("/create_project")
    public String createProjectAction(@RequestParam String projectTitle, @RequestParam String projectDescription,
                                      @RequestParam int customer, @RequestParam Date orderDate, @RequestParam Date deliveryDate,
                                      @RequestParam(required = false)String linkAgreement, @RequestParam int companyRep, @RequestParam int status) {
        /*
        Daniel - DanielJensenKEA
         */
        Project project = new Project(projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status);
        projectService.createProject(project); // Projekt oprettes i DB
        projectService.setProjectID(project); // Projekt ID sættes i tilfælde af, at objektets ID benyttes andre steder

        //TODO: Kræver et kundenummer på 99 for internal projects. I html er der en select form, hvor internal project=99. Skal akkomoderes i SQL scripts ved næste merge.
        return "redirect:/project/success"; //TODO:korriger redirect til Project Manager dashboard, når denne er færdig
    }
    @GetMapping("/success")
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
    @GetMapping("/show_all_projects")
    public String showAllProjects(Model model) {
        model.addAttribute("projects", projectService.showAllProjects());
        return "showAllProjectsTest";
        //TODO: Html template bare til eksempelvisning for at se om det virker. Skal formentlig migreres til PM dashboard, når denne er færdig
    }

}


