package org.ongawa.peru.chlorination.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * 
 * Shows an alert with the given message
 * 
 * @author Alberto Mardomingo
 *
 */
public class ClAlert {
    
    private String alertMessage;
    
    public ClAlert(String message) {
        this.alertMessage = message;
    }
    
    public void show() throws IOException {
     // Create the loader and get the root node from the .fxml file describing the scene
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/ClAlert.fxml"));
        // Create the scene (maybe get the size from the stage?
        // Only after the .show(): stage.getWidth()
        Stage stage = new Stage();
        Scene scene = new Scene(rootNode,400, 200);
        
        // Set the error text
        ((Label)rootNode.lookup("#errorMessage")).setText(this.alertMessage);
        
        // Set the close button
        Button closeButton = (Button) rootNode.lookup("#closeButton");
        closeButton.onMouseClickedProperty().addListener((observable) -> stage.close());
        
        stage.setHeight(200);
        stage.setWidth(400);
        
        // Set max size
        stage.setMaxHeight(200);
        stage.setMaxWidth(400);
        
        stage.setTitle("Error");
        // Add to history
        stage.setScene(scene);
        stage.show();
    }

}
