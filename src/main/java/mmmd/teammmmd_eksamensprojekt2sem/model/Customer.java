package mmmd.teammmmd_eksamensprojekt2sem.model;

public class Customer {
    private int customerID;
    private String companyName;
    private String repName;

    public Customer(String companyName, String repName) {
        customerID = -1;
        this.companyName = companyName;
        this.repName = repName;
    }

    public Customer(int customerID, String companyName, String repName) {
        this.customerID = customerID;
        this.companyName = companyName;
        this.repName = repName;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }
}
