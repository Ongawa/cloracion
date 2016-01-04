package org.ongawa.peru.chlorination.gui.design;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.gui.manage.ProvinceSelector;
import org.ongawa.peru.chlorination.logic.DataCalculator;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CLCalculator implements Initializable {

    private IDataSource ds;
    private static Logger log;

    static {
        log = LoggerFactory.getLogger(ProvinceSelector.class);
    }

    private SubBasin selectedSubBasin;
    private Community selectedCommunity;
    private WaterSystem selectedWaterSystem;
    
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

    private boolean isDataValid() {
        // TODO: Actually validate the results..
        return true;
    }

    public void calculateResults() {
        if (isDataValid()) {
            DataLoader dataloader = DataLoader.getDataLoader();
            String ph = dataloader.getValue("ph");
            String turbidity = dataloader.getValue("turbidity");
            double population = this.selectedWaterSystem.getPopulation();
            double dota = this.selectedWaterSystem.getEndowment();
            String minCaudal = String.valueOf(DataCalculator.caudalMin(population, dota, "20"));
            this.currentFlowRate.setText(minCaudal);
        }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.ds = DataSourceFactory.getInstance().getDefaultDataSource();
            DataLoader dataloader = DataLoader.getDataLoader();
            
            this.selectedWaterSystem = dataloader.getSelectedWaterSystem();
            this.selectedSubBasin = dataloader.getSelectedSubBasin();
            this.selectedCommunity = dataloader.getSelectedCommunity();
            
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            log.warn(e.toString());
        }
    }

}
