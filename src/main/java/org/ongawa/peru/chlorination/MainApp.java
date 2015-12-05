package org.ongawa.peru.chlorination;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Main 
 * 
 * @author Alberto Mardomingo
 *
 */
public class MainApp extends Application {
    
    private static Stage mainStage;

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }
    
    public static Stage getStage() {
        return mainStage;
    }

    public void start(Stage stage) throws Exception {

        log.info("Starting Hello JavaFX and Maven demonstration application");
        
        String fxmlFile = "/fxml/selectorWindow.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        
        // Create the loader and get the root node from the .fxml file describing the scene
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

        log.debug("Showing JFX scene");
        
        // Create the scene (maybe get the size from the stage?
        // Only after the .show(): stage.getWidth() 
        Scene scene = new Scene(rootNode, 700, 1000);
        scene.getStylesheets().add("/styles/styles.css");
        
        // Set max size
        stage.setMaxHeight(700);
        stage.setMaxWidth(1000);
        
        stage.setTitle("Cloración");
        stage.setScene(scene);
        stage.show();
        mainStage = stage;
    }
}
