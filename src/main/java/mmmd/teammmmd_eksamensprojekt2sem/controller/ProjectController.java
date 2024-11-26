package mmmd.teammmmd_eksamensprojekt2sem.controller;

import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

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
    @PostMapping("/create_project")
    public String createProjectAction(@RequestParam String projectTitle, @RequestParam String projectDescription,
                                      @RequestParam int customer, @RequestParam Date orderDate, @RequestParam Date deliveryDate,
                                      @RequestParam(required = false)String linkAgreement, @RequestParam int companyRep, @RequestParam int status) {
        /*
        Daniel - DanielJensenKEA
         */
        Project project = new Project(projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status);
        //Find customer() - Branched
        //Find companyRep ( Business Consultants + Project Managers (X)
        //linkagreement optional
        projectService.createProject(project); // Projekt oprettes i DB
        projectService.setProjectID(project); // Projekt ID sættes i tilfælde af, at objektets ID benyttes andre steder
        //TODO: List of available customers dropdown OR CREATE new - customer, List of available employees internal, List of available enums for status:

        /*
        TODO: , korriger redirect
         */
        return "redirect://";
    }
    @GetMapping("show_create_project")
    public String showCreateProject(Model model) {
        model.addAttribute("PMEmployees", projectService.findPMEmployees());
        model.addAttribute("BCEmployees", projectService.findBCEmployees());


        return "createProjectForm";
    }

}


