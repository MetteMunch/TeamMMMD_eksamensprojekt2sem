package mmmd.teammmmd_eksamensprojekt2sem.model;

public class EmployeeRole {
    private int roleID;
    private String roleTitle;
    private boolean isManager;

    public EmployeeRole(int roleID, String roleTitle, boolean isManager) {
        this.roleID = roleID;
        this.roleTitle = roleTitle;
        this.isManager = isManager;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
