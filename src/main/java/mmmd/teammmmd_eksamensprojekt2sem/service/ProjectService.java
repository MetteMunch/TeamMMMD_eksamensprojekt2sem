package mmmd.teammmmd_eksamensprojekt2sem.service;
import mmmd.teammmmd_eksamensprojekt2sem.model.*;
import mmmd.teammmmd_eksamensprojekt2sem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    /*
    #####################################
    #           Customer Methods        #
    #####################################
     */
    public List<Customer> getListOfCurrentCustomers() {
        return projectRepository.getListOfCurrentCustomers();
    }
    public void createCustomer(Customer customer) {
        projectRepository.createCustomer(customer);
    }

    /*
    #####################################
    #           CRUD Project            #
    #####################################
     */
    public void createProject(Project project) {
        projectRepository.createProject(project);
    }
    public List<Project> showAllProjects() {
        return projectRepository.showAllProjects();
    }
    public void updateProject(Project project) {
        projectRepository.updateProject(project);
    }
    public void deleteProject(Project project) throws SQLException {
        projectRepository.deleteProject(project);
    }

    /*
    ###########---LOOKUP METHODS---###########
     */
    public Project fetchSpecificProject(String projectTitle) {
        return projectRepository.fetchSpecificProject(projectTitle);
    }

    public List<Status> fetchAllStatus() {
        return projectRepository.fetchAllStatus();
    }
    public boolean checkIfProjectNameAlreadyExists(String projectTitle) {
        return projectRepository.checkIfProjectNameAlreadyExists(projectTitle);
    }
    public void setProjectID(Project project) {
        projectRepository.setProjectID(project);
    }
    /*
    ###########---EMPLOYEE METHODS---###########
     */
    public List<Employee> findPMEmployees() {
        return projectRepository.findPMEmployees();
    }
    public List<Employee> findBCEmployees() {
        return projectRepository.findBCEmployees();
    }

    /*
    ##################################
    #           CRUD Task            #
    ##################################
     */
    public void createTask(int projectID, int subProjectID, Task task) throws SQLException {
        projectRepository.createTask(projectID, subProjectID, task);
    }

    public List<Task> getAllTasksInSpecificSubProject(int subProjectID) throws SQLException {
        return projectRepository.getAllTasksInSpecificSubProject(subProjectID);
    }

    public void updateTask(Task task) throws SQLException {
        projectRepository.updateTask(task);
    }

    public void deleteTask(int taskID) throws SQLException {
        projectRepository.deleteTask(taskID);
    }

    /*
    ###########---Helper Methods---###########
     */
    public List<EmployeeRole> getNonManagerRoles() throws SQLException {
        return projectRepository.getNonManagerRoles();
    }

    public List<Employee> findNonManagerEmployees() {
        return projectRepository.findNonManagerEmployees();
    }
}