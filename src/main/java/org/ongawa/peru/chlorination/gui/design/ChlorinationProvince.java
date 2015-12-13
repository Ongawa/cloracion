package org.ongawa.peru.chlorination.gui.design;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.DataLoader;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChlorinationProvince implements Initializable{
    
    /**
     * Static data
     */
    private DataLoader dataLoader;
    
    /**
     * River basin selector
     */
    @FXML
    private ComboBox basinCombo;

    /**
     * Town selector
     */
    @FXML
    private ComboBox townCombo;

    /**
     * Specific System
     */
    @FXML
    private ComboBox systemCombo;

    /**
     * Inhabitants count for the given town.
     * 
     */
    @FXML
    private TextField inhabintantsCount;

    /**
     * Families count for the given town.
     * 
     */
    @FXML
    private TextField familiesCount;

    
    /**
     * Groth rate we want to use for the population
     * 
     */
    @FXML
    private TextField growthRate;
    
    /**
     * Water consumption by person and day
     * 
     */
    @FXML
    private TextField consumption;
    
    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;
    
    public void basinSelected() {
        String basinSelected = this.basinCombo.valueProperty().getValue().toString();
        
        this.townCombo.getItems().clear();
        this.townCombo.setItems(FXCollections.observableList(this.dataLoader.getTowns(basinSelected)));
    }

    public void townSelected() {
        // Get basin selected
        String basinSelected = this.basinCombo.valueProperty().getValue().toString();
        
        // Get townSelected
        String townSelected = this.townCombo.valueProperty().getValue().toString();
        
        this.systemCombo.getItems().clear();
        this.systemCombo.setItems(FXCollections.observableArrayList(this.dataLoader.getSystems(basinSelected, townSelected)));
    }

    public void systemSelected() {
        // TODO: Get default families and inhabitants from the DAtaLoader 
    }
    
    public void triggerBack() {

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
        // TODO Initialize list of provinces here.
        this.dataLoader = DataLoader.getDataLoader();
        this.basinCombo.getItems().clear();
        this.basinCombo.setItems(FXCollections.observableArrayList(dataLoader.getBasins()));
    }
}
