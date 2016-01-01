package org.ongawa.peru.chlorination.gui.design;

import org.ongawa.peru.chlorination.MainApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CLCalculator {

    /**
     * Natural flow rate
     */
    @FXML
    private TextField naturalFlowRate;
    
    /**
     * Current required flow rate
     */
    @FXML
    private Label currentFlowRate;
    
    /**
     * Future required flow rate
     * 
     */
    @FXML
    private Label futureFlowRate;
    
    /**
     * Comercial cl purity
     */
    @FXML
    private TextField clPurity;
    
    /**
     * How often the cl is recharged.
     */
    @FXML
    private TextField rechargePeriod;
    
    /**
     * Daily drip rate
     */
    @FXML
    private TextField dailyDripRate;
    
    @FXML
    private TextField clDemmand;
    
    @FXML
    private ComboBox climateCombo;
    
    public void triggerBack() {
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }
    public void triggerNext() throws Exception {
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());

        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/ResultPrices.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
        
    }
    
}
