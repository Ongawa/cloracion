package org.ongawa.peru.chlorination;

import java.util.ArrayList;
import java.util.Properties;

import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Main 
 * 
 * @author Alberto Mardomingo
 *
 */
public class MainApp extends Application {
    
    private static Stage mainStage;
    
    private static ArrayList<Scene> history;

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }
    
    public static Stage getStage() {
        return mainStage;
    }
    
    public static void pushHistory(Scene sceneToPush) {
        if (history != null ) {
            history.add(sceneToPush);
        }
    }
    
    public static Scene popHistory() {
        if (history != null) {
            System.out.println("Geting from history with size--- " + String.valueOf(history.size()));
            return history.remove(history.size() -1);
        }
        return null;
    }

    public void start(Stage stage) throws Exception {
    	
        history = new ArrayList<Scene>();
        
        log.info("Starting Hello JavaFX and Maven demonstration application");
        
    	//Forcing to load properties
    	Properties properties = ApplicationProperties.getInstance().getProperties();
    	log.info("Loaded properties");
    	
    	//Checking application first run
    	if(Boolean.parseBoolean(properties.getProperty(KEYS.APP_FIRST_RUN, "true"))){
    		IDataSource ds = DataSourceFactory.getInstance().getDefaultDataSource();
    		ds.createInitialEnvironment();
    	}
        
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
        
        stage.setHeight(700);
        stage.setWidth(1000);
        
        // Set max size
        stage.setMaxHeight(710);
        stage.setMaxWidth(1010);
        
        stage.setTitle("Cloración");
        // Add to history
        pushHistory(scene);
        stage.setScene(scene);
        stage.show();
        mainStage = stage;
    }
}
