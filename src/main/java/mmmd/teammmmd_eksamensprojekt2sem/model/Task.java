package mmmd.teammmmd_eksamensprojekt2sem.model;

import java.util.Date;

public class Task {

    private int taskID;
    private String taskTitle;
    private String taskDescription;
    private int assignedEmployee;
    private double estimatedTime;
    private double actualTime;
    private Date plannedStartDate;
    private int dependingOnTask;
    private int requiredRole;
    private int subProjectID;
    private Status status;

    public Task(int taskID, String taskTitle, String taskDescription, int assignedEmployee, double estimatedTime, double actualTime, Date plannedStartDate, int dependingOnTask, int requiredRole, int subProjectID, Status status) {
        this.taskID = taskID;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assignedEmployee = assignedEmployee;
        this.estimatedTime = estimatedTime;
        this.actualTime = actualTime;
        this.plannedStartDate = plannedStartDate;
        this.dependingOnTask = dependingOnTask;
        this.requiredRole = requiredRole;
        this.subProjectID = subProjectID;
        this.status = status;
    }

    public int getTaskID() {
        return taskID;
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

    public int getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(int assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
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

    public int getDependingOnTask() {
        return dependingOnTask;
    }

    public void setDependingOnTask(int dependingOnTask) {
        this.dependingOnTask = dependingOnTask;
    }

    public int getRequiredRole() {
        return requiredRole;
    }

    public void setRequiredRole(int requiredRole) {
        this.requiredRole = requiredRole;
    }

    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
