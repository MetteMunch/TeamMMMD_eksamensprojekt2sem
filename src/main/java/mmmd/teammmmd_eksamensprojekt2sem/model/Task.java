package mmmd.teammmmd_eksamensprojekt2sem.model;

import java.sql.Date;

public class Task {

    private int taskID;
    private String taskTitle;
    private String taskDescription;
    private Integer assignedEmployee;
    private String assignedEmployeeString;
    private Double estimatedTime;
    private double actualTime;
    private Date plannedStartDate;
    private Date calculatedEndDate;
    private Integer dependingOnTask;
    private String dependingOnTaskString;
    private Integer requiredRole;
    private String requiredRoleString;
    private int subProjectID;
    private int status;
    private String statusString;
    private int projectID;

    // PM Constructor hvor actual time er sat default til 0 da denne kun skal kunne sættes af employee
    public Task(String taskTitle, String taskDescription, Integer assignedEmployee,
                Double estimatedTime, Date plannedStartDate, Integer dependingOnTask,
                Integer requiredRole, int subProjectID, int status) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assignedEmployee = assignedEmployee;
        this.estimatedTime = estimatedTime != null ? estimatedTime : 0.0; // Default sat til 0.0 hvis den er null
        this.actualTime = 0.0;
        this.plannedStartDate = plannedStartDate;
        this.dependingOnTask = dependingOnTask;
        this.requiredRole = requiredRole;
        this.subProjectID = subProjectID;
        this.status = status;
    }

    public Task(String taskTitle, String taskDescription, Integer assignedEmployee,
                Double estimatedTime, double actualTime, Date plannedStartDate, Integer dependingOnTask,
                Integer requiredRole, int subProjectID, int status) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assignedEmployee = assignedEmployee;
        this.estimatedTime = estimatedTime != null ? estimatedTime : 0.0; // Default sat til 0.0 hvis den er null
        this.actualTime = actualTime;
        this.plannedStartDate = plannedStartDate;
        this.dependingOnTask = dependingOnTask;
        this.requiredRole = requiredRole;
        this.subProjectID = subProjectID;
        this.status = status;
    }

    public Task(int taskID, String taskTitle, String taskDescription, Integer assignedEmployee,
                Double estimatedTime, Date plannedStartDate, Integer dependingOnTask,
                Integer requiredRole, int subProjectID, int status) {
        this.taskID = taskID;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assignedEmployee = assignedEmployee;
        this.estimatedTime = estimatedTime != null ? estimatedTime : 0.0; // Default sat til 0.0 hvis den er null
        this.actualTime = 0.0;
        this.plannedStartDate = plannedStartDate;
        this.dependingOnTask = dependingOnTask;
        this.requiredRole = requiredRole;
        this.subProjectID = subProjectID;
        this.status = status;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Integer getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(Integer assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public String getAssignedEmployeeString() {
        return assignedEmployeeString;
    }

    public void setAssignedEmployeeString(String assignedEmployeeString) {
        this.assignedEmployeeString = assignedEmployeeString;
    }

    public Double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public double getActualTime() {
        return actualTime;
    }

    public void setActualTime(double actualTime) {
        this.actualTime = actualTime;
    }

    public Date getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(Date plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public Integer getDependingOnTask() {
        return dependingOnTask;
    }

    public void setDependingOnTask(Integer dependingOnTask) {
        this.dependingOnTask = dependingOnTask;
    }

    public String getDependingOnTaskString() {
        return dependingOnTaskString;
    }

    public void setDependingOnTaskString(String dependingOnTaskString) {
        this.dependingOnTaskString = dependingOnTaskString;
    }

    public Integer getRequiredRole() {
        return requiredRole;
    }

    public void setRequiredRole(Integer requiredRole) {
        this.requiredRole = requiredRole;
    }

    public String getRequiredRoleString() {
        return requiredRoleString;
    }

    public void setRequiredRoleString(String requiredRoleString) {
        this.requiredRoleString = requiredRoleString;
    }

    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public Date getCalculatedEndDate() {
        return calculatedEndDate;
    }

    public void setCalculatedEndDate(Date calculatedEndDate) {
        this.calculatedEndDate = calculatedEndDate;
    }
}
