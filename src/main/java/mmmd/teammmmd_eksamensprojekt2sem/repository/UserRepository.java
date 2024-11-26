package mmmd.teammmmd_eksamensprojekt2sem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {

    private final Connection dbConnection;

    @Autowired
    public UserRepository(ConnectionManager connectionManager) throws SQLException {
        this.dbConnection = connectionManager.getConnection();
    }

    //LOGIN METHODS - CREATED BY: Mette,

    public boolean validateLogin(String username, String password) throws SQLException {
        boolean result = false;
        String SQL = "SELECT password FROM employee WHERE username =?";

        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    result = dbPassword.equals(password);
                }
            }
        }
        return result;
    }

    public int getEmployeeIdFromDB(String username) throws SQLException {
        int result = 0;
        String SQL = "SELECT employeeID FROM employee WHERE username =?";

        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("employeeID");
                }
            }
        }
        return result;
    }

    //Muligvis kan nedenstående metode erstattes af alm getter metode fra model senere?
    public boolean getIsEmployeeManagerInfoFromDB(int employeeID) throws SQLException {
        boolean result = false;
        String SQL = "SELECT isManager FROM employeerole INNER JOIN employee ON employee.role = employeerole.roleid WHERE employeeID =?";

        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1,employeeID);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    result = rs.getBoolean("isManager");
                }
            }
        }
        return result;
    }


}
