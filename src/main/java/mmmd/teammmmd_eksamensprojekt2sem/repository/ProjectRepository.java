package mmmd.teammmmd_eksamensprojekt2sem.repository;

import mmmd.teammmmd_eksamensprojekt2sem.model.*;
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
    #####################################
    #           CRUD Customer           #
    #####################################
     */
    public List<Customer> getListOfCurrentCustomers() {
        String sql = "SELECT customerID, companyName, repName FROM customer";
        List<Customer> customersToReturn = new ArrayList<>();
        try(PreparedStatement ps = dbConnection.prepareStatement(sql)) {
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
        try(PreparedStatement ps = dbConnection.prepareStatement(sql)) {
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
        try(PreparedStatement ps = dbConnection.prepareStatement(sql)) {
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
        String sql ="SELECT projectID, projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status" +
                " FROM project";
        List<Project> listOfProjects = new ArrayList<>();
        try(PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Project project = new Project(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                            rs.getDate(5), rs.getDate(6), rs.getString(7), rs.getInt(8), rs.getInt(9));

                    listOfProjects.add(project);
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return listOfProjects;
    }
    /*
    ###########---UPDATE PROJECT---###########
     */
    public void updateProject(Project project) {
        String sql="UPDATE project SET projectTitle=?, projectDescription=?, customer=?, orderDate=?, " +
                "deliveryDate=?, linkAgreement=?, companyRep=?, status=? WHERE projectID=?";
        try(PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, project.getProjectTitle());
            ps.setString(2, project.getProjectDescription());
            ps.setInt(3,project.getCustomer());
            ps.setDate(4, project.getOrderDate());
            ps.setDate(5, project.getDeliveryDate());
            ps.setString(6, project.getLinkAgreement());
            ps.setInt(7, project.getCompanyRep());
            ps.setInt(8, project.getStatus());
            ps.setInt(9, project.getID());
            ps.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    /*
    ###########---DELETE PROJECT---###########
     */
    public void deleteProject(Project project) throws SQLException {
        String sql="DELETE FROM project WHERE projectID=?";
        try {
            dbConnection.setAutoCommit(false);
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setInt(1, project.getID());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No project found with ID: "+project.getID());
                }
            }
            dbConnection.commit();
        }catch(SQLException e) {
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
        String sql="SELECT projectID, projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status FROM project WHERE projectTitle=?";
        Project project = null;
        try(PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, projectTitle);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    project = new Project(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                            rs.getDate(5), rs.getDate(6), rs.getString(7), rs.getInt(8), rs.getInt(9));
                    return project;
                }
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }
        if (project == null) {
            throw new IllegalArgumentException("No project found with title: "+projectTitle);
        }
        return project;
    }
    public boolean checkIfProjectNameAlreadyExists(String projectTitle) {
        String sql ="SELECT projectTitle FROM project WHERE LOWER (projectTitle)=LOWER(?)";
        try(PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, projectTitle);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (projectTitle.equals(rs.getString(1))) {
                        return true;
                    }
                }
            }
        }catch(SQLException e) {
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
        String sql ="SELECT statusID, status FROM status";
        List<Status> statusFromDB = new ArrayList<>();
        try(PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Status status = new Status(rs.getInt(1), rs.getString(2));
                    statusFromDB.add(status);
                }
            }
        }catch(SQLException e) {
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




    /*
    ##################################
    #           CRUD Task            #
    ##################################
     */


    /*
    ##################################
    #           Create Task            #
    ##################################
     */
    public void createTask(int projectID, int subProjectID, Task task) throws SQLException {
        String sql = "INSERT INTO Task (taskTitle, taskDescription, assignedEmployee, estimatedTime, actualTime, plannedStartDate, dependingOnTask, requiredRole, subProjectID, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (var ps = dbConnection.prepareStatement(sql)) {
            //setObject håndterer automatisk null værdier
            ps.setString(1, task.getTaskTitle());
            ps.setString(2, task.getTaskDescription());
            ps.setObject(3, task.getAssignedEmployee(), java.sql.Types.INTEGER);
            ps.setObject(4, task.getEstimatedTime(), java.sql.Types.DOUBLE);
            ps.setDouble(5, task.getActualTime());
            ps.setDate(6, task.getPlannedStartDate());
            ps.setObject(7, task.getDependingOnTask(), java.sql.Types.INTEGER);
            ps.setObject(8, task.getRequiredRole(), java.sql.Types.INTEGER);
            ps.setInt(9, task.getSubProjectID());
            ps.setInt(10, task.getStatus());

            ps.executeUpdate();
        }
    }


    /*
    ##################################
    #           READ Task            #
    ##################################
     */
    public List<Task> getAllTasksInSpecificSubProject(int subProjectID) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT taskID, taskTitle, taskDescription, assignedEmployee, estimatedTime, " +
                "actualTime, plannedStartDate, dependingOnTask, requiredRole, subProjectID, status " +
                "FROM Task WHERE subProjectID = ?";
        //TODO: skal tilrettes så taskID er korrekt når man vælger en specifik task i dependingOnTask

        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, subProjectID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    // getObject metoden bruges så vi kan håndtere null værdier
                    Integer assignedEmployee = rs.getObject("assignedEmployee", Integer.class);
                    Double estimatedTime = rs.getObject("estimatedTime", Double.class);
                    Integer dependingOnTask = rs.getObject("dependingOnTask", Integer.class);
                    Integer requiredRole = rs.getObject("requiredRole", Integer.class);

                    Task task = new Task(
                            rs.getInt("taskID"),
                            rs.getString("taskTitle"),
                            rs.getString("taskDescription"),
                            assignedEmployee,
                            estimatedTime,
                            rs.getDate("plannedStartDate"),
                            dependingOnTask,
                            requiredRole,
                            rs.getInt("subProjectID"),
                            rs.getInt("status")
                    );
                    tasks.add(task);
                }
            }

            return tasks;
        }
    }

    /*
    ##################################
    #           Update Task          #
    ##################################
     */
    public void updateTask(Task task) throws SQLException {
        String sql = "UPDATE Task SET taskTitle = ?, taskDescription = ?, assignedEmployee = ?, estimatedTime = ?, " +
                "actualTime = ?, plannedStartDate = ?, dependingOnTask = ?, requiredRole = ?, status = ? " +
                "WHERE taskID = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, task.getTaskTitle());
            ps.setString(2, task.getTaskDescription());
            ps.setObject(3, task.getAssignedEmployee(), java.sql.Types.INTEGER);
            ps.setObject(4, task.getEstimatedTime(), java.sql.Types.DOUBLE);
            ps.setDouble(5, task.getActualTime());
            ps.setDate(6, task.getPlannedStartDate());
            ps.setObject(7, task.getDependingOnTask(), java.sql.Types.INTEGER);
            ps.setObject(8, task.getRequiredRole(), java.sql.Types.INTEGER);
            ps.setInt(9, task.getStatus());
            ps.setInt(10, task.getTaskID());

            ps.executeUpdate();
        }
    }

    /*
    ##################################
    #           Delete Task          #
    ##################################
    */
    public void deleteTask(int taskID) throws SQLException {
        String sql = "DELETE FROM Task WHERE taskID = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, taskID);

            ps.executeUpdate();
        }
    }



    /*
    ###################################
    #          Helper Methods         #
    ###################################
    */
    public Status getStatusByID(int statusID) throws SQLException {
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

    public List<EmployeeRole> getNonManagerRoles() throws SQLException {
        String sql = "SELECT roleID, roleTitle, isManager FROM EmployeeRole WHERE isManager = false";

        List<EmployeeRole> nonManagerRoles = new ArrayList<>();

        try (var ps = dbConnection.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                int roleID = rs.getInt("roleID");
                String roleTitle = rs.getString("roleTitle");
                boolean isManager = rs.getBoolean("isManager");

                nonManagerRoles.add(new EmployeeRole(roleID, roleTitle, isManager));
            }
        }

        return nonManagerRoles;
    }

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
