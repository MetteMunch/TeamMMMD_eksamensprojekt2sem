package mmmd.teammmmd_eksamensprojekt2sem.model;

import java.sql.Date;

public class Task {

    private int taskID;
    private String taskTitle;
    private String taskDescription;
    private Integer assignedEmployee;
    private Double estimatedTime;
    private double actualTime;
    private Date plannedStartDate;
    private Integer dependingOnTask;
    private Integer requiredRole;
    private int subProjectID;
    private int status;

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

    public Integer getRequiredRole() {
        return requiredRole;
    }

    public void setRequiredRole(Integer requiredRole) {
        this.requiredRole = requiredRole;
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
}
