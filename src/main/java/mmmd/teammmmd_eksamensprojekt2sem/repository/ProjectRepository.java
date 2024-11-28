package mmmd.teammmmd_eksamensprojekt2sem.repository;

import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Status;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
    #           CRUD Customer           #
    #####################################
     */
    public List<Customer> getListOfCurrentCustomers() {
        String sql = "SELECT customerID, companyName, repName FROM customer";
        List<Customer> customersToReturn = new ArrayList<>();
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int customerID = rs.getInt(1);
                    String companyName = rs.getString(2);
                    String repName = rs.getString(3);
                    Customer customer = new Customer(companyName, repName);
                    customer.setCustomerID(customerID);
                    customersToReturn.add(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersToReturn;
    }

    public void createCustomer(Customer customer) {
        String sql = "INSERT INTO customer(companyName, repName) VALUES(?,?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, customer.getCompanyName());
            ps.setString(2, customer.getRepName());
            ps.executeUpdate();

            customer.setCustomerID(lookUpCustomerIDFromDB(customer));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int lookUpCustomerIDFromDB(Customer customer) {
        String sql = "SELECT customerID FROM customer WHERE companyName=? AND repName=?";
        int customerIDFromDB = -1;
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, customer.getCompanyName());
            ps.setString(2, customer.getRepName());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customerIDFromDB = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (customerIDFromDB != -1) {
            return customerIDFromDB;
        } else {
            throw new IllegalArgumentException("No valid customer with the following company name: " + customer.getCompanyName() + " and rep. name: " + customer.getRepName() + ".");
        }
    }

    /*
    #####################################
    #           CRUD Project            #
    #####################################
     */
    /*
    ###########---CREATE PROJECT---###########
     */
    public void createProject(Project project) {
        String sql = "INSERT INTO project(projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status)" +
                "VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, project.getProjectTitle());
            ps.setString(2, project.getProjectDescription());
            ps.setInt(3, project.getCustomer());
            ps.setDate(4, project.getOrderDate());
            ps.setDate(5, project.getDeliveryDate());
            ps.setString(6, project.getLinkAgreement());
            ps.setInt(7, project.getCompanyRep());
            ps.setInt(8, project.getStatus());

            ps.executeUpdate();
            System.out.println("Successfully created project: " + project.getProjectTitle()); //todo: delete
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    ###########---READ PROJECT---###########
     */
    public List<Project> showAllProjects() { //READ
        String sql = "SELECT projectID, projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status" +
                " FROM project";
        List<Project> listOfProjects = new ArrayList<>();
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                            rs.getDate(5), rs.getDate(6), rs.getString(7), rs.getInt(8), rs.getInt(9));

                    listOfProjects.add(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfProjects;
    }

    public List<Project> showAllProjectsSpecificEmployee(int employeeID) {
        String SQL = "SELECT DISTINCT project.projectID, projectTitle, project.projectDescription, project.customer, customer.companyName, \n" +
                "orderDate, deliveryDate, linkAgreement, project.companyRep, employee.fullName AS companyRepName, project.status, status.status FROM project\n" +
                "INNER JOIN customer ON customer.customerID = project.customer\n" +
                "INNER JOIN status ON status.statusID = project.status\n" +
                "INNER JOIN subproject ON subproject.projectID = project.projectID\n" +
                "INNER JOIN employee ON employee.employeeID = project.companyRep\n" +
                "INNER JOIN task ON task.subProjectID = subproject.subProjectID WHERE task.assignedEmployee =?";

        List<Project> listOfProjectsSpecificEmployee = new ArrayList<>();

        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1, employeeID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectID = rs.getInt(1);
                String projectTitle = rs.getString(2);
                String projectDescription = rs.getString(3);
                int customerID = rs.getInt(4);
                String customerName = rs.getString(5);
                Date orderDate = rs.getDate(6);
                Date agreedDeliveryDate = rs.getDate(7);
                String linkAgreement = rs.getString(8);
                int companyRep = rs.getInt(9);
                String companyRepName = rs.getString(10);
                int projectStatusID = rs.getInt(11);
                String projectStatus = rs.getString(12);
                Project project = new Project(projectID,projectTitle,projectDescription,customerID,orderDate,agreedDeliveryDate,linkAgreement,companyRep,projectStatusID);
                project.setCompanyRepString(companyRepName);
                project.setCustomerNameString(customerName);
                project.setStatusString(projectStatus);

                listOfProjectsSpecificEmployee.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfProjectsSpecificEmployee;
    }

    /*
    ###########---UPDATE PROJECT---###########
     */
    public void updateProject(Project project) {
        String sql = "UPDATE project SET projectTitle=?, projectDescription=?, customer=?, orderDate=?, " +
                "deliveryDate=?, linkAgreement=?, companyRep=?, status=? WHERE projectID=?";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, project.getProjectTitle());
            ps.setString(2, project.getProjectDescription());
            ps.setInt(3, project.getCustomer());
            ps.setDate(4, project.getOrderDate());
            ps.setDate(5, project.getDeliveryDate());
            ps.setString(6, project.getLinkAgreement());
            ps.setInt(7, project.getCompanyRep());
            ps.setInt(8, project.getStatus());
            ps.setInt(9, project.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    ###########---DELETE PROJECT---###########
     */
    public void deleteProject(Project project) throws SQLException {
        String sql = "DELETE FROM project WHERE projectID=?";
        try {
            dbConnection.setAutoCommit(false);
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setInt(1, project.getID());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No project found with ID: " + project.getID());
                }
            }
            dbConnection.commit();
        } catch (SQLException e) {
            dbConnection.rollback();
            e.printStackTrace();
        } finally {
            dbConnection.setAutoCommit(true);
        }
    }


    /*
       #####################################
       #           Helper Methods          #
       #####################################
    */
    public Project fetchSpecificProject(String projectTitle) {
        String sql = "SELECT projectID, projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status FROM project WHERE projectTitle=?";
        Project project = null;
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, projectTitle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    project = new Project(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                            rs.getDate(5), rs.getDate(6), rs.getString(7), rs.getInt(8), rs.getInt(9));
                    return project;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (project == null) {
            throw new IllegalArgumentException("No project found with title: " + projectTitle);
        }
        return project;
    }

    public boolean checkIfProjectNameAlreadyExists(String projectTitle) {
        String sql = "SELECT projectTitle FROM project WHERE LOWER (projectTitle)=LOWER(?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, projectTitle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (projectTitle.equals(rs.getString(1))) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setProjectID(Project project) {
        String sql = "SELECT projectID FROM project WHERE projectTitle=? AND orderDate=?";
        int projectIDFromDB = -1;

        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
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

    public List<Status> fetchAllStatus() {
        String sql = "SELECT statusID, status FROM status";
        List<Status> statusFromDB = new ArrayList<>();
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Status status = new Status(rs.getInt(1), rs.getString(2));
                    statusFromDB.add(status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statusFromDB;
    }
    /*
        #####################################
        #          Employee Methods         #
        #####################################
     */

    public List<Employee> findPMEmployees() {
        //Project Managers(role code 1)
        List<Employee> employeesFromDBPM = new ArrayList<>();
        String sql = "SELECT employeeID, fullName, username, password, role FROM employee WHERE role=1";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
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
        //Business Consultants(role code 2)
        List<Employee> employeesFromDBBC = new ArrayList<>();
        String sql = "SELECT employeeID, fullName, username, password, role FROM employee WHERE role=2";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
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


}
