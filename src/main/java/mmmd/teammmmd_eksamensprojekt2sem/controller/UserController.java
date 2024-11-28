package mmmd.teammmmd_eksamensprojekt2sem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mmmd.teammmmd_eksamensprojekt2sem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@RequestMapping("/user")
@Controller
public class UserController {

    private final UserService userService;
    private HttpSession session;


    public UserController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @GetMapping("/loginpage")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Either username or password do not match. Please try again or contact Admin");
        }
        return "login";

    }
@PostMapping("/loginvalidation")
    public String loginValidation(HttpServletRequest request, @RequestParam String username, @RequestParam String password) throws SQLException {
        String returnStatement;

        if (userService.validateLogin(username,password)) {
            session = request.getSession();
            int employeeID = userService.getEmployeeIDFromDB(username);
            session.setAttribute("employeeID",employeeID); // Brugerens unikke employeeID gemmes i sessionen, så vi kan sikre, at
            //brugeren ikke tilgår Endpoints, som tilhører andre brugere ved at skrive dette direkte i url

            if (userService.getIsEmployeeManagerInfoFromDB(employeeID)) {
                System.out.println("test udskrift - brugeren er manager");
                returnStatement = "redirect:/user/projectmanager/"+employeeID; //Jeg kender ikke korrekt sti/Get Endpoint endnu

            } else {
                System.out.println("test udskrift - brugeren er ikke manager");
                returnStatement = "redirect:/user/employee/"+employeeID; //Jeg kender ikke korrekt sti/Get Endpoint endnu
            }
        } else {
            System.out.println("test udskrift - brugeren kunne ikke valideres");
            returnStatement = "redirect:/user/loginpage?error=true";
        }
        return returnStatement;
    }


}

