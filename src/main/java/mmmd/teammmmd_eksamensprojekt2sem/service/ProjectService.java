package mmmd.teammmmd_eksamensprojekt2sem.service;
import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Status;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import mmmd.teammmmd_eksamensprojekt2sem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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
        /*
        Daniel - DanielJensenKEA
         */
        projectRepository.createProject(project);
    }
    public List<Project> showAllProjects() {
        /*
        Daniel - DanielJensenKEA
         */
        return projectRepository.showAllProjects();
    }
    public void updateProject(Project project) {
        /*
        Daniel - DanielJensenKEA
         */
        projectRepository.updateProject(project);
    }
    public void deleteProject(Project project) throws SQLException {
        projectRepository.deleteProject(project);
    }

    /*
    ###########---LOOKUP METHODS---###########
     */
    public Project fetchSpecificProject(String projectTitle) {
        /*
        Daniel - DanielJensenKEA
         */
        return projectRepository.fetchSpecificProject(projectTitle);
    }

    public List<Status> fetchAllStatus() {
        /*
        Daniel - DanielJensenKEA
         */
        return projectRepository.fetchAllStatus();
    }
    public boolean checkIfProjectNameAlreadyExists(String projectTitle) {
        /*
        Daniel - DanielJensenKEA
         */
        return projectRepository.checkIfProjectNameAlreadyExists(projectTitle);
    }
    public void setProjectID(Project project) {
        /*
        Daniel - DanielJensenKEA
         */
        projectRepository.setProjectID(project);
    }
    /*
    ###########---EMPLOYEE METHODS---###########
     */
    public List<Employee> findPMEmployees() {
        /*
        Daniel - DanielJensenKEA
         */
        return projectRepository.findPMEmployees();
    }
    public List<Employee> findBCEmployees() {
        /*
        Daniel - DanielJensenKEA
         */
        return projectRepository.findBCEmployees();
    }
}
