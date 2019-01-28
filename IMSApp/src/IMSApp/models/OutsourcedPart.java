package IMSApp.models;

/**
 * @author daniel strong <dstron7@wgu.edu>
 */
public class OutsourcedPart extends Part {

    // Name of the company
    private String companyName;
    
    /**
     * Get name
     * 
     * return String name 
     */
    public String getCompanyName() {
        return companyName;
    }
        
    /**
     * Set name
     * 
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
