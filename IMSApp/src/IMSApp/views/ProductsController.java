package IMSApp.views;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import Exceptions.ValidationException;
import IMSApp.models.Inventory;
import IMSApp.models.Part;
import IMSApp.models.Product;
import static IMSApp.views.MainController.getModifiedProduct;

/**
 * Products Controller class enables ability to add and modify product
 * 
 * @author daniel strong <dstron7@wgu.edu>
 */
public class ProductsController implements Initializable {

    // page label
    @FXML
    private Label ProductsPageLabel;
    
    // ProductID
    @FXML
    private TextField ProductsIDField;
    
    // Productname
    @FXML
    private TextField ProductsNameField;
    
    // Product max inventory
    @FXML
    private TextField ProductsMaxField;
    
    // Product min inventory
    @FXML
    private TextField ProductsMinField;
    
    // product inventory
    @FXML
    private TextField ProductsInStockField;
    
    // Product price
    @FXML
    private TextField ProductsPriceField;
    
    // Parts search box
    @FXML
    private TextField ProductPartsSearchField;
    
    // parts table
    @FXML
    private TableView<Part> ProductAllPartsTable;
    
    // parts ID
    @FXML
    private TableColumn<Part, Integer> ProductAllPartsIDCol;
    
    // parts name
    @FXML
    private TableColumn<Part, String> ProductAllPartsNameCol;
    
    // parts inventory
    @FXML
    private TableColumn<Part, Integer> ProductAllPartsInStockCol;
    
    // parts price
    @FXML
    private TableColumn<Part, Double> ProductAllPartsPriceCol;
    
    // Parts Table
    @FXML
    private TableView<Part> ProductCurrentPartsTable;
    
    // PartsID
    @FXML
    private TableColumn<Part, Integer> ProductCurrentPartsIDCol;
    
    // parts name
    @FXML
    private TableColumn<Part, String> ProductCurrentPartsNameCol;
    
    // parts inventory
    @FXML
    private TableColumn<Part, Integer> ProductCurrentPartsInStockCol;
    
    // parts price
    @FXML
    private TableColumn<Part, Double> ProductCurrentPartsPriceCol;
    
    // list of part
    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    
    // Product being modified 
    private final Product modifiedProduct;
    
    /**
     * Constructor
     */
    public ProductsController() {
        this.modifiedProduct = getModifiedProduct();
    }
    
    /**
     * Add part to product
     * 
     * @param event 
     */
    @FXML
    void handleAddProductPart(ActionEvent event) {
        Part part = ProductAllPartsTable.getSelectionModel().getSelectedItem();
        productParts.add(part);
        populateCurrentPartsTable();
    }

    /**
     * event to cancel
     */
    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modification");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Confirm cancellation to update of product " + ProductsNameField.getText() + ".");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    /**
     * Delete product
     * 
     */
    @FXML
    void handleDeleteProductPart(ActionEvent event) throws IOException {
  
        if (productParts.size() > 2) {
            Part part = ProductCurrentPartsTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Delete");
            alert.setHeaderText("Confirm deletion");
            alert.setContentText("Are you sure you want to delete " + part.getName() + " ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                productParts.remove(part);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Part Deletion Error!");
            alert.setHeaderText("Product requires one part!");
            alert.setContentText("Product requires at least one part.");
            alert.showAndWait();
        }
    }

    /**
     * event to save
     * 
     */
    @FXML
    void handleSave(ActionEvent event) throws IOException {
        String productName = ProductsNameField.getText();
        String productInv = ProductsInStockField.getText();
        String productPrice = ProductsPriceField.getText();
        String productMin = ProductsMinField.getText();
        String productMax = ProductsMaxField.getText();

        Product newProduct = new Product();
        newProduct.setName(productName);
        newProduct.setPrice(Double.parseDouble(productPrice));
        newProduct.setInStock(Integer.parseInt(productInv));
        newProduct.setMin(Integer.parseInt(productMin));
        newProduct.setMax(Integer.parseInt(productMax));
       
    
        if (modifiedProduct != null) {
            modifiedProduct.purgeAssociatedParts();
        }
        
        // Iterate and add to product
        for (Part p: productParts) {
            newProduct.addAssociatedPart(p);
        }
        
        try {
            newProduct.isValid();
            
            // Create and update product
            if (modifiedProduct == null) {
                newProduct.setProductID(Inventory.getProductsCount());
                Inventory.addProduct(newProduct);
            } else {
                newProduct.setProductID(modifiedProduct.getProductID());
                Inventory.updateProduct(newProduct);
            }

            // switch to main screen
            Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ValidationError");
            alert.setHeaderText("Product not valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    /**
     * event for parts search
     * 
     */
    @FXML
    void handleSearchParts(ActionEvent event) throws IOException {
        String partsSearchIdString = ProductPartsSearchField.getText();
        Part searchedPart = Inventory.lookupPart(Integer.parseInt(partsSearchIdString));

        if (searchedPart != null) {
            ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
            filteredPartsList.add(searchedPart);
            ProductAllPartsTable.setItems(filteredPartsList);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("No matching parts found!");
            alert.showAndWait();
        }
    }
    
    /**
     * Initializes the controller class
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (modifiedProduct == null) {
            ProductsPageLabel.setText("Add Product");
            int productAutoID = Inventory.getProductsCount();
            ProductsIDField.setText("AUTO GEN: " + productAutoID);
            System.out.println("Here");
        } else {
            ProductsPageLabel.setText("Modify Product");
            
            ProductsIDField.setText(Integer.toString(modifiedProduct.getProductID()));
            ProductsNameField.setText(modifiedProduct.getName());
            ProductsInStockField.setText(Integer.toString(modifiedProduct.getInStock()));
            ProductsPriceField.setText(Double.toString(modifiedProduct.getPrice()));
            ProductsMinField.setText(Integer.toString(modifiedProduct.getMin()));
            ProductsMaxField.setText(Integer.toString(modifiedProduct.getMax()));
        
            productParts = modifiedProduct.getAssociatedParts();
        }
        
        ProductAllPartsIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        ProductAllPartsNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ProductAllPartsInStockCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        ProductAllPartsPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        ProductCurrentPartsIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        ProductCurrentPartsNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ProductCurrentPartsInStockCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        ProductCurrentPartsPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        populateAvailablePartsTable();
        populateCurrentPartsTable();
    }
    
    /**
     * add to parts table
     */
    public void populateAvailablePartsTable() {
        ProductAllPartsTable.setItems(Inventory.getParts());
    }

    /**
     * add to part table
     */
    public void populateCurrentPartsTable() {
        ProductCurrentPartsTable.setItems(productParts);
    }

}
