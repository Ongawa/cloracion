package org.ongawa.peru.chlorination.gui;

import org.ongawa.peru.chlorination.MainApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LaunchWindow {
    
    private static final Logger log = LoggerFactory.getLogger(LaunchWindow.class);
    
    public void triggerManage() throws Exception {
        Stage stage = MainApp.getStage();
        
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/manageProvince.xml"));

        log.debug("Showing JFX scene");
        Scene scene = new Scene(rootNode, 800, 600);
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
        
    }
    
    public void triggerDesign() {
        Stage stage = MainApp.getStage();

    }

}
