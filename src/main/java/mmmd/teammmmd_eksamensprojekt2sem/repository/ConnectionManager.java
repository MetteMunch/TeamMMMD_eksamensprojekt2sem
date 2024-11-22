package mmmd.teammmmd_eksamensprojekt2sem.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Component
public class ConnectionManager {
    private Connection connection;
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUser;
    @Value("${spring.datasource.password}")
    private String dbPassword;
    public ConnectionManager() {
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        }
        return connection;
    }
}
