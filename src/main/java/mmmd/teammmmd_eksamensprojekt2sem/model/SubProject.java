package mmmd.teammmmd_eksamensprojekt2sem.model;

public class SubProject {

    private int subProjectID;
    private String subProjectTitle;
    private String subProjectDescription;
    private int projectID;
    private int statusID;
    private String statusString;

    public SubProject (String subprojectTitle, String subprojectDescription, int projectID, int statusID) {
        this.subProjectTitle = subprojectTitle;
        this.subProjectDescription = subprojectDescription;
        this.projectID = projectID;
        this.statusID = statusID;
    }

    public SubProject() {
    }


    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }

    public String getSubProjectTitle() {
        return subProjectTitle;
    }

    public String getSubProjectDescription() {
        return subProjectDescription;
    }

    public int getProjectID() {
        return projectID;
    }

    public int getStatusID() {
        return statusID;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setSubProjectTitle(String subProjectTitle) {
        this.subProjectTitle = subProjectTitle;
    }

    public void setSubProjectDescription(String subProjectDescription) {
        this.subProjectDescription = subProjectDescription;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public void setStatusString(String status) {
        this.statusString = status;
    }
}
