package mmmd.teammmmd_eksamensprojekt2sem.controller;

import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Task;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import mmmd.teammmmd_eksamensprojekt2sem.model.SubProject;
import mmmd.teammmmd_eksamensprojekt2sem.model.Task;
import mmmd.teammmmd_eksamensprojekt2sem.model.Status;
import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/project")
@Controller
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
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
        return "succes"; //TODO slet html, bare til verifikation. Husk at ændre i ProjectControllerTest.
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
            if (customer == 99) { //TODO: Problem med skalerbarhed ;) - En mere dynamisk måde at tjekke for dette sammenholdt med html eftertragtes. Måske skal 'Internal Project' kunde bare sættes ind som den allerførste kunde i databasen.
                Customer internalProject = projectService.fetchInternalProjectCustomer();
                customer = internalProject.getCustomerID();
            }
            Project project = new Project(projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status);
            projectService.createProject(project); // Projekt oprettes i DB
            projectService.setProjectID(project); // Projekt ID sættes i tilfælde af, at objektets ID benyttes andre steder

            redirectAttributes.addAttribute("projectID",project.getID());
            //TODO: Kræver et kundenummer på 99 for internal projects. I html er der en select form, hvor internal project=99. Skal akkomoderes i SQL scripts ved næste merge.
            //TODO: Tilføj gå tilbage eller return to PM Dashboard i html
            return "redirect:/project/{projectID}"; //TODO:korriger redirect til Project Manager dashboard, når denne er færdig
        }
    }
//    @GetMapping("/success") //TODO: Udelukkende til demokode for at se om metode eksekveres korrekt med redirect. Slet når ikke længere nødvendig sammen med html fil.
//    public String showSuccess() {
//        return "succes";
//    }

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

    @GetMapping("/{projectID}")
    public String showProject(@PathVariable int projectID, Model model) {
        Project project = projectService.fetchSpecificProject(projectID);
        List<SubProject> listOfSpecificSubProjects = projectService.showListOfSpecificSubProjects(projectID);
        model.addAttribute("project",project);
        model.addAttribute("listOfSubProjects",listOfSpecificSubProjects);

        return "showProject";

    }


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
                                      @RequestParam(required = false) String linkAgreement, @RequestParam int companyRep, @RequestParam int status) {
        Project project = new Project(projectID, projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status);
        projectService.updateProject(project);
        return "redirect:/project/success"; //TODO: Ændre redirect til PM Dashboard. Husk at korriger i ProjectControllerTest også.
    }

    /*
    ###########---DELETE---###########
     */
    @PostMapping("/{name}/delete") //Button
    public String deleteProject(@PathVariable String name) throws SQLException {
        Project project = projectService.fetchSpecificProject(name);
        projectService.deleteProject(project);
        return "redirect:/project/success"; //TODO: Ændre redirect til PM Dashboard. Husk at ændre test i projectControllerTest.
    }



    /*
    #####################################
    #           SubProject              #
    #####################################
    */
    @GetMapping("/{projectID}/createsubproject")
    public String createSubProject(@PathVariable int projectID, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("projectID", projectID);
        redirectAttributes.addFlashAttribute("message", "SubProject created succesfully");
        return "createSubProjectForm";
    }

    @PostMapping("/{projectID}/savesubproject")
    public String saveSubProject(@PathVariable int projectID,
                                 @RequestParam String subProjectTitle,
                                 @RequestParam String subProjectDescription,
                                 @RequestParam int statusID) {
        SubProject newSubProject = new SubProject(subProjectTitle, subProjectDescription, projectID, statusID);

        projectService.createSubproject(newSubProject);

        return "redirect:/project/" + projectID + "/subproject";
    }
    //TODO: Lav et view der hvor vi lander efter at subproject er gemt fx. subprojectview

//    @PostMapping("/deleteSubProject/{employeeID}/{subProjectID}")
//    public String deleteSubProject(@PathVariable int employeeID, @PathVariable int subProjectID) {
//        projectService.deleteSubProject(subProjectID);
//        return "redirect:/project/" + employeeID;
//    }

