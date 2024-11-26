package mmmd.teammmmd_eksamensprojekt2sem.service;
import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Status;
import mmmd.teammmmd_eksamensprojekt2sem.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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

    public List<Status> fetchAllStatus() {
        return projectRepository.fetchAllStatus();
    }
    public void setProjectID(Project project) {
        projectRepository.setProjectID(project);
    }
    public List<Employee> findPMEmployees() {
        return projectRepository.findPMEmployees();
    }
    public List<Employee> findBCEmployees() {
        return projectRepository.findBCEmployees();
    }
}
