package mmmd.teammmmd_eksamensprojekt2sem.repository;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
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
    public List<Customer> getListOfCurrentCustomers() {
        String sql = "SELECT customerID, companyName, repName FROM customer";
        List<Customer> customersToReturn = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    int customerID = rs.getInt(1);
                    String companyName = rs.getString(2);
                    String repName = rs.getString(3);
                    Customer customer = new Customer(companyName, repName);
                    customer.setCustomerID(customerID);
                    customersToReturn.add(customer);
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return customersToReturn;
    }

    public void createCustomer(Customer customer) {
        String sql="INSERT INTO customer(companyName, repName) VALUES(?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getCompanyName());
            ps.setString(2, customer.getRepName());
            ps.executeUpdate();

            customer.setCustomerID(lookUpCustomerIDFromDB(customer));

        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public int lookUpCustomerIDFromDB(Customer customer) {
        String sql="SELECT customerID FROM customer WHERE companyName=? AND repName=?";
        int customerIDFromDB = -1;
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getCompanyName());
            ps.setString(2, customer.getRepName());
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customerIDFromDB = rs.getInt(1);
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }

        if (customerIDFromDB != -1) {
            return customerIDFromDB;
        } else {
            throw new IllegalArgumentException("No valid customer with the following company name: "+customer.getCompanyName()+" and rep. name: "+customer.getRepName()+".");
        }
    }


}
