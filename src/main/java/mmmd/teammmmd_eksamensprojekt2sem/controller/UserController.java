package mmmd.teammmmd_eksamensprojekt2sem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mmmd.teammmmd_eksamensprojekt2sem.service.ProjectService;
import mmmd.teammmmd_eksamensprojekt2sem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RequestMapping("/user")
@Controller
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;
    private HttpSession session;


    public UserController(UserService userService, ProjectService projectService, HttpSession session) {
        this.userService = userService;
        this.projectService = projectService;
        this.session = session;
    }

    /*
    ###########---LOGIN---###########
     */

    @GetMapping("/loginpage")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        System.out.println("her er vi ende i endpoint get /loginpage");
        if (error != null) {
            model.addAttribute("errorMessage", "Either username or password do not match. Please try again or contact Admin");
        }
        return "login";

    }

    @PostMapping("/loginvalidation")
    public String loginValidation(HttpServletRequest request, @RequestParam String username, @RequestParam String password) throws SQLException {
        System.out.println("her bliver endpoint loginvalidation kaldt");
        String returnStatement;

        if (userService.validateLogin(username, password)) {
            session = request.getSession();
            int employeeID = userService.getEmployeeIDFromDB(username);
            session.setAttribute("employeeID", employeeID); // Brugerens unikke employeeID gemmes i sessionen, så vi kan sikre, at
            //brugeren ikke tilgår Endpoints, som tilhører andre brugere ved at skrive dette direkte i url

            if (userService.getIsEmployeeManagerInfoFromDB(employeeID)) {
                System.out.println("test udskrift - brugeren er manager");
                returnStatement = "redirect:/user/projectmanager/" + employeeID; //Jeg kender ikke korrekt sti/Get Endpoint endnu

            } else {
                returnStatement = "redirect:/user/employee/" + employeeID;
            }
        } else {
            System.out.println("test udskrift - brugeren kunne ikke valideres");
            returnStatement = "redirect:/user/loginpage?error=true";
        }
        return returnStatement;
    }

    /*
    ###########---EMPLOYEE---###########
     */

    @GetMapping("/employee/{employeeID}")
    public String showEmployeeDashboard(@PathVariable int employeeID, Model model) {
        String redirect = userService.redirectUserLoginAttributes(session, employeeID);
        if (redirect != null) {
            return redirect;
        }
        /*
        redirectUserLoginAttributes() returnerer en string(html/endpoint. Hvis if-blokken i denne metode
        ikke opfyldes, så returnerer metoden null og vi fortsætter /{userID}s metodeflow.
        Se redirectUserLoginAttributes() dokumentation i koden.
         */

        model.addAttribute("projects", projectService.showAllProjectsSpecificEmployee(employeeID));
        model.addAttribute("employee", userService.getEmployee(employeeID));

        return "employeeDashboard";
    }





    /*
    ###########---MANAGER---###########
     */


}

