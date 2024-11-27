package mmmd.teammmmd_eksamensprojekt2sem.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


@Repository
public class ProjectRepository {

    private final Connection dbConnection;

    @Autowired
    public ProjectRepository(ConnectionManager connectionManager) throws SQLException {
        this.dbConnection = connectionManager.getConnection();
    }

    public void createSubproject(int subprojectID, String subprojectTitle, String subprojectDescription) {
        String sqlInsertSubproject = "INSERT INTO Subproject (subprojectID, subprojectTitle, subprojectDescription) VALUES (?, ?, ?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(sqlInsertSubproject, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, subprojectID);
            ps.setString(2, subprojectTitle);
            ps.setString(3, subprojectDescription);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
