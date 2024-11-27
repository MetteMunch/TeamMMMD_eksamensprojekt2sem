package mmmd.teammmmd_eksamensprojekt2sem.service;

import mmmd.teammmmd_eksamensprojekt2sem.model.SubProject;
import mmmd.teammmmd_eksamensprojekt2sem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    //*******CRUD --- SUBPROJECT******//
    public void createSubproject(String subprojectTitle, String subprojectDescription, int projectID, int statusID) {
        projectRepository.createSubProject(subprojectTitle, subprojectDescription, projectID, statusID);
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

}
