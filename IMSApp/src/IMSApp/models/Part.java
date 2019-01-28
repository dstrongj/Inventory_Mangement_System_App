package IMSApp.models;

import Exceptions.ValidationException;

/**
 * @author daniel strong <dstron7@wgu.edu>
 */
public abstract class Part {
    
    // partID
    private int partID;
    
    // part name
    private String name;
    
    // Price of part
    private double price;
    
    // inventory level
    private int inStock;
    
    // Min inventory
    private int min;
    
    // Max inventory
    private int max;
    
    /**
     * Constructor
     */
    public Part() {
    }
    
    /**
     * Get inventory
     * 
     * return inventory
     */
    public int getInStock() {
        return inStock;
    }
    
    /**
     * Get max inventory
     * 
     * return max inventory
     */
    public int getMax() {
        return max;
    }
    
    /**
     * Get min inventory
     * 
     * return min inventory
     */
    public int getMin() {
        return min;
    }
    
    /**
     * Get partname
     * 
     * return partname
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get partID
     * 
     * return partID
     */
    public int getPartID() {
        return partID;
    }
    
    /**
     * Get price
     * 
     * return price
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * error handling to make sure part is valid
     * 
     */
    public boolean isValid() throws ValidationException {
        // Name is required
        if (getName().equals("")) {
            throw new ValidationException("The name field cannot be empty.");
        }
        
        // inventory must be 0>
        if (getInStock() < 0) {
            throw new ValidationException("The current inventory must be greater than 0.");
        }
        
        // part price must be 0>
        if (getPrice() < 0) {
            throw new ValidationException("The price must be greater than $0");
        }
        
        // the min must be 0>
        if (getMin() < 0) {
            throw new ValidationException("The minimum inventory must be greater than 0.");
        }
        
        // the max must be > than min
        if (getMin() > getMax()) {
            throw new ValidationException("The minimum inventory must be less than the maximum.");
        }
        
        // inventory must be between min and max
        if (getInStock() < getMin() || getInStock() > getMax()) {
            throw new ValidationException("The current inventory must be between the minimum and maximum inventory.");
        }
        
        return true;
    }
    
    /**
     * Set inventory
     *
     */
    public void setInStock(int inStock) {
        this.inStock = inStock;
    }
    
    /**
     * Set a max inventory
     *
     */
    public void setMax(int max) {
        this.max = max;
    }
    
    /**
     * Set min inventory
     *  
     */
    public void setMin(int min) {
        this.min = min;
    }
    
    /**
     * Set partname
     * 
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Set partID
     *  
     */
    public void setPartID(int partID) {
        this.partID = partID;
    }
    
    /**
     * Set price
     * 
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
