package org.ongawa.peru.chlorination.gui.manage;

import org.ongawa.peru.chlorination.HelpStage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ChlorinationWindow {

    
    public void triggerInfo() throws Exception{
        String fxmlFile = "/fxml/helps/NaturalCaudalHelp.fxml";
        HelpStage help = new HelpStage(fxmlFile);
        
        // Create the loader and get the root node from the .fxml file describing the scene
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        
        // Create the scene (maybe get the size from the stage?
        // Only after the .show(): stage.getWidth() 
        Scene scene = new Scene(rootNode, 700, 600);
        scene.getStylesheets().add("/styles/styles.css");
        
        // Set max size
        help.setMaxHeight(700);
        help.setMaxWidth(1000);
        
        help.setTitle("Ayuda");
        help.setScene(scene);
        help.show();
        }
    
    public void triggerCalculation(){
        // TODO: Do the calculations
    }
    
    public void triggerSave() {
        // TODO: Save the results
    }
    
    public void triggerPrint() {
        // TODO: Print the results
    }
}
