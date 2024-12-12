package mmmd.teammmmd_eksamensprojekt2sem.service;

import mmmd.teammmmd_eksamensprojekt2sem.model.*;
import mmmd.teammmmd_eksamensprojekt2sem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /*
    #####################################
    #             Customer              #
    #####################################
     */
    public List<Customer> getListOfCurrentCustomers() {
        return projectRepository.getListOfCurrentCustomers();
    }

    public void createCustomer(Customer customer) {
        projectRepository.createCustomer(customer);
    }

    public int lookUpCustomerIDFromDB(Customer customer) {
        return projectRepository.lookUpCustomerIDFromDB(customer);
    }

    /*
    #####################################
    #             Project               #
    #####################################
    */
    public void createProject(Project project) {
        projectRepository.createProject(project);
    }

    public void updateProject(Project project) {
        projectRepository.updateProject(project);
    }

    public void deleteProject(Project project) throws SQLException {
        projectRepository.deleteProject(project);
    }

    public Project fetchSpecificProject(int projectID) {
        return projectRepository.fetchSpecificProject(projectID);
    }

    public Customer fetchInternalProjectCustomer() {
        return projectRepository.fetchInternalProjectCustomer();
    }

    public List<Status> fetchAllStatus() {
        return projectRepository.fetchAllStatus();
    }

    public boolean checkIfProjectNameAlreadyExists(String projectTitle) {
        return projectRepository.checkIfProjectNameAlreadyExists(projectTitle);
    }

    public int findProjectIDFromDB(Project project) {
       return projectRepository.findProjectIDFromDB(project);
    }

    public List<Project> showAllProjectsSpecificEmployee(int employeeID) {
        return projectRepository.showAllProjectsSpecificEmployee(employeeID);
    }

    public List<Project> showAllProjectSpecificProjectManager(int employeeID) {
        return projectRepository.showAllProjectsSpecificProjectManager(employeeID);
    }

    public void updateProjectsCustomerID(int projectID, int customerID) {
        projectRepository.updateProjectsCustomerID(projectID, customerID);
    }

    /*
      ####################################
                SubProject
      #####################################
     */
    public void createSubproject(SubProject subProject) {
        projectRepository.createSubProject(subProject);
    }

    public boolean checkIfSubProjectNameAlreadyExists(int projectID, String subProjectTitle) {
        return projectRepository.checkIfSubProjectNameAlreadyExists(subProjectTitle, projectID);
    }

    public List<SubProject> showListOfSpecificSubProjects(int projectID) {
        return projectRepository.showListOfSpecificSubProjects(projectID);
    }

    public void updateSubProject(SubProject subProject) {
        projectRepository.updateSubProject(subProject);
    }

    public void deleteSubProject(int subProjectID) {
        projectRepository.deleteSubproject(subProjectID);
    }

    public SubProject showSubProject(int subProjectID) {
        return projectRepository.showSubProject(subProjectID);
    }


    /*
    ##################################
    #              Task              #
    ##################################
     */
    public void createTask(int projectID, int subProjectID, Task task) throws SQLException {
        projectRepository.createTask(projectID, subProjectID, task);
    }

    public List<Task> getAllTasks() {
        return projectRepository.getAllTasks();
    }

    public List<Task> getAllTasksInSpecificSubProject(int subProjectID) {
        return projectRepository.getAllTasksInSpecificSubProject(subProjectID);
    }

    public List<Task> showAllTasksSpecificEmployee(int employeeID) {
        return projectRepository.showAllTasksSpecificEmployee(employeeID);
    }

    public List<Task> showAllTasksSpecificProjectManager(int employeeID) {
        return projectRepository.showAllTasksSpecificProjectManager(employeeID);
    }

    public void updateTask(Task task) throws SQLException {
        projectRepository.updateTask(task);
    }

    public void deleteTask(int taskID) throws SQLException {
        projectRepository.deleteTask(taskID);
    }


    /*
    #####################################
    #             Employee              #
    #####################################
    */

    public List<Employee> findPMEmployees() {
        return projectRepository.findPMEmployees();
    }

    public List<Employee> findBCEmployees() {
        return projectRepository.findBCEmployees();
    }

    public List<EmployeeRole> getNonManagerRoles() throws SQLException {
        return projectRepository.getNonManagerRoles();
    }

    public List<Employee> findNonManagerEmployees() {
        return projectRepository.findNonManagerEmployees();
    }

    /*
    #####################################
    #             Calculations          #
    #####################################
    */

    public List<Task> tasksWithCalculatedEndDateLaterThanProjectDeadline(int employeeID, int projectID) {
       List<Task> tasksWithCalculatedEndDateLaterThanProjectDeadline = new ArrayList<>();

        Project projectToBeChecked = projectRepository.fetchSpecificProject(projectID);

        for(Task task: listOfTasksSpecificProject(employeeID,projectID)) {
            LocalDate startDate = task.getPlannedStartDate().toLocalDate();

            // Beregn arbejdsdage baseret på estimatedTime og 6 timers effektiv arbejdstid pr. dag
            // datatype ændres til int, da vi kun er interesseret i hele dage og oprunding (derfor Math-ceil)
            int estimatedDaysOfWork = (int) Math.ceil(task.getEstimatedTime()/ 6.0);

            // Beregnet slutdato for denne task
            LocalDate taskEndDate = startDate.plus(estimatedDaysOfWork,ChronoUnit.DAYS);

            LocalDate agreedDeliveryDateProject = projectToBeChecked.getDeliveryDate().toLocalDate();

            // Hvis task har slutdato senere end projekts aftalte levering, så tilføjer vi til listen.
            if (taskEndDate.isAfter(agreedDeliveryDateProject)) {
                tasksWithCalculatedEndDateLaterThanProjectDeadline.add(task);
                task.setCalculatedEndDate(Date.valueOf(taskEndDate));
            }
        }
        return tasksWithCalculatedEndDateLaterThanProjectDeadline;
    }



    public List<Task> listOfTasksSpecificProject(int employeeID, int projectID) {

        List<Task> listOfTasksSpecificPM = projectRepository.showAllTasksSpecificProjectManager(employeeID);
        List<Task> listOfTasksSpecificProject = new ArrayList<>();

        for(Task task: listOfTasksSpecificPM) {
            if(task.getProjectID()==projectID) {
                listOfTasksSpecificProject.add(task);
            }
        }
        return listOfTasksSpecificProject;
    }


    /*
    #####################################
    #             Helper Methods        #
    #####################################
    */


    public Task getTaskByID(int taskID) throws SQLException {
        return projectRepository.getTaskByID(taskID);
    }

    public void submitHours(int taskID, double hours) throws SQLException {
        projectRepository.submitHours(taskID, hours);
    }
}