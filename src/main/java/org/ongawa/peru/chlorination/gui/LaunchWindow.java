package org.ongawa.peru.chlorination.gui;

import org.ongawa.peru.chlorination.MainApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * Controller for the selector window 
 * 
 * @author Alberto Mardomingo
 *
 */
public class LaunchWindow {
    
    private static final Logger log = LoggerFactory.getLogger(LaunchWindow.class);
    
    /**
     * 
     * Button pressed for managing existing systems
     * 
     *  TODO: Maybe change this so the Manage are actually two buttons:
     *    - Clhorination every 15 days
     *    - Desinfect twice a year
     * 
     * @throws Exception
     */
    public void triggerManage() throws Exception {
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());

        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/manageProvince.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
        
    }
    
    public void triggerDessinfect() throws Exception {
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());

        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/desinfectProvince.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
    }
    
    /**
     * 
     * Launches the Design process for a new system
     * 
     * @throws Exception
     */
    public void triggerDesign() throws Exception{
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());

        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/designProvince.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
    }

}
