package IMSApp.models;

/**
 * this code adds machineID
 * @author daniel strong <dstron7@wgu.edu>
 */
public class InhousePart extends Part {

    // MachineID of part
    private int machineID;
        
    /**
     * Get machineID
     * 
     * return machineID 
     */
    public int getMachineID() {
        return machineID;
    }
    
    /**
     * Set machineID
     * 
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
