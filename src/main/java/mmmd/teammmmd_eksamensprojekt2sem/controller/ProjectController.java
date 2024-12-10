package mmmd.teammmmd_eksamensprojekt2sem.controller;

import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Task;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import mmmd.teammmmd_eksamensprojekt2sem.model.SubProject;
import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import mmmd.teammmmd_eksamensprojekt2sem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user/{employeeID}")
@Controller
public class ProjectController {

    private final ProjectService projectService;

    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
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
    public String showCreateCustomer(@PathVariable int employeeID, Model model) {
        model.addAttribute("employeeID", employeeID);
        return "createCustomer";
    }

    @PostMapping("/create-customer")
    public String createCustomerAction(@RequestParam String companyName, @RequestParam String repName,
                                       @RequestParam int projectID, RedirectAttributes redirectAttributes, @PathVariable int employeeID, Model model) {
        Customer customer = new Customer(companyName, repName);
        projectService.createCustomer(customer);

        // Vi skal have fat i den oprettede kundes ID så dette kan sættes på rette projekt i stedet for id for new
        int customerId = projectService.lookUpCustomerIDFromDB(customer);
        projectService.updateProjectsCustomerID(projectID,customerId);

        redirectAttributes.addAttribute("projectID", projectID);
        redirectAttributes.addAttribute("employeeID", employeeID);
        return "redirect:/user/{employeeID}/{projectID}";
    }


    /*
    #####################################
    #           CRUD Project            #
    #####################################
     */
    /*
    ###########---CREATE---###########
     */
    @PostMapping("/create-project") //CREATE
    public String createProjectAction(@RequestParam String projectTitle, @RequestParam String projectDescription,
                                      @RequestParam int customer, @RequestParam Date orderDate, @RequestParam Date deliveryDate,
                                      @RequestParam(required = false)String linkAgreement, @RequestParam int companyRep,
                                      @RequestParam int status,@PathVariable int employeeID, RedirectAttributes redirectAttributes, Model model) {
        if (projectService.checkIfProjectNameAlreadyExists(projectTitle)) { //returnerer true, hvis navnet allerede eksisterer i DB.
            redirectAttributes.addFlashAttribute("titleAlreadyExistsError", "The selected project title already exists. " +
                    "Please select another title for this project.");
            return "redirect:/user/{employeeID}/show-create-project";
        }
        else {
//            if (customer == 1) {
//                Customer internalProject = projectService.fetchInternalProjectCustomer();
//                customer = internalProject.getCustomerID();
//            }

            Project project = new Project(projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status);
            projectService.createProject(project); // Projekt oprettes i DB
            int pID = projectService.findProjectIDFromDB(project);
            project.setID(pID);

            if (customer == 2) {
                model.addAttribute("employeeID", employeeID);
                model.addAttribute("projectID", pID);
                return "createCustomer";
            }

            redirectAttributes.addAttribute("projectID",pID);
            redirectAttributes.addAttribute("employeeID", employeeID);
            return "redirect:/user/{employeeID}/{projectID}";

        }
    }

    @GetMapping("/show-create-project")
    public String showCreateProject(@PathVariable("employeeID") int employeeID, Model model) {
        if(projectService.isManager(employeeID)) {
            model.addAttribute("PMEmployees", projectService.findPMEmployees());
            model.addAttribute("BCEmployees", projectService.findBCEmployees());
            model.addAttribute("statusobjects", projectService.fetchAllStatus());
            model.addAttribute("customers", projectService.getListOfCurrentCustomers());
            model.addAttribute("employeeID", employeeID);

            return "createProjectForm";
        } else {
            return "redirect:/user/{employeeID}";
        }
    }

    /*
    ###########---READ---###########
     */

