package mmmd.teammmmd_eksamensprojekt2sem.repository;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public void createCustomer(Customer customer) {
        String sql="INSERT INTO customer(companyName, repName) VALUES(?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getCompanyName());
            ps.setString(2, customer.getRepName());
            ps.executeUpdate();

        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public int lookUpCustomerIDFromDB(Customer customer) {
        String sql="SELECT customerID FROM customer WHERE companyName=? AND repName=?";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getCompanyName());
            ps.setString(2, customer.getRepName());

        }catch(SQLException e) {
            e.printStackTrace();
        }

    }


}
