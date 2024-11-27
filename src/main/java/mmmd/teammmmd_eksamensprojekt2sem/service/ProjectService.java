package mmmd.teammmmd_eksamensprojekt2sem.service;
import mmmd.teammmmd_eksamensprojekt2sem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void createSubproject (int subprojectID, String subprojectTitle, String subprojectDescription) {
        projectRepository.createSubproject(subprojectID, subprojectTitle, subprojectDescription);
    }

}
