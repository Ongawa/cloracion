package org.ongawa.peru.chlorination.gui.desinfect;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.SystemElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SystemDetails implements Initializable {

    private ObservableList<SystemElement> elements;
    
    /**
     * Pane with the op.
     */
    @FXML
    private AnchorPane editPane;
    
    public void triggerBack() {

    }
    
    /**
     * Create a new element
     */
    public void showAddElement() {
        // Clear the editPane and set 
        ObservableList<Node> children = this.editPane.getChildren();
        children.clear();
       
    }
    
    /**
     * Edit an existing element
     */
    public void showEditElement() {
        // Clear the editPane and set 
        ObservableList<Node> children = this.editPane.getChildren();
        children.clear();
       
    }

    public void triggerNext() throws Exception {
        Stage stage = MainApp.getStage();

        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/waterProperties.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Get system info from DataLoader
        this.elements = FXCollections.observableArrayList();
    }

}
