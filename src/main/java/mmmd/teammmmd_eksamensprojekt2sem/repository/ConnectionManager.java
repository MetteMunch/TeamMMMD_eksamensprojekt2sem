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

    public synchronized Connection getConnection() throws SQLException {
        /*
        Synchronized: Kun én thread kan tilgå metoden af gangen for at sikre korrekt resultat ved delte resurser.
        thread: Mindste enhed af udførelse i et program. Kan køre i samspil med andre threads samtidigt.
         */
        if (connection == null) {
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        }
        return connection;
    }
}
