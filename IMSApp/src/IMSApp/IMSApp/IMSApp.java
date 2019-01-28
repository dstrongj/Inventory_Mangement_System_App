package IMSApp.IMSApp;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Inventory Management System App C482
 * 
 * @author daniel strong <dstron7@wgu.edu>
 */
public class IMSApp extends Application {
    /**
     * This piece of code starts the GUI, loads the main FXML and sets the viewport
     * 
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(IMSApp.class.getResource("/IMSApp/views/Main.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Launch GUI
     *
     */
    public static void main(String[] args) {
        launch(args);
    }
}
