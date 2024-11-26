package mmmd.teammmmd_eksamensprojekt2sem.repository;

import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ProjectRepository {

    private final Connection dbConnection;

    @Autowired
    public ProjectRepository(ConnectionManager connectionManager) throws SQLException {
        this.dbConnection = connectionManager.getConnection();
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
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, project.getProjectTitle());
            ps.setString(2, project.getProjectDescription());
            ps.setInt(3, project.getCustomer());
            ps.setDate(4, project.getOrderDate());
            ps.setDate(5, project.getDeliveryDate());
            ps.setString(6, project.getLinkAgreement());
            ps.setInt(7, project.getCompanyRep());
            ps.setInt(8, project.getStatus());

            ps.executeUpdate();
            System.out.println("Successfully created project: " + project.getProjectTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
        #####################################
        #           Helper Methods          #
        #####################################
     */
    public List<Status> fetchAllStatus() {
        String sql ="SELECT statusID, status FROM status";
        List<Status> statusFromDB = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Status status = new Status(rs.getInt(1), rs.getString(2));
                    statusFromDB.add(status);
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return statusFromDB;
    }
    public List<Employee> findPMEmployees() {
        /*
        Daniel - DanielJensenKEA
         */
        //Project Managers(role code 1)
        List<Employee> employeesFromDBPM = new ArrayList<>();
        String sql = "SELECT employeeID, fullName, username, password, role FROM employee WHERE role=1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getInt(5));
                    employeesFromDBPM.add(emp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeesFromDBPM;
    }

    public List<Employee> findBCEmployees() {
        /*
        Daniel - DanielJensenKEA
         */
        //Business Consultants(role code 2)
        List<Employee> employeesFromDBBC = new ArrayList<>();
        String sql = "SELECT employeeID, fullName, username, password, role FROM employee WHERE role=2";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getInt(5));
                    employeesFromDBBC.add(emp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeesFromDBBC;
    }

    public void setProjectID(Project project) {
        /*
        Daniel - DanielJensenKEA
         */
        String sql = "SELECT projectID FROM project WHERE projectTitle=? AND orderDate=?";
        int projectIDFromDB = -1;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, project.getProjectTitle());
            ps.setDate(2, project.getOrderDate());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    projectIDFromDB = rs.getInt(1);
                    project.setID(projectIDFromDB);
                    System.out.println("Successfully created project: " + project.getProjectTitle() + " with ID: " + project.getID());
                } else {
                    throw new IllegalArgumentException("No project found with title: " + project.getProjectTitle() + " and order date: " + project.getOrderDate() + ". PROJECT REPOSITORY LINE 45.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
