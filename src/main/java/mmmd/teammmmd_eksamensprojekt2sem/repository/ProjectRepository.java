package mmmd.teammmmd_eksamensprojekt2sem.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
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


}
