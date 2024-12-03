package mmmd.teammmmd_eksamensprojekt2sem.model;

public class SubProject {

    private String subProjectTitle;
    private String subProjectDescription;
    private int projectID;
    private int statusID;
    private String status;

    public SubProject (String subprojectTitle, String subprojectDescription, int projectID, int statusID) {
        this.subProjectTitle = subprojectTitle;
        this.subProjectDescription = subprojectDescription;
        this.projectID = projectID;
        this.statusID = statusID;
    }

//    public SubProject (String subprojectTitle, String subprojectDescription, int projectID, String status) {
//        this.subProjectTitle = subprojectTitle;
//        this.subProjectDescription = subprojectDescription;
//        this.projectID = projectID;
//        this.status = status;
//    }

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

    public String getStatus() {
        return status;
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
        this.status = status;
    }
}
