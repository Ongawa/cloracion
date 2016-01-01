package org.ongawa.peru.chlorination.gui.manage;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.DataValidator;
import org.ongawa.peru.chlorination.logic.DataCalculator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }

    public void triggerNext() throws Exception {
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());


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
        this.dataLoader = DataLoader.getDataLoader();
        this.basinCombo.getItems().clear();
        this.basinCombo.setItems(FXCollections.observableArrayList(dataLoader.getBasins()));
        
        this.familiesCount.textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        // Set the inhabitants from the families
                        if (!newValue.equals(""))
                            inhabintantsCount.textProperty().set(DataCalculator.getInhabitantsFromFamilies(newValue));
                    }
        });
    }

}
