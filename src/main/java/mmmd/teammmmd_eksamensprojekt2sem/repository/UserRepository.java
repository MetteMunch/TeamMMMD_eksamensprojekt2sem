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

    //Fiktiv metode til test af forbindelse til DB plus for at teste test
    public int testForespørgselTilDB(String role) {
        int result = 0;
        String SQL = "SELECT fullName FROM employee INNER JOIN employeerole ON employee.role = employeerole.roleID WHERE employeerole.roleTitle = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setString(1,role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result ++;
                    String fullName = rs.getString("fullName");
                    System.out.println(fullName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
         return result;

    }
}
