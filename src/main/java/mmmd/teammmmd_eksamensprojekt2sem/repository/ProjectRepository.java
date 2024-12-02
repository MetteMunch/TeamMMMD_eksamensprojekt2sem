package mmmd.teammmmd_eksamensprojekt2sem.repository;

import mmmd.teammmmd_eksamensprojekt2sem.model.SubProject;

import mmmd.teammmmd_eksamensprojekt2sem.model.Employee;
import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Status;
import mmmd.teammmmd_eksamensprojekt2sem.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    //*******CRUD --- SUBPROJECT******//
    public void createSubProject(SubProject subProject) {
        String sqlInsertSubproject = "INSERT INTO Subproject (subProjectTitle, subProjectDescription, projectID, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(sqlInsertSubproject, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, subProject.getSubProjectTitle());
            ps.setString(2, subProject.getSubProjectDescription());
            ps.setInt(3, subProject.getProjectID());
            ps.setInt(4, subProject.getStatusID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfSubProjectNameAlreadyExists(String subProjectTitle) {
        String sql = "SELECT subProjectTitle FROM subProject WHERE LOWER(subProjectTitle) = LOWER(?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, subProjectTitle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (subProjectTitle.equals(rs.getString(1))) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<SubProject> showAllSubProjects() { //READ
        String sql = "SELECT subProjectTitle, subProjectDescription, projectID, status.status FROM subProject\n" +
                "INNER JOIN Status ON subProject.status = status.statusID";
        List<SubProject> listOfSubProjects = new ArrayList<>();
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SubProject subProject = new SubProject(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4));

                    listOfSubProjects.add(subProject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfSubProjects;
    }

    public List<SubProject> showListOfSpecificSubProject(int projectID) {
        List<SubProject> listOfSubProjects = new ArrayList<>();

        String SQL = "SELECT subProjectTitle, subProjectDescription, projectID, Status.status FROM subProject\n" +
                "INNER JOIN status ON Status.statusID = SubProject.status WHERE projectID = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1, projectID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String subProjectTitle = rs.getString("subProjectTitle");
                String subProjectdescription = rs.getString("subProjectDescription");
                int projectNameID = rs.getInt("projectID");
                int statusID = rs.getInt("statusID");
                listOfSubProjects.add(new SubProject(subProjectTitle, subProjectdescription, projectID, statusID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfSubProjects;
    }

    public void updateSubProject() {
        //TODO:
    }

    public void deleteSubproject(int subprojectID) {
        String SQL = "DELETE FROM SubProject WHERE subProjectID = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1, subprojectID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public Customer fetchInternalProjectCustomer() {
        String fetchSql ="SELECT customerID, companyName, repName FROM customer WHERE companyName=?";
        String insertSQL = "INSERT INTO customer(companyName, repName) VALUES(?,?)";
        String internalProject = "Internal Project";
        String internalRep = "Internal";

        try(PreparedStatement fetchPs = dbConnection.prepareStatement(fetchSql)) { //Vi henter Internal Project som Customer, hvis det eksisterer i databasen
            fetchPs.setString(1, internalProject);

            try(ResultSet rs = fetchPs.executeQuery()) {
                if (rs.next()) { //Internal Project(IP) eksisterer i databasen og vi laver det til Customer objekt og sender videre.
                    Customer internalCus = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3));
                    return internalCus;
                }
            }

            try(PreparedStatement insertPs = dbConnection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) { //IP findes ikke og vi inserter det i databasen.
                insertPs.setString(1, internalProject);
                insertPs.setString(2, internalRep);
                int affectedRows = insertPs.executeUpdate();

                if (affectedRows > 0 ) {
                    try(ResultSet rs = insertPs.getGeneratedKeys()) {
                        if (rs.next()) {
                            int customerID = rs.getInt(1);
                            return new Customer(customerID,internalProject, internalRep );
                        }
                    }
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
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
