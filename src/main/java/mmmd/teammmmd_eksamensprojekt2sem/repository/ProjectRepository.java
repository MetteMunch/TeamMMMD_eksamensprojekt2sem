package mmmd.teammmmd_eksamensprojekt2sem.repository;
import mmmd.teammmmd_eksamensprojekt2sem.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
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


    // CRUD TASK
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

            preparedStatement.executeUpdate(); // Execute the insert
        }
    }

    public List<Task> showListOfTasks() {

    }


}
