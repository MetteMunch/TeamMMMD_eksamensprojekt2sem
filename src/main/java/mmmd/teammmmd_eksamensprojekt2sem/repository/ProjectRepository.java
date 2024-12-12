package mmmd.teammmmd_eksamensprojekt2sem.repository;

import mmmd.teammmmd_eksamensprojekt2sem.model.*;
import mmmd.teammmmd_eksamensprojekt2sem.model.SubProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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

    public Customer fetchInternalProjectCustomer() {
        String fetchSql = "SELECT customerID, companyName, repName FROM customer WHERE companyName=?";
        String insertSQL = "INSERT INTO customer(companyName, repName) VALUES(?,?)";
        String internalProject = "Internal Project";
        String internalRep = "Internal";

        try (PreparedStatement fetchPs = dbConnection.prepareStatement(fetchSql)) { //Vi henter Internal Project som Customer, hvis det eksisterer i databasen
            fetchPs.setString(1, internalProject);

            try (ResultSet rs = fetchPs.executeQuery()) {
                if (rs.next()) { //Internal Project(IP) eksisterer i databasen og vi laver det til Customer objekt og sender videre.
                    Customer internalCus = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3));
                    return internalCus;
                }
            }

            try (PreparedStatement insertPs = dbConnection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) { //IP findes ikke og vi inserter det i databasen.
                insertPs.setString(1, internalProject);
                insertPs.setString(2, internalRep);
                int affectedRows = insertPs.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet rs = insertPs.getGeneratedKeys()) {
                        if (rs.next()) {
                            int customerID = rs.getInt(1);
                            return new Customer(customerID, internalProject, internalRep);
                        }
                    }
                }
            }
        } catch (SQLException e) {
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
                Project project = new Project(projectID, projectTitle, projectDescription, customerID, orderDate, agreedDeliveryDate, linkAgreement, companyRep, projectStatusID);
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

    public List<Project> showAllProjectsSpecificProjectManager(int employeeID) {
        String SQL = "SELECT project.projectID, project.projectTitle, project.projectDescription, project.customer, customer.companyName, \n" +
                "project.orderDate, project.deliveryDate, project.linkAgreement, project.status, status.status,\n" +
                "SUM(task.estimatedTime) AS estimatedTime, SUM(task.actualTime) AS actualTime FROM project\n" +
                "INNER JOIN customer ON project.customer = customer.customerID\n" +
                "INNER JOIN status ON project.status = status.statusID\n" +
                "LEFT JOIN subproject ON project.projectID = subproject.projectID\n" +
                "LEFT JOIN task ON subproject.subprojectID = task.subprojectID\n" +
                "WHERE companyRep =?\n" +
                "GROUP BY project.projectID, project.projectTitle, project.projectDescription, project.customer, customer.companyName, " +
                "project.orderDate, project.deliveryDate, project.linkAgreement, project.status, status.status";

        List<Project> listOfProjectsSpecificPM = new ArrayList<>();

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
                int projectStatusID = rs.getInt(9);
                String projectStatus = rs.getString(10);
                double totalEstimatedTime = rs.getDouble(11);
                double totalActualTime = rs.getDouble(12);


                Project project = new Project(projectID, projectTitle, projectDescription, customerID, orderDate, agreedDeliveryDate, linkAgreement, employeeID, projectStatusID);
                project.setCustomerNameString(customerName);
                project.setStatusString(projectStatus);
                project.setActualTimeTotal(totalActualTime);
                project.setEstimatedTimeTotal(totalEstimatedTime);


                listOfProjectsSpecificPM.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfProjectsSpecificPM;
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
    ###########---UPDATE CUSTOMER ID ON PROJECT USED IN CONNECTION WITH NEW AND INTERNAL---###########
     */
    public void updateProjectsCustomerID(int projectID, int customerID) {
        String SQL = "UPDATE project SET customer=? WHERE projectID=?";

        try(PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1, customerID);
            ps.setInt(2, projectID);
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
    #           CRUD SUBPROJECT        #
    #####################################
     */

    //*******CREATE --- SUBPROJECT******//
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

    public boolean checkIfSubProjectNameAlreadyExists(String subProjectTitle, int projectID) {
        String sql = "SELECT subProjectTitle FROM subProject WHERE LOWER(subProjectTitle) = LOWER(?) AND projectID=?";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, subProjectTitle);
            ps.setInt(2, projectID);
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


    public SubProject showSubProject(int subProjectID) {
        String sql = "SELECT subProjectTitle, subProjectDescription, projectID, subproject.status, status.status FROM subProject\n" +
                "INNER JOIN Status ON subProject.status = status.statusID WHERE subProjectID =?";

        SubProject subProject = null;

        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, subProjectID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    subProject = new SubProject(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
                    subProject.setSubProjectID(subProjectID);
                    subProject.setStatusString(rs.getString(5));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subProject;
    }

    public List<SubProject> showListOfSpecificSubProjects(int projectID) {
        List<SubProject> listOfSubProjects = new ArrayList<>();

        String SQL = "SELECT subProjectID, subProjectTitle, subProjectDescription, subProject.status, Status.status FROM subProject\n" +
                "INNER JOIN status ON Status.statusID = SubProject.status WHERE projectID = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1, projectID);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int subProjectID = rs.getInt(1);
                    String subProjectTitle = rs.getString(2);
                    String subProjectDescription = rs.getString(3);
                    int statusID = rs.getInt(4);
                    String statusString = rs.getString(5);
                    SubProject subProject = new SubProject(subProjectTitle, subProjectDescription, projectID, statusID);
                    subProject.setStatusString(statusString);
                    subProject.setSubProjectID(subProjectID);
                    listOfSubProjects.add(subProject);
                }
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
    ##################################
    #             Task               #
    ##################################
     */


    //###### Create Task #############

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


    //###### Read Task #############

    public List<Task> getAllTasksInSpecificSubProject(int subProjectID) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT taskID, taskTitle, taskDescription, assignedEmployee, estimatedTime, " +
                "actualTime, plannedStartDate, dependingOnTask, requiredRole, subProjectID, status " +
                "FROM Task WHERE subProjectID = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, subProjectID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    // getObject metoden bruges så vi kan håndtere null værdier
                    Integer assignedEmployee = rs.getObject("assignedEmployee", Integer.class);
                    Double estimatedTime = rs.getObject("estimatedTime", Double.class);
                    Integer dependingOnTask = rs.getObject("dependingOnTask", Integer.class);
                    Integer requiredRole = rs.getObject("requiredRole", Integer.class);
                    double actualTime = rs.getDouble("actualTime");

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
                    task.setActualTime(actualTime);
                    tasks.add(task);

                }
            }

            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Task> showAllTasksSpecificEmployee(int employeeID) {
        //Forespørgsel til at få alle tasks der er assigned til en specifik medarbejder

        List<Task> listOfTasksSpecificEmployee = new ArrayList<>();

        String SQL = "SELECT task.taskID, task.taskTitle, task.taskDescription, task.assignedEmployee, employee.fullName,\n" +
                "task.estimatedTime, task.actualTime, task.plannedStartDate,task.dependingOnTask, task2.taskTitle AS dependingOnTaskTitle, \n" +
                "task.requiredRole, employeerole.roleTitle, task.subProjectID, task.status, status.status, subproject.projectID FROM task\n" +
                "LEFT JOIN task task2 ON task.dependingOntask = task2.taskID\n" +
                "INNER JOIN employee ON employee.employeeID = task.assignedEmployee\n" +
                "LEFT JOIN employeerole ON employeerole.roleID = task.requiredRole\n" +
                "INNER JOIN status ON status.statusID = task.status\n" +
                "INNER JOIN subproject ON subproject.subprojectID = task.subProjectID\n" +
                "WHERE task.assignedEmployee = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1, employeeID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    int taskID = rs.getInt(1);
                    String taskTitle = rs.getString(2);
                    String taskDescription = rs.getString(3);
                    int assignedEmp = rs.getInt(4);
                    String assignedEmpFullName = rs.getString(5);
                    double estTime = rs.getDouble(6);
                    double actTime = rs.getDouble(7);
                    Date plannedStartDate = rs.getDate(8);
                    int dependingOnTaskID = rs.getInt(9);
                    String dependingOnTaskTitle = rs.getString(10);
                    int requiredRole = rs.getInt(11);
                    String roleTitle = rs.getString(12);
                    int subProjectID = rs.getInt(13);
                    int statusID = rs.getInt(14);
                    String status = rs.getString(15);
                    int projectID = rs.getInt(16);

                    Task task = new Task(taskID, taskTitle, taskDescription, assignedEmp, estTime, plannedStartDate, dependingOnTaskID, requiredRole, subProjectID, statusID);
                    task.setActualTime(actTime);
                    task.setAssignedEmployeeString(assignedEmpFullName);
                    task.setDependingOnTaskString(dependingOnTaskTitle);
                    task.setRequiredRoleString(roleTitle);
                    task.setStatusString(status);
                    task.setProjectID(projectID);

                    listOfTasksSpecificEmployee.add(task);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listOfTasksSpecificEmployee;
    }

    public List<Task> showAllTasksSpecificProjectManager(int employeeID) {
        //Forespørgsel til at få alle tasks der er oprettet under projekter tilknyttet en specifik projekt manager

        List<Task> listOfTasksSpecificPM = new ArrayList<>();

        String SQL = "SELECT task.taskID, task.taskTitle, task.taskDescription, task.assignedEmployee, employee.fullName,\n" +
                "task.estimatedTime, task.actualTime, task.plannedStartDate,task.dependingOnTask, task2.taskTitle AS dependingOnTaskTitle, \n" +
                "task.requiredRole, task.subProjectID, task.status, status.status, subproject.projectID FROM task\n" +
                "LEFT JOIN task task2 ON task.dependingOntask = task2.taskID\n" +
                "INNER JOIN subproject ON subproject.subprojectID = task.subProjectID\n" +
                "INNER JOIN project ON project.projectID = subproject.projectID\n" +
                "LEFT JOIN employee ON employee.employeeID = task.assignedEmployee\n" +
                "INNER JOIN status ON status.statusID = task.status\n" +
                "WHERE project.companyRep = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1, employeeID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    int taskID = rs.getInt(1);
                    String taskTitle = rs.getString(2);
                    String taskDescription = rs.getString(3);
                    int assignedEmp = rs.getInt(4);
                    String assignedEmpFullName = rs.getString(5);
                    double estTime = rs.getDouble(6);
                    double actTime = rs.getDouble(7);
                    Date plannedStartDate = rs.getDate(8);
                    int dependingOnTaskID = rs.getInt(9);
                    String dependingOnTaskTitle = rs.getString(10);
                    int requiredRole = rs.getInt(11);
                    int subProjectID = rs.getInt(12);
                    int statusID = rs.getInt(13);
                    String status = rs.getString(14);
                    int projectID = rs.getInt(15);

                    Task task = new Task(taskID, taskTitle, taskDescription, assignedEmp, estTime, plannedStartDate, dependingOnTaskID, requiredRole, subProjectID, statusID);
                    task.setActualTime(actTime);
                    task.setAssignedEmployeeString(assignedEmpFullName);
                    task.setDependingOnTaskString(dependingOnTaskTitle);
                    task.setStatusString(status);
                    task.setProjectID(projectID);

                    listOfTasksSpecificPM.add(task);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listOfTasksSpecificPM;
    }


    //###### Update  Task #############

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
    //###### Delete Task #############

    public void deleteTask(int taskID) throws SQLException {
        String sql = "DELETE FROM Task WHERE taskID = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, taskID);

            ps.executeUpdate();
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

                } else {
                    throw new IllegalArgumentException("No project found with title: " + project.getProjectTitle() + " and order date: " + project.getOrderDate() + ". PROJECT REPOSITORY LINE 45.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Project fetchSpecificProject(int projectID) {
        Project project = null;

        String SQL = "SELECT projectTitle, projectDescription, project.customer, customer.companyName, orderDate, deliveryDate, " +
                "linkAgreement, companyRep, employee.fullName, project.status AS statusID, status.status AS statusString FROM project " +
                "INNER JOIN customer ON customer.customerID = project.customer " +
                "INNER JOIN status ON status.statusID = project.status " +
                "INNER JOIN employee ON employee.employeeID = project.companyrep WHERE project.projectID=?";


        try (PreparedStatement ps = dbConnection.prepareStatement(SQL)) {
            ps.setInt(1, projectID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String projectTitle = rs.getString(1);
                    String projectDescription = rs.getString(2);
                    int customerID = rs.getInt(3);
                    String customerName = rs.getString(4);
                    Date orderDate = rs.getDate(5);
                    Date agreedDeliveryDate = rs.getDate(6);
                    String linkAgreement = rs.getString(7);
                    int companyRep = rs.getInt(8);
                    String nameCompanyRep = rs.getString(9);
                    String status = rs.getString("statusString");
                    int statusID = rs.getInt("statusID");

                    project = new Project(projectID, projectTitle, projectDescription, customerID, orderDate, agreedDeliveryDate,
                            linkAgreement, companyRep, statusID);
                    project.setStatusString(status);
                    project.setCustomerNameString(customerName);
                    project.setCompanyRepString(nameCompanyRep);

                    return project;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (project == null) {
            throw new IllegalArgumentException("No project found with ID: " + projectID);
        }
        return project;
    }

    public int findProjectIDFromDB(Project project) {
        String sql = "SELECT projectID FROM project WHERE projectTitle=? AND orderDate=?";
        int projectIDFromDB = -1;

        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, project.getProjectTitle());
            ps.setDate(2, project.getOrderDate());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    projectIDFromDB = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (projectIDFromDB == -1) {
            throw new IllegalArgumentException("No project found with title: " + project.getProjectTitle() + " and order date: " + project.getOrderDate() + ". PROJECT REPOSITORY LINE 45.");
        }
        return projectIDFromDB;
    }

    public Task getTaskByID(int taskID) throws SQLException {
        String sql = "SELECT taskTitle, taskDescription, assignedEmployee, estimatedTime, actualTime, plannedStartDate, dependingOnTask, requiredRole, subProjectID, status FROM Task WHERE taskID = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setInt(1, taskID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    // getObject metoden bruges så vi kan håndtere null værdier
                    Integer assignedEmployee = rs.getObject("assignedEmployee", Integer.class);
                    Double estimatedTime = rs.getObject("estimatedTime", Double.class);
                    Integer dependingOnTask = rs.getObject("dependingOnTask", Integer.class);
                    Integer requiredRole = rs.getObject("requiredRole", Integer.class);

                    return new Task(
                            rs.getString("taskTitle"),
                            rs.getString("taskDescription"),
                            assignedEmployee,
                            estimatedTime,
                            rs.getDouble("actualTime"),
                            rs.getDate("plannedStartDate"),
                            dependingOnTask,
                            requiredRole,
                            rs.getInt("subProjectID"),
                            rs.getInt("status")
                    );
                }
            }
        }
        return null; // Hvis ikke taskID findes returnerer vi null
    }

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

    public void submitHours(int taskID, double hours) throws SQLException {
        String fetchActualTimeSql = "SELECT actualTime FROM Task WHERE taskID = ?";

        double currentActualTime = 0.0;
        try (PreparedStatement ps = dbConnection.prepareStatement(fetchActualTimeSql)) {
            ps.setInt(1, taskID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currentActualTime = rs.getDouble("actualTime");
                }
            }
        }

        double updatedActualTime = currentActualTime + hours;
        String updateActualTimeSql = "UPDATE Task SET actualTime = ? WHERE taskID = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(updateActualTimeSql)) {
            ps.setDouble(1, updatedActualTime);
            ps.setInt(2, taskID);
            ps.executeUpdate();
        }
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
        String sql = "SELECT e.employeeID, e.fullName FROM Employee e " +
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

    /*
        #####################################
        #          Calculation Methods      #
        #####################################
     */




}
