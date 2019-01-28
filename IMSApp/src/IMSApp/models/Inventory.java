package IMSApp.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory represents the main init for all products and parts in the IMS app
 * 
 * @author daniel strong <dstron7@wgu.edu>
 */
public class Inventory {
    
    // uses observable list for all parts in IMS
    private final static ObservableList<Part> allParts = FXCollections.observableArrayList();
    
    // uses observable list for all products in IMS
    private final static ObservableList<Product> products = FXCollections.observableArrayList();

    /**
     * Inventory constructor
     */
    public Inventory() {
    }

    /**
     * this will add new parts
     * 
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }
    
    /**
     * this will add new products
     *  
     */
    public static void addProduct(Product newProduct){
        products.add(newProduct);
    }
    
    /**
     * This code is used to select if product can be deleted or not. Logic for this model is that if a product has 
     * parts associated the product cannot be deleted from the IMS.
     * 
     * 
     */
    public static boolean canDeleteProduct(Product product) {
        return product.getAssociatedPartsCount() == 0;
    }
    
    /**
     * Get list all parts
     * 
     * return list all parts
     */
    public static ObservableList<Part> getParts() {
        return allParts;
    }
    
    /**
     * Get number of parts
     * 
     * return number parts
     */
    public static int getPartsCount() {
        return allParts.size();
    }
    
    /**
     * Get number of products
     * 
     * return number of products
     */
    public static int getProductsCount() {
        return products.size();
    }
    
    /**
     * Get list of products
     * 
     * return products
     */
    public static ObservableList<Product> getProducts() {
        return products;
    }
    
    /**
     * Look up part by ID number
     *
     * return part, else null
     */
    public static Part lookupPart(int partID) {
        for (Part p : allParts) {
            if (p.getPartID() == partID) {
                return p;
            }
        }

        return null;
    }
    
    /**
     * search product by ID
     * 
     * return product, else null
     */
    public static Product lookupProduct(int productID) {
        for (Product p : products) {
            if (p.getProductID() == productID) {
                return p;
            }
        }
        
        return null;
    }
    
    /**
     * delete a part
     *
     * return true if removed successfully, else false
     */
    public static boolean removePart(int partID) {
        for (Part p : allParts) {
            if (p.getPartID() == partID) {
                allParts.remove(p);
                return true;
            }
        }
        
        return false;
    }

    /**
     * delete product
     * 
     * return true if removed successfully, else false
     */
    public static boolean removeProduct(int productID) {
        for (Product p : products) {
            if (p.getProductID() == productID) {
                products.remove(p);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * update inventory
     *  
     */
    public static void updatePart(Part updatedPart) {
        allParts.set(updatedPart.getPartID(), updatedPart);
    }
    
    /**
     * update product 
     */
    public static void updateProduct(Product updatedProduct) {
        products.set(updatedProduct.getProductID(), updatedProduct);
    }
}
