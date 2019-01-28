package IMSApp.views;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import Exceptions.ValidationException;
import IMSApp.models.InhousePart;
import IMSApp.models.Inventory;
import IMSApp.models.OutsourcedPart;
import IMSApp.models.Part;
import static IMSApp.views.MainController.getModifiedPart;

/**
 * Part Controller class enables functionality for both adding and modifying
 * parts
 * 
 * @author daniel strong <dstron7@wgu.edu>
 */
public class PartsController implements Initializable {

    // partID
    @FXML
    private TextField PartsIDField;
    
    // part name
    @FXML
    private TextField PartsNameField;
    
    // part inventory
    @FXML
    private TextField PartsInStockField;
    
    // part price
    @FXML
    private TextField PartsPriceField;
    
    // part max inventory
    @FXML
    private TextField PartsMaxField;
    
    // part min inventory
    @FXML
    private TextField PartsMinField;
    
     // inhouse or outsourced
    @FXML
    private Label PartsMfgLabel;

    // part ID
    @FXML
    private TextField PartsMfgField;
    
    // page label
    @FXML
    private Label PartsPageLabel;
    
    // in-house radio button
    @FXML
    private RadioButton PartsInHouseRadioButton;
    
    // outsourced radio button
    @FXML
    private RadioButton PartsOutsourcedRadioButton;
    
    // status of part
    private boolean isInHouse;
    
    // Part being modified 
    private final Part modifyPart;

    /**
     * Constructor
     */
    public PartsController() {
        this.modifyPart = getModifiedPart();
    }
    
    /**
     * event to switch to inhouse
     * 
     * @param event 
     */
    @FXML
    void handleInHouse(ActionEvent event) {
        isInHouse = true;
        PartsMfgLabel.setText("Mach ID");
    }
    
    /**
     * event to switch to outsoursed
     * 
     * @param event 
     */
    @FXML
    void handleOutsource(ActionEvent event) {
        isInHouse = false;
        PartsMfgLabel.setText("Company Nm");
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
        alert.setContentText("Are you sure you want to cancel the update of part " + PartsNameField.getText() + "?");
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
     * event to save 
     */
    @FXML
    void handleSave(ActionEvent event) throws IOException {
        // Get data from the GUI
        String partName = PartsNameField.getText();
        String partInv = PartsInStockField.getText();
        String partPrice = PartsPriceField.getText();
        String partMin = PartsMinField.getText();
        String partMax = PartsMaxField.getText();
        String partDyn = PartsMfgField.getText();
        
        if ("".equals(partInv)) {
            partInv = "0";
        }
        
        
        if (isInHouse) {
            // change instance of InhousePart and set the instance
           
            InhousePart modifiedPart = new InhousePart();
            modifiedPart.setName(partName);
            modifiedPart.setPrice(Double.parseDouble(partPrice));
            modifiedPart.setInStock(Integer.parseInt(partInv));
            modifiedPart.setMin(Integer.parseInt(partMin));
            modifiedPart.setMax(Integer.parseInt(partMax));
            modifiedPart.setMachineID(Integer.parseInt(partDyn));

            try {
                modifiedPart.isValid();
              
                if (modifyPart == null) {
                    modifiedPart.setPartID(Inventory.getPartsCount());
                    Inventory.addPart(modifiedPart);
                } else {
                    int partID = modifyPart.getPartID();
                    modifiedPart.setPartID(partID);
                    Inventory.updatePart(modifiedPart);
                }
                
                // Return to the main screen
                Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (ValidationException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ValidationError");
                alert.setHeaderText("Not valid part");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
          
            OutsourcedPart modifiedPart = new OutsourcedPart();
            modifiedPart.setName(partName);
            modifiedPart.setPrice(Double.parseDouble(partPrice));
            modifiedPart.setInStock(Integer.parseInt(partInv));
            modifiedPart.setMin(Integer.parseInt(partMin));
            modifiedPart.setMax(Integer.parseInt(partMax));
            modifiedPart.setCompanyName(partDyn);
            
            try {
                modifiedPart.isValid();
                
               //update if modified part else nothing
                if (modifyPart == null) {
                    modifiedPart.setPartID(Inventory.getPartsCount());
                    Inventory.addPart(modifiedPart);
                } else {
                    int partID = modifyPart.getPartID();
                    modifiedPart.setPartID(partID);
                    Inventory.updatePart(modifiedPart);
                }
                
                // switch back to main screen
                Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (ValidationException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ValidationError");
                alert.setHeaderText("Not valid part");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Initialize the class
     * @param url
     * @param rb    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (modifyPart == null) {
            PartsPageLabel.setText("Add Part");
            int partAutoID = Inventory.getPartsCount();
            PartsIDField.setText("AUTO GEN: " + partAutoID);
            
            isInHouse = true;
            PartsMfgLabel.setText("Mach ID");
        }
        else{
            PartsPageLabel.setText("Modify Part");
            PartsIDField.setText(Integer.toString(modifyPart.getPartID()));
            PartsNameField.setText(modifyPart.getName());
            PartsInStockField.setText(Integer.toString(modifyPart.getInStock()));
            PartsPriceField.setText(Double.toString(modifyPart.getPrice()));
            PartsMinField.setText(Integer.toString(modifyPart.getMin()));
            PartsMaxField.setText(Integer.toString(modifyPart.getMax()));
            
            
            if (modifyPart instanceof InhousePart) {
                PartsMfgField.setText(Integer.toString(((InhousePart) modifyPart).getMachineID()));
                
                PartsMfgLabel.setText("Mach ID");
                PartsInHouseRadioButton.setSelected(true);

            } else {
                PartsMfgField.setText(((OutsourcedPart) modifyPart).getCompanyName());
                PartsMfgLabel.setText("Comp Nm");
                PartsOutsourcedRadioButton.setSelected(true);
            }
        }
    }
}
