package mmmd.teammmmd_eksamensprojekt2sem.repository;
import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class ProjectRepository {
    private ConnectionManager connectionManager;
    private Connection connection;

    @Autowired
    public ProjectRepository(ConnectionManager connectionManager) throws SQLException {
        this.connectionManager = connectionManager;
        connection = connectionManager.getConnection();
    }

    /*
    #####################################
    #           CRUD Project            #
    #####################################
     */
    public void createProject(Project project) {
        /*
        Daniel - DanielJensenKEA
         */
        String sql = "INSERT INTO project(projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status)" +
                "VALUES(?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, project.getProjectTitle());
            ps.setString(2, project.getProjectDescription());
            ps.setInt(3,project.getCustomer());
            ps.setDate(4, project.getOrderDate());
            ps.setDate(5, project.getDeliveryDate());
            ps.setString(6, project.getLinkAgreement());
            ps.setInt(7, project.getCompanyRep());
            ps.setInt(8, project.getStatus());

            ps.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public void setProjectID(Project project) {
        /*
        Daniel - DanielJensenKEA
         */
        String sql ="SELECT projectID FROM project WHERE projectTitle=? AND orderDate=?";
        int projectIDFromDB = -1;

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, project.getProjectTitle());
            ps.setDate(2, project.getOrderDate());

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    projectIDFromDB = rs.getInt(1);
                    project.setID(projectIDFromDB);
                } else {
                    throw new IllegalArgumentException("No project found with title: "+project.getProjectTitle()+" and order date: "+project.getOrderDate()+". PROJECT REPOSITORY LINE 45.");
                }
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }
    }


}
