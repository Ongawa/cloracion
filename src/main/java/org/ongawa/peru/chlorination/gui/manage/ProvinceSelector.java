package org.ongawa.peru.chlorination.gui.manage;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;

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

/**
 * 
 * Controller for the selection of the system to work on.
 * 
 * @author Alberto Mardomingo
 *
 */
public class ProvinceSelector implements Initializable{

    /**
     * River basin selector
     */
    @FXML
    private ComboBox<FXCollections> basinCombo;

    /**
     * Town selector
     */
    @FXML
    private ComboBox<FXCollections> townCombo;

    /**
     * Specific System
     */
    @FXML
    private ComboBox<FXCollections> systemCombo;

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

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

    public void basinSelected() {

    }

    public void townSelected() {

    }

    public void triggerBack() {

    }

    public void triggerNext() throws Exception {
        Stage stage = MainApp.getStage();

        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/ChlorinationDetails.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        ChlorinationWindow chlorinationController = loader.<ChlorinationWindow>getController();
        
        // Get and set the values for the next scene
        chlorinationController.setBasin(this.basinCombo.valueProperty().toString());
        chlorinationController.setTown(this.townCombo.valueProperty().toString());
        chlorinationController.setSystemName(this.systemCombo.valueProperty().toString());
        chlorinationController.setFamiliesCount(Integer.parseInt(this.familiesCount.textProperty().get()));
        chlorinationController.setInhabitants(Integer.parseInt(this.inhabintantsCount.textProperty().get()));
        
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Initialize list of provinces here.
        
    }

}
