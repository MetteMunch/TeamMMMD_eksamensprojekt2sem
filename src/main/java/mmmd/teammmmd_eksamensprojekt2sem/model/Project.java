package mmmd.teammmmd_eksamensprojekt2sem.model;

import java.sql.Date;

public class Project {
    /*
    Daniel - DanielJensenKEA
     */
    private int ID;
    private String projectTitle;
    private String projectDescription;
    private int customer;
    private Date orderDate;
    private Date deliveryDate;
    private String linkAgreement;
    private int companyRep;
    private int status;

    public Project(String projectTitle, String projectDescription, int customer, Date orderDate, Date deliveryDate, String linkAgreement, int companyRep, int status) {
        ID = -1; //Vi retter ID til korrekte id ved lookup i DB. Kun relevant ved createProject()
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.customer = customer;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.linkAgreement = linkAgreement;
        this.companyRep = companyRep;
        this.status = status;
    }

    public Project(int ID, String projectTitle, String projectDescription, int customer, Date orderDate, Date deliveryDate, String linkAgreement, int companyRep, int status) {
        //Denne constructor bruges, når vi henter projektet fra databasen direkte.
        this.ID = ID;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.customer = customer;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.linkAgreement = linkAgreement;
        this.companyRep = companyRep;
        this.status = status;
    }
    public Project() {

    }

    public int getID() {
        if (ID == -1) {
            throw new IllegalStateException("The Project ID has not been corrected. PROJECT CLASS LINE 33.");
        } else {
            return ID;
        }
    }
    public void setID(int projectID) {
        this.ID = projectID;
    }


    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getLinkAgreement() {
        return linkAgreement;
    }

    public void setLinkAgreement(String linkAgreement) {
        this.linkAgreement = linkAgreement;
    }

    public int getCompanyRep() {
        return companyRep;
    }

    public void setCompanyRep(int companyRep) {
        this.companyRep = companyRep;
    }

    public int getStatus() {
        /*
        TODO: Lav enum
         */
        return status;
    }

    public void setStatus(int status) {
        /*
        TODO: Lav enum
         */
        this.status = status;
    }
}
