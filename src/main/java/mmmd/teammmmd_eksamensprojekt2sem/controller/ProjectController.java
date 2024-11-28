package mmmd.teammmmd_eksamensprojekt2sem.controller;

import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/project")
@Controller
public class ProjectController {

    private final ProjectService projectService;


    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{employeeID}/{projectID}/createSubProject")
    public String createSubProject(@PathVariable int employeeID, @PathVariable int projectID, Model model) {
        model.addAttribute("employeeID", employeeID);
        model.addAttribute("projectID", projectID);
        return "createSubProject";
    }

    @PostMapping("/{employeeID}/{projectID}/saveSubProject")
    public String saveSubProject(@PathVariable int employeeID, @PathVariable int projectID, @RequestParam String subProjectTitle, @RequestParam String subProjectDescription, @RequestParam int statusID) {
        projectService.createSubproject(subProjectTitle, subProjectDescription, projectID, statusID);
        return "redirect:/project/" + employeeID + "/" + projectID;
    }

    @PostMapping("/deleteSubProject/{employeeID}/{subProjecwqa<tID}")
    public String deleteSubProject(@PathVariable int employeeID, @PathVariable int subProjectID) {
        projectService.deleteSubProject(subProjectID);
        return "redirect:/project/" + employeeID;
    }
}
