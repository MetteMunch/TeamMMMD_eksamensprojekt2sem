package mmmd.teammmmd_eksamensprojekt2sem.service;

import mmmd.teammmmd_eksamensprojekt2sem.model.SubProject;
import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Status;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import mmmd.teammmmd_eksamensprojekt2sem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.sql.SQLException;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    //*******CRUD --- SUBPROJECT******//
    public void createSubproject(SubProject subProject) {
        projectRepository.createSubProject(subProject);
    }

    public boolean checkIfSubProjectNameAlreadyExists(int projectID, String subProjectTitle) {
        return projectRepository.checkIfSubProjectNameAlreadyExists(subProjectTitle);
    }

    public List<SubProject> showListOfSpecificSubProject(int subProjectID) {
        return projectRepository.showListOfSpecificSubProject(subProjectID);
    }

    public void updateSubProject() {
        //TODO:
    }

    public void deleteSubProject(int subProjectID) {
        projectRepository.deleteSubproject(subProjectID);
    }

    public List<SubProject> showAllSubProjects() {
        return projectRepository.showAllSubProjects();
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

}
