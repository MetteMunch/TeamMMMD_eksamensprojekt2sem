package mmmd.teammmmd_eksamensprojekt2sem.repository;
import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.model.Status;
import mmmd.teammmmd_eksamensprojekt2sem.model.Task;
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
    ##################################
    #           CRUD Task            #
    ##################################
     */
    public void createTask(Task task) throws SQLException {
        String sql = "INSERT INTO Task (taskTitle, taskDescription, assignedEmployee, estimatedTime, actualTime, plannedStartDate, dependingOnTask, requiredRole, subProjectID, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (var preparedStatement = dbConnection.prepareStatement(sql)) {
            preparedStatement.setString(1, task.getTaskTitle());
            preparedStatement.setString(2, task.getTaskDescription());
            preparedStatement.setInt(3, task.getAssignedEmployee());
            preparedStatement.setDouble(4, task.getEstimatedTime());
            preparedStatement.setDouble(5, task.getActualTime());
            preparedStatement.setDate(6, task.getPlannedStartDate() != null ? new java.sql.Date(task.getPlannedStartDate().getTime()) : null);
            preparedStatement.setInt(7, task.getDependingOnTask());
            preparedStatement.setInt(8, task.getRequiredRole());
            preparedStatement.setInt(9, task.getSubProjectID());
            preparedStatement.setInt(10, task.getStatus().getStatusID());

            preparedStatement.executeUpdate();
        }
    }

    /*
    ##################################
    #           READ Task            #
    ##################################
     */
    public List<Task> showAllTasksInSpecificSubproject(int subProjectID) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT taskID, taskTitle, taskDescription, assignedEmployee, estimatedTime, " +
                "actualTime, plannedStartDate, dependingOnTask, requiredRole, subProjectID, status " +
                "FROM Task WHERE subProjectID = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, subProjectID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Status taskStatus = findStatusByID(rs.getInt("statusID"));

                    Task task = new Task(
                            rs.getInt("taskID"),
                            rs.getString("taskTitle"),
                            rs.getString("taskDescription"),
                            rs.getInt("assignedEmployee"),
                            rs.getDouble("estimatedTime"),
                            rs.getDouble("actualTime"),
                            rs.getDate("plannedStartDate"),
                            rs.getInt("dependingOnTask"),
                            rs.getInt("requiredRole"),
                            rs.getInt("subProjectID"),
                            taskStatus
                    );
                    tasks.add(task);
                }
            }
            return tasks;
        }
    }

    /*
        #####################################
        #          Helper Methods         #
        #####################################
     */
    public Status findStatusByID(int statusID) throws SQLException {
        String sql = "SELECT statusID, status FROM Status WHERE statusID = ?";
        Status status = null;

        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, statusID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    status = new Status(rs.getInt("statusID"), rs.getString("status"));
                }
            }
        }
        return status;
    }



    /*
        #####################################
        #          Employee Methods         #
        #####################################
     */
    public List<Employee> findNonManagerEmployees() {
        List<Employee> nonManagerEmployees = new ArrayList<>();
        String sql = "SELECT e.employeeID, e.fullName " +
                "FROM Employee e " +
                "JOIN EmployeeRole r ON e.role = r.roleID " +
                "WHERE r.isManager = false";

        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee(rs.getInt(1), rs.getString(2), null, null, 0);
                    nonManagerEmployees.add(emp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nonManagerEmployees;
    }

}