    @GetMapping("/{projectID}")
    public String showProject(@PathVariable int projectID, Model model,@PathVariable int employeeID) {
        Project project = projectService.fetchSpecificProject(projectID);
        List<SubProject> listOfSpecificSubProjects = projectService.showListOfSpecificSubProjects(projectID);
        List<Task> listOfTasksWithEndDateLaterThanProjectDeadline = projectService.tasksWithCalculatedEndDateLaterThanProjectDeadline(employeeID, projectID);
        model.addAttribute("project",project);
        model.addAttribute("listOfSubProjects",listOfSpecificSubProjects);
        model.addAttribute("employeeID",employeeID);
        model.addAttribute("isManager", projectService.isManager(employeeID));
        model.addAttribute("TasksWithEndDateToLate", listOfTasksWithEndDateLaterThanProjectDeadline);
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
    @GetMapping("/{projectID}/create-subproject")
    public String createSubProject(@PathVariable int projectID, @PathVariable int employeeID, Model model, RedirectAttributes redirectAttributes) {
        if(projectService.isManager(employeeID)) {
            model.addAttribute("projectID", projectID);
            model.addAttribute("employeeID", employeeID);
            redirectAttributes.addFlashAttribute("message", "SubProject created succesfully");
            return "createSubProjectForm";
        } else {
            return "redirect:/user/{employeeID}";
        }

    }

    @PostMapping("/{projectID}/save-subproject")
    public String saveSubProject(@PathVariable int projectID,
                                 @PathVariable int employeeID,
                                 @RequestParam String subProjectTitle,
                                 @RequestParam String subProjectDescription,
                                 @RequestParam int statusID,
                                 RedirectAttributes redirectAttributes) {
        SubProject newSubProject = new SubProject(subProjectTitle, subProjectDescription, projectID, statusID);


        projectService.createSubproject(newSubProject);
        redirectAttributes.addAttribute("employeeID", employeeID);
        redirectAttributes.addAttribute("projectID", projectID);

        return "redirect:/user/{employeeID}/{projectID}";
    }
    //TODO: Lav et view der hvor vi lander efter at subproject er gemt fx. subprojectview

    @PostMapping("/{projectID}/{subProjectID}/delete-subproject")
    public String deleteSubProject(@PathVariable int employeeID, @PathVariable int subProjectID, @PathVariable int projectID, RedirectAttributes redirectAttributes) {
        projectService.deleteSubProject(subProjectID);
        redirectAttributes.addAttribute("employeeID", employeeID);
        redirectAttributes.addAttribute("projectID", projectID);
        return "redirect:/user/{employeeID}/{projectID}";
    }

    @GetMapping("/{projectID}/{subProjectID}")
    public String showSubProject(@PathVariable int employeeID, @PathVariable int projectID, @PathVariable int subProjectID, Model model) throws SQLException {

        if (userService.getIsEmployeeManagerInfoFromDB(employeeID)) {
            model.addAttribute("subProject", projectService.showSubProject(subProjectID));
            model.addAttribute("tasks",projectService.getAllTasksInSpecificSubProject(subProjectID));
            model.addAttribute("employeeID", employeeID);
            return "showSubProject"; //Manager = true
        } else {
            model.addAttribute("subProject", projectService.showSubProject(subProjectID));
            model.addAttribute("tasks",projectService.getAllTasksInSpecificSubProject(subProjectID));
            model.addAttribute("employeeID",employeeID);
            return "showSubProjectNotMgr"; //Manager = false
        }

    }

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
    @GetMapping("/{projectID}/{subProjectID}/create-task")
    public String createTask(@PathVariable int employeeID, @PathVariable int projectID, @PathVariable int subProjectID, Model model) throws SQLException {
        if(projectService.isManager(employeeID)) {
            model.addAttribute("employeeID", employeeID);
            model.addAttribute("projectID", projectID);
            model.addAttribute("subProjectID", subProjectID);
            model.addAttribute("nonManagerEmployees", projectService.findNonManagerEmployees());
            model.addAttribute("tasks", projectService.getAllTasksInSpecificSubProject(subProjectID));
            model.addAttribute("nonManagerRoles", projectService.getNonManagerRoles());
            model.addAttribute("statusobjects", projectService.fetchAllStatus());
            return "createTask";
        } else {
            return "redirect:/user/{employeeID}";
        }

    }

    @PostMapping("/{projectID}/{subProjectID}/save-task")
    public String saveTask(
            @PathVariable int employeeID,
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

        return "redirect:/user/"+employeeID+"/"+projectID+"/"+subProjectID;
    }


    /*
    ###########---READ---###########
     */
    @GetMapping("/{projectID}/{subProjectID}/{taskID}")
    public String showTask(@PathVariable int employeeID, @PathVariable int projectID, @PathVariable int subProjectID, @PathVariable int taskID, Model model) throws SQLException {
        Task task = projectService.getTaskByID(taskID);

        model.addAttribute("task", task);
        model.addAttribute("employeeID", employeeID);
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjectID", subProjectID);
        model.addAttribute("taskID", taskID);
        model.addAttribute("employeeID",employeeID);
        model.addAttribute("nonManagerEmployees", projectService.findNonManagerEmployees());
        model.addAttribute("tasks", projectService.getAllTasksInSpecificSubProject(subProjectID));
        model.addAttribute("nonManagerRoles", projectService.getNonManagerRoles());
        model.addAttribute("statusobjects", projectService.fetchAllStatus());
        model.addAttribute("isManager", projectService.isManager(employeeID));
        return "showTask";
    }
    /*
    ###########---UPDATE---###########
     */
    @GetMapping("/{projectID}/{subProjectID}/{taskID}/edit-task")
    public String editTask(@PathVariable int employeeID, @PathVariable int projectID, @PathVariable int subProjectID, @PathVariable int taskID, Model model) throws SQLException {
        Task task = projectService.getTaskByID(taskID);
        if (task == null) {
            throw new IllegalArgumentException("Task with ID " + taskID + " not found");
        }
        model.addAttribute("employeeID", employeeID);
        model.addAttribute("taskID", taskID);
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjectID", subProjectID);
        model.addAttribute("task", task);
        model.addAttribute("nonManagerEmployees", projectService.findNonManagerEmployees());
        model.addAttribute("tasks", projectService.getAllTasksInSpecificSubProject(subProjectID));
        model.addAttribute("nonManagerRoles", projectService.getNonManagerRoles());
        model.addAttribute("statusobjects", projectService.fetchAllStatus());
        return "updateTask";
    }

    @PostMapping("/{projectID}/{subProjectID}/{taskID}/update-task")
    public String updateTask(
            @PathVariable int employeeID,
            @PathVariable int projectID,
            @PathVariable int subProjectID,
            @PathVariable int taskID,
            @RequestParam String taskTitle,
            @RequestParam(required = false) String taskDescription,
            @RequestParam(required = false) Integer assignedEmployee,
            @RequestParam(required = false) Double estimatedTime,
            @RequestParam int status,
            @RequestParam(required = false) Date plannedStartDate,
            @RequestParam(required = false) Integer dependingOnTask,
            @RequestParam(required = false) Integer requiredRole) throws SQLException {

        Task updatedTask = new Task(taskTitle, taskDescription, assignedEmployee, estimatedTime, plannedStartDate, dependingOnTask, requiredRole, subProjectID, status);
        updatedTask.setTaskID(taskID);
        projectService.updateTask(updatedTask);

        return "redirect:/user/"+employeeID+"/"+projectID+"/"+subProjectID+"/"+taskID;
    }

    /*
    ###########---DELETE---###########
    */
    @PostMapping("/{projectID}/{subProjectID}/{taskID}/delete-task")
    public String deleteTask(@PathVariable int employeeID, @PathVariable int projectID, @PathVariable int subProjectID, @PathVariable int taskID, RedirectAttributes redirectAttributes) throws SQLException {
        projectService.deleteTask(taskID);
        redirectAttributes.addAttribute("employeeID", employeeID);
        redirectAttributes.addAttribute("projectID", projectID);
        redirectAttributes.addAttribute("subProjectID",subProjectID);
        return "redirect:/user/{employeeID}/{projectID}/{subProjectID}";
    }

    /*
    ################################
    #             Other            #
    ################################
    */

    @GetMapping("/{projectID}/{subProjectID}/{taskID}/submit-hours")
    public String submitHours(@PathVariable int projectID,
                                      @PathVariable int subProjectID,
                                      @PathVariable int taskID,
                                      @PathVariable int employeeID,
                                      Model model) {

        model.addAttribute("employeeID", employeeID);
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjectID", subProjectID);
        model.addAttribute("taskID", taskID);

        return "submitHours";
    }

    @PostMapping("/{projectID}/{subProjectID}/{taskID}/save-submit-hours")
    public String saveSubmitHours(@PathVariable int projectID,
                              @PathVariable int subProjectID,
                              @PathVariable int taskID,
                              @PathVariable int employeeID,
                              @RequestParam double hours,
                              Model model) throws SQLException {

        projectService.submitHours(taskID, hours);

        Task task = projectService.getTaskByID(taskID);
        model.addAttribute("task", task);
        model.addAttribute("employeeID", employeeID);
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjectID", subProjectID);
        model.addAttribute("taskID", taskID);

        return "redirect:/user/{employeeID}/{projectID}/{subProjectID}/{taskID}";
    }



}