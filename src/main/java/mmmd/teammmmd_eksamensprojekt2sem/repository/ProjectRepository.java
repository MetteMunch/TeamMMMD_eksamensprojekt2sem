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
    public void createTask() {

    }

    public List<Task> showListOfTasks() {

    }


}
