package mmmd.teammmmd_eksamensprojekt2sem.model;

public class Subproject {

    int subprojectID;
    String subprojectTitle;
    String subprojectDescription;

    public Subproject (int subprojectID, String subprojectTitle, String subprojectDescription) {
        this.subprojectID = subprojectID;
        this.subprojectTitle = subprojectTitle;
        this.subprojectDescription = subprojectDescription;
    }

    public int getSubprojectID() {
        return subprojectID;
    }

    public String getSubprojectTitle() {
        return subprojectTitle;
    }

    public String getSubprojectDescription() {
        return subprojectDescription;
    }

    public void setSubprojectID(int subprojectID) {
        this.subprojectID = subprojectID;
    }

    public void setSubprojectTitle(String subprojectTitle) {
        this.subprojectTitle = subprojectTitle;
    }

    public void setSubprojectDescription(String subprojectDescription) {
        this.subprojectDescription = subprojectDescription;
    }
}
