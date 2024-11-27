package mmmd.teammmmd_eksamensprojekt2sem.service;
import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.model.Status;
import mmmd.teammmmd_eksamensprojekt2sem.model.Task;
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
    #           CRUD Task            #
    #####################################
     */
    public void createTask(Task task) throws SQLException {
        projectRepository.createTask(task);
    }

    /*
    ###########---EMPLOYEE METHODS---###########
     */
    public List<Employee> findNonManagerEmployees() {
        return projectRepository.findNonManagerEmployees();
    }
}