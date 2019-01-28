package IMSApp.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Exceptions.ValidationException;

/**
 * @author daniel strong <dstron7@wgu.edu>
 */
public class Product {
    
    // productID
    private int productID;
    
    // productname
    private String name;
    
    // Price of product
    private double price;
    
    // inventory
    private int inStock;
    
    // Min inventory
    private int min;
    
    // Max inventory
    private int max;
    
    // List parts
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public Product() {
    }
    
    /**
     * Add part 
     */
    public void addAssociatedPart(Part associatedPart) {
        this.associatedParts.add(associatedPart);
    }
    
    /**
     * Get a list of parts
     * return parts
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }
    
    /**
     * Get number of parts
     * return parts
     */
    public int getAssociatedPartsCount() {
        return associatedParts.size();
    }
    
    /**
     * Get inventory
     * return inventory
     */
    public int getInStock() {
        return inStock;
    }
    
    /**
     * Get max inventory
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
     * Get product name
     * 
     * return product name
     */
    public String getName() {
        return name;
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
     * Get productID
     * 
     * return productID
     */
    public int getProductID() {
        return productID;
    }
    
    /**
     * Validate product
     *
     */
    public boolean isValid() throws ValidationException {
        double totalPartsPrice = 0.00;
        
        // gets price of parts
        for(Part p : getAssociatedParts()) {
            totalPartsPrice += p.getPrice();
        }
        
        // validates product name
        if (getName().equals("")) {
            throw new ValidationException("The name field cannot be empty.");
        }
        
        // validates a product is greater than 1
        if (getInStock() < 0) {
            throw new ValidationException("The current inventory must be greater than 0.");
        }
        
        // validates prices is greater than 1
        if (getPrice() < 0) {
            throw new ValidationException("The price must be greater than $0");
        }
        
        // validates product is greater than 1
        if (getAssociatedPartsCount() < 1) {
            throw new ValidationException("The product must contain at least 1 part.");
        }
        
        // validates product price
        if (totalPartsPrice > getPrice()) {
            throw new ValidationException("The product price must be greater than total cost of associated parts.");
        }
        
        // validate min inventory greater than 0
        if (getMin() < 0) {
            throw new ValidationException("The minimum inventory must be greater than 0.");
        }
        
        // validate max inventory greater than min
        if (getMin() > getMax()) {
            throw new ValidationException("The minimum inventory must be less than the maximum.");
        }
        
        // validates inventory is between the min and max
        if (getInStock() < getMin() || getInStock() > getMax()) {
            throw new ValidationException("The current inventory must be between the minimum and maximum inventory.");
        }
        
        return true;
    }
    
    /**
     * Lookup partID
     */
    public Part lookupAssociatedPart(int partID) {
        for (Part p : associatedParts) {
            if (p.getPartID() == partID) {
                return p;
            }
        }
        
        return null;
    }
    
    /**
     * Delete parts
     */
    public void purgeAssociatedParts() {
        associatedParts = FXCollections.observableArrayList();
    }
    
    /**
     * Remove part
     *
     */
    public boolean removeAssociatedPart(int partID) {
        for (Part p : associatedParts) {
            if (p.getPartID() == partID) {
                associatedParts.remove(p);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Set inventory
     *
     */
    public void setInStock(int inStock) {
        this.inStock = inStock;
    }
    
    /**
     * Set the max inventory
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
     * Set name
     *  
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** 
     * Set price 
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * Set productID
     *  
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }
}
