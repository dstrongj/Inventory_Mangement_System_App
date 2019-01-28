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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import IMSApp.IMSApp.IMSApp;
import IMSApp.models.Inventory;
import static IMSApp.models.Inventory.canDeleteProduct;
import static IMSApp.models.Inventory.getParts;
import static IMSApp.models.Inventory.getProducts;
import static IMSApp.models.Inventory.removePart;
import static IMSApp.models.Inventory.removeProduct;
import IMSApp.models.Part;
import IMSApp.models.Product;

/**
 * Main Class that controls the FXML and app inputs
 * 
 * @author daniel strong <dstron7@wgu.edu>
 */
public class MainController implements Initializable {

    // main part table
    @FXML
    private TableView<Part> PartsTable;
    
    // part tableID column
    @FXML
    private TableColumn<Part, Integer> PartIDCol;
    
    // part tablename column
    @FXML
    private TableColumn<Part, String> PartNameCol;
    
    // part table inventory column
    @FXML
    private TableColumn<Part, Integer> PartInStockCol;
    
    // part table price column
    @FXML
    private TableColumn<Part, Double> PartPriceCol;
    
    // product tabls
    @FXML
    private TableView<Product> ProductsTable;
    
    // product tableID column
    @FXML
    private TableColumn<Product, Integer> ProductIDCol;
    
    // product table name column
    @FXML
    private TableColumn<Product, String> ProductNameCol;
    
    // product table inventory column
    @FXML
    private TableColumn<Product, Integer> ProductInStockCol;
    
    // product table price column
    @FXML
    private TableColumn<Product, Double> ProductPriceCol;
    
    // part search bar
    @FXML
    private TextField PartsSearchField;
    
    // product search bar
    @FXML
    private TextField ProductsSearchField;

    // the modified part
    private static Part modifiedPart;
    
    // the modified product
    private static Product modifiedProduct;
    
    /**
     * Constructor
     */
    public MainController() {
    }
    
    /**
     * Get modified part
     * 
     * return modified part
     * @return 
     */
    public static Part getModifiedPart() {
        return modifiedPart;
    }
    
    /**
     * Set modified part
     *  
     * @param modifyPart
     */
    public void setModifiedPart(Part modifyPart) {
        MainController.modifiedPart = modifyPart;
    }
    
    /**
     * Get modified product
     * 
     * return modified product
     * @return 
     */
    public static Product getModifiedProduct() {
        return modifiedProduct;
    }
    
    /**
     * Set modified product
     * 
     * @param modifiedProduct
     */
    public void setModifiedProduct(Product modifiedProduct) {
        MainController.modifiedProduct = modifiedProduct;
    }

    /**
     * exit functions
     */
    @FXML
    void handleExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Successful exit!");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * changes to add part screen
     * 
     */
    @FXML
    void handleAddPart(ActionEvent event) throws IOException {        
        showPartsScreen(event);
    }

    /**
     * changes to add product screen
     * 
     */
    @FXML
    void handleAddProduct(ActionEvent event) throws IOException {
        showProductScreen(event);
    }

    /**
     * Handles delete of part
     * 
     * 
     */
    @FXML
    void handleDeletePart(ActionEvent event) throws IOException {
        Part part = PartsTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Part Delete");
        alert.setHeaderText("Confirm delete?");
        alert.setContentText("Are you sure you want to delete " + part.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            removePart(part.getPartID());
            populatePartsTable();
        }
    }
    
    /**
     * delete  product 
     */
    @FXML
    void handleDeleteProduct(ActionEvent event) throws IOException {
        Product product = ProductsTable.getSelectionModel().getSelectedItem();

        if (!canDeleteProduct(product)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Produt Deletion Error!");
            alert.setHeaderText("The produt cannot be removed!");
            alert.setContentText("This product has associated parts and cannot be deleted.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Delete");
            alert.setHeaderText("Confirm deletion?");
            alert.setContentText("Are you sure you want to delete " + product.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                removeProduct(product.getProductID());
                populatePartsTable();
            }
        }
    }
    
    /**
     * Set modifiedPart
     */
    @FXML
    void handleModifyPart(ActionEvent event) throws IOException {
        modifiedPart = PartsTable.getSelectionModel().getSelectedItem();
        setModifiedPart(modifiedPart);

        showPartsScreen(event);
    }

    /**
     * Set modifiedProduct
     */
    @FXML
    void handleModifyProduct(ActionEvent event) throws IOException {
        modifiedProduct = ProductsTable.getSelectionModel().getSelectedItem();
        setModifiedProduct(modifiedProduct);
        
        showProductScreen(event);
    }
    
    /**
     * Part search handler 
     */
    @FXML
    void handleSearchPart(ActionEvent event) throws IOException {
        String partsSearchIdString = PartsSearchField.getText();
        Part searchedPart = Inventory.lookupPart(Integer.parseInt(partsSearchIdString));

        if (searchedPart != null) {
            ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
            filteredPartsList.add(searchedPart);
            PartsTable.setItems(filteredPartsList);
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("The part is not found");
            alert.setContentText("The search has no parts found!");
            alert.showAndWait();
        }
    }
    
    /**
     * Product search handler 
     */
    @FXML
    void handleSearchProduct(ActionEvent event) throws IOException {
        String productSearchIdString = ProductsSearchField.getText();
        Product searchedProduct= Inventory.lookupProduct(Integer.parseInt(productSearchIdString));

        if (searchedProduct != null) {
            ObservableList<Product> filteredProductList = FXCollections.observableArrayList();
            filteredProductList.add(searchedProduct);
            ProductsTable.setItems(filteredProductList);
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("No product found");
            alert.setContentText("The search has no product found!");
            alert.showAndWait();
        }
    }
   
    /**
     * Initialize controller
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize part and product with nulls
        setModifiedPart(null);
        setModifiedProduct(null);

        PartIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        PartNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        PartInStockCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        PartPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        ProductIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductID()).asObject());
        ProductNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ProductInStockCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        ProductPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        populatePartsTable();
        populateProductsTable();
    }
    
    /**
     * fills part table
     */
    public void populatePartsTable() {
        PartsTable.setItems(getParts());
    }

    /**
     * fills product table
     */
    public void populateProductsTable() {
        ProductsTable.setItems(getProducts());
    }
    
    /**
     * Sets main app
     * 
     * @param mainApp
     */
    public void setMainApp(IMSApp mainApp) {
        populatePartsTable();
        populateProductsTable();
    }
    
    /**
     * loads parts screen 
     * @param event
     * @throws java.io.IOException
     */
    public void showPartsScreen(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Parts.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    /**
     * loads products screen
     * @param event
     * @throws java.io.IOException
     */
    public void showProductScreen(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Products.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
