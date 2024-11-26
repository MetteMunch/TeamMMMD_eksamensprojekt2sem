package mmmd.teammmmd_eksamensprojekt2sem.service;

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

    public int getEmployeeIDFromDB(String username) throws SQLException {
        return userRepository.getEmployeeIdFromDB(username);
    }

    public boolean getIsEmployeeManagerInfoFromDB(int employeeID) throws SQLException {
        return userRepository.getIsEmployeeManagerInfoFromDB(employeeID);
    }
}
