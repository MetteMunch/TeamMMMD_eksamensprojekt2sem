package mmmd.teammmmd_eksamensprojekt2sem.service;

import jakarta.servlet.http.HttpSession;
import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validateLogin(String username, String password) throws SQLException {
        return userRepository.validateLogin(username, password);
    }

    public Employee getEmployee(int employeeID) {
        return userRepository.getEmployee(employeeID);
    }

    public int getEmployeeIDFromDB(String username) throws SQLException {
        return userRepository.getEmployeeIdFromDB(username);
    }

    public boolean getIsEmployeeManagerInfoFromDB(int employeeID) throws SQLException {
        return userRepository.getIsEmployeeManagerInfoFromDB(employeeID);
    }

    //////HJÆLPEMETODE TIL CONTROLLEREN////////

    public String redirectUserLoginAttributes(HttpSession session, int employeeID) throws SQLException {
        Integer sessionEmpID = (Integer) session.getAttribute("employeeID"); //empID er i metoden loginValidation
        //blevet gemt som et objekt på sessionen, derfor skal attributten omdannes til Integer, for at sammenligne
        //med meployeeID

        if (sessionEmpID == null) {
            //Hvis brugeren ikke er logget ind ligger der ikke et ID gemt på session,
            // hvorfor brugeren derfor promptes til at logge ind
            return "redirect:/user/loginpage";
        } else if (!sessionEmpID.equals(employeeID)) {
            /*
            Brugeren prøver at tilgå en anden brugers data.
            De bliver redirected til deres egen side, hvis de er logget ind.
             */
            return "redirect:/user/" + sessionEmpID;
        }
        return null;
    }
}
