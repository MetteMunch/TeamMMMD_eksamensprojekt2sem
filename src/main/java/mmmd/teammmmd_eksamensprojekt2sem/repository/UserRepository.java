package mmmd.teammmmd_eksamensprojekt2sem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    //LOGIN METHODS - USER STORY ISSUE 22 - CREATED BY: Mette,

    public boolean validateLogin(String username, String password) throws SQLException {
        boolean result = false;
        String SQL = "SELECT password FROM employee WHERE username =?";

        try(PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setString(1,username);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    String dbPassword = rs.getString("password");
                    result = dbPassword.equals(password);
                }
            }
        }
        return result;
    }


}
