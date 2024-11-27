package mmmd.teammmmd_eksamensprojekt2sem.repository;

import mmmd.teammmmd_eksamensprojekt2sem.model.SubProject;
import mmmd.teammmmd_eksamensprojekt2sem.model.Subproject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ProjectRepository {

    private final Connection dbConnection;

    @Autowired
    public ProjectRepository(ConnectionManager connectionManager) throws SQLException {
        this.dbConnection = connectionManager.getConnection();
    }

    //*******CRUD --- SUBPROJECT******//
    public void createSubProject(String subprojectTitle, String subprojectDescription, int projectID, int statusID) {
        String sqlInsertSubproject = "INSERT INTO Subproject (subProjectTitle, subProjectDescription, projectID, statusID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(sqlInsertSubproject, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, subprojectTitle);
            ps.setString(2, subprojectDescription);
            ps.setInt(3, projectID);
            ps.setInt(4, statusID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<SubProject> showListOfSpecificSubProject(int subProjectID) {
        List<SubProject> listOfSubProjects = new ArrayList<>();

        String SQL = "SELECT subProjectTitle, subProjectDescription, projectID, statusID FROM subProject WHERE subProjectID = ?";

        try(PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1, subProjectID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String subProjectTitle = rs.getString("subProjectTitle");
                String subProjectdescription = rs.getString("subProjectDescription");
                int projectID = rs.getInt("projectID");
                int statusID = rs.getInt("statusID");
                listOfSubProjects.add(new SubProject(subProjectTitle, subProjectdescription, projectID, statusID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfSubProjects;
    }

    public void updateSubProject() {
        //TODO:
    }

 public void deleteSubproject(int subprojectID) {
        String SQL = "DELETE FROM SubProject WHERE subProjectID = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1, subprojectID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
