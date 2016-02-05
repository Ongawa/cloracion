package org.ongawa.peru.chlorination.gui;

import org.ongawa.peru.chlorination.HelpStage;
import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.gui.design.ChlorinationProvince;
import org.ongawa.peru.chlorination.gui.desinfect.DesinfectionProvince;
import org.ongawa.peru.chlorination.gui.manage.ProvinceSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        
        Scene future = MainApp.popFuture(ProvinceSelector.class.getSimpleName());
        if (future != null) {
            stage.setScene(future);
            return ;
        }

        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/manageProvince.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
        
    }
    
    public void triggerDessinfect() throws Exception {
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());
        Scene future = MainApp.popFuture(DesinfectionProvince.class.getSimpleName());
        if (future != null) {
            stage.setScene(future);
            return ;
        }
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

        Scene future = MainApp.popFuture(ChlorinationProvince.class.getSimpleName());
        if (future != null) {
            stage.setScene(future);
            return ;
        }
        
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/designProvince.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
    }
    public void triggerInfo(ActionEvent event) throws Exception{
        String fxmlFile = "/fxml/helps/" + ((Button)event.getTarget()).getId()+".fxml";
        HelpStage help = new HelpStage(fxmlFile);
        
        // Create the loader and get the root node from the .fxml file describing the scene
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        
        // Create the scene (maybe get the size from the stage?
        // Only after the .show(): stage.getWidth() 
        Scene scene = new Scene(rootNode, 700, 600);
        scene.getStylesheets().add("/styles/styles.css");
        
        // Set max size
        //help.setMaxHeight(700);
        help.setMaxWidth(1000);
        
        help.setTitle("Ayuda");
        help.setScene(scene);
        help.show();
        }

}
