package mmmd.teammmmd_eksamensprojekt2sem.controller;

import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import org.springframework.stereotype.Controller;
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
                                      @RequestParam String linkAgreement, @RequestParam int companyRep, @RequestParam int status) {
        /*
        Daniel - DanielJensenKEA
         */
        Project project = new Project(projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status);
        projectService.createProject(project);
        projectService.setProjectID(project);

        /*
        TODO: , korriger redirect
         */
        return "redirect://";
    }
    @GetMapping("show_create_project")
    public String showCreateProject() {
        return "createProjectForm";
    }

}


