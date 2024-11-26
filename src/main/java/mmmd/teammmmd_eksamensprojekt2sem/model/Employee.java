package mmmd.teammmmd_eksamensprojekt2sem.model;

public class Employee {
    private int employeeID;
    private String fullName;
    private String username;
    private String password;
    private int role;

    public Employee(int employeeID, String fullName, String username, String password, int role) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