//    @GetMapping("/{projectID}/show_all_subprojects")
//    public String showAllSubProjects(@PathVariable int projectID, Model model) {
//        model.addAttribute("subProjects", projectService.showAllSubProjects());
//        return "showAllSubProjectsTest";
//        //TODO: Html template bare til eksempelvisning for at se om det virker. Skal formentlig migreres til PM dashboard, når denne er færdig
//    }

    @GetMapping("/{projectID}/show_specific_subprojects")
    public String showSpecificSubProjects(@PathVariable int projectID, Model model) {
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjects", projectService.showListOfSpecificSubProjects(projectID));
        return "showSpecificSubProjects";
        //TODO: Html template bare til eksempelvisning for at se om det virker. Skal formentlig migreres til PM dashboard, når denne er færdig
    }

    /*
    ##################################
    #           CRUD Task            #
    ##################################

    ###########---CREATE---###########
     */
    @GetMapping("/{projectID}/{subProjectID}/createtask")
    public String createTask(@PathVariable int projectID, @PathVariable int subProjectID, Model model) throws SQLException {
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjectID", subProjectID);
        model.addAttribute("nonManagerEmployees", projectService.findNonManagerEmployees());
        model.addAttribute("tasks", projectService.getAllTasksInSpecificSubProject(subProjectID));
        model.addAttribute("nonManagerRoles", projectService.getNonManagerRoles());
        model.addAttribute("statusobjects", projectService.fetchAllStatus());
        return "createTask";
    }

    @PostMapping("/{projectID}/{subProjectID}/savetask")
    public String saveTask(
            @PathVariable int projectID,
            @PathVariable int subProjectID,
            @RequestParam String taskTitle,
            @RequestParam(required = false) String taskDescription,
            @RequestParam(required = false) Integer assignedEmployee,
            @RequestParam(required = false) Double estimatedTime,
            @RequestParam int status,
            @RequestParam Date plannedStartDate,
            @RequestParam(required = false) Integer dependingOnTask,
            @RequestParam(required = false) Integer requiredRole) throws SQLException {

        Task newTask = new Task(taskTitle, taskDescription,
                assignedEmployee, estimatedTime, plannedStartDate,
                dependingOnTask, requiredRole, subProjectID, status);

        projectService.createTask(projectID, subProjectID, newTask);

        return "redirect:/project/" + projectID + "/" + subProjectID + "/tasks";
    }


    /*
    ###########---READ---###########
     */
    /*@GetMapping("/{projectID}/{subProjectID}/tasks")
    public String showTasks(@PathVariable int projectID, @PathVariable int subProjectID, Model model) {
        model.addAttribute("tasks", projectService.fetchTasksForSubproject(subProjectID));
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjectID", subProjectID);
        return "viewTasks";
    }
    /*
    ###########---UPDATE---###########
     */
    /*@GetMapping("/{projectID}/{subProjectID}/editTask/{taskID}")
    public String editTask(@PathVariable int projectID, @PathVariable int subProjectID, @PathVariable int taskID, Model model) {
        Task task = projectService.fetchTaskById(taskID);
        model.addAttribute("task", task);
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjectID", subProjectID);
        model.addAttribute("nonManagerEmployees", projectService.findNonManagerEmployees());
        model.addAttribute("roles", projectService.getRoles());
        model.addAttribute("statusobjects", projectService.fetchAllStatus());
        return "updateTask";
    }

    @PostMapping("/{projectID}/{subProjectID}/updateTask")
    public String updateTask(
            @PathVariable int projectID,
            @PathVariable int subProjectID,
            @RequestParam String taskTitle,
            @RequestParam(required = false) String taskDescription,
            @RequestParam(required = false) Integer assignedEmployee,
            @RequestParam(required = false) Double estimatedTime,
            @RequestParam int status,
            @RequestParam(required = false) Date plannedStartDate,
            @RequestParam(required = false) Integer dependingOnTask,
            @RequestParam(required = false) Integer requiredRole) throws SQLException {

        Task updatedTask = new Task(taskTitle, taskDescription, assignedEmployee, estimatedTime, plannedStartDate, dependingOnTask, requiredRole, subProjectID, status);
        projectService.updateTaskInProject(projectID, subProjectID, updatedTask);

        return "redirect:/project/" + projectID + "/" + subProjectID + "/tasks";
    } */

    /*
    ###########---DELETE---###########
    */
    @PostMapping("/{projectID}/{subProjectID}/deleteTask/{taskID}")
    public String deleteTask(@PathVariable int projectID, @PathVariable int subProjectID, @PathVariable int taskID) throws SQLException {
        projectService.deleteTask(taskID);
        return "redirect:/project/" + projectID + "/" + subProjectID + "/tasks";
    }


}