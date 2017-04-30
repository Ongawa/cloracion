package org.ongawa.peru.chlorination.gui.design;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.HelpStage;
import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.gui.ClAlert;
import org.ongawa.peru.chlorination.gui.manage.ProvinceSelector;
import org.ongawa.peru.chlorination.logic.DataCalculator;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.DataValidator;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private TextField clDemand;

    @FXML
    private ComboBox<String> climateCombo;

    @FXML
    private Label kgquin;

    @FXML
    private Label kgmes;

    @FXML
    private Label minTankVol;

    @FXML
    private ComboBox<String> targetPopulation;

    private String[] calculatedCLResuts;
    private String calculatedTank;

    public void triggerBack() {
        // Add future
        Scene current = MainApp.getStage().getScene();
        MainApp.pushFuture(this.getClass().getSimpleName(), current);

        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }

    public void triggerInfo(ActionEvent event) throws Exception {
        String fxmlFile = "/fxml/helps/" + ((Button) event.getTarget()).getId() + ".fxml";
        HelpStage help = new HelpStage(fxmlFile);

        // Create the loader and get the root node from the .fxml file
        // describing the scene
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

        // Create the scene (maybe get the size from the stage?
        // Only after the .show(): stage.getWidth()
        Scene scene = new Scene(rootNode, 700, 600);
        scene.getStylesheets().add("/styles/styles.css");

        // Set max size
        // help.setMaxHeight(700);
        help.setMaxWidth(1000);

        help.setTitle("Ayuda");
        help.setScene(scene);
        help.show();
    }

    /**
     * Validate the results.
     * 
     * @return
     */
    private String isDataValid() {
        // Check everything...
        ArrayList<String> fieldValues = new ArrayList<String>();
        fieldValues.add(this.naturalFlowRate.getText());
        fieldValues.add(this.clPurity.getText());
        fieldValues.add(this.rechargePeriod.getText());
        fieldValues.add(this.dailyDripRate.getText());
        fieldValues.add(this.clDemand.getText());
        String formatError =  DataValidator.checkChlorinationData(fieldValues);
        if(formatError.length() > 1 )
            return formatError;
        
        String natCaudValidation = DataValidator.checkCaud(Double.valueOf(this.naturalFlowRate.getText()));
        if(natCaudValidation.length() > 0)
            return natCaudValidation;
        
        String purityValidation = DataValidator.checkPure(Double.valueOf(this.clPurity.getText()));
        if (purityValidation.length() > 0)
            return purityValidation;
        
        String rechargeTimeValidation = DataValidator.checkTiemRecar(Double.valueOf(this.rechargePeriod.getText()));
        if (rechargeTimeValidation.length() > 0)
            return rechargeTimeValidation;
        
        String dripRateValidation = DataValidator.checkHoraGote(Double.valueOf(this.dailyDripRate.getText()));
        if (dripRateValidation.length() > 0)
            return dripRateValidation;
        
        String clValidation = DataValidator.checkDemaClor(Double.valueOf(this.clDemand.getText()));
        if(clValidation.length() >0 )
            return clValidation;
        
        // Everything is a-OK
        return "";
    }

    public void calculateResults() {
        String validationResult = isDataValid();

        if (validationResult.length() > 0) {
            ClAlert alert = new ClAlert(validationResult);
            try {
                alert.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        DataLoader dataloader = DataLoader.getDataLoader();
        
        // Save the purity
        dataloader.setValue("clPurity", clPurity.getText());
        String ph = dataloader.getValue("ph");
        String turbidity = dataloader.getValue("turbidity");
        double population = this.selectedWaterSystem.getPopulation();
        this.selectedWaterSystem.setClimate(this.climateCombo.getValue());

        double futPopulation = Double.valueOf(DataCalculator.getFutPopulation(
                this.selectedWaterSystem.getGrowingIndex(), this.selectedWaterSystem.getPopulation()));

        // TODO: save the futPopulation in the database
        // double futPopulationdb =
        // this.selectedWaterSystem.getPopulationForecast();
        double dota = this.selectedWaterSystem.getEndowment();
        double minCaudal = DataCalculator.caudalMin(population, dota, "20");
        double futCaudal = DataCalculator.caudalMin(futPopulation, dota, "20");
        this.currentFlowRate.setText(String.format("%1$,.2f", minCaudal));
        this.futureFlowRate.setText(String.format("%1$,.2f", futCaudal));

        double reservoirVolume = this.selectedWaterSystem.getReservoirVolume();
        double[] clResults;
        if (this.targetPopulation.getValue().contains("future")) {
            clResults = DataCalculator.chlorination(String.valueOf(futCaudal), this.clPurity.getText(),
                    String.valueOf(reservoirVolume), this.rechargePeriod.getText(), this.dailyDripRate.getText(),
                    this.clDemand.getText(), "3");
        } else {
            clResults = DataCalculator.chlorination(String.valueOf(minCaudal), this.clPurity.getText(),
                    String.valueOf(reservoirVolume), this.rechargePeriod.getText(), this.dailyDripRate.getText(),
                    this.clDemand.getText(), "3");
        }

        this.kgquin.setText(String.format("%1$,.2f", clResults[1]) + " kg/periodo");
        this.kgmes.setText(String.format("%1$,.2f", clResults[2]) + " kg/mes");
        DataLoader.getDataLoader().setValue("kgmes", String.valueOf(clResults[2]));
        this.selectedWaterSystem.setCurrentNeededFlow(Double.valueOf(minCaudal));
        this.selectedWaterSystem.setFutureNeededFlow(Double.valueOf(futCaudal));

        /*
         * TODO: Add a selector after the "calculate" button, so onChange it
         * will toggle between the future and the current caudal for the
         * calculations.
         * 
         */
        String calculatedTankVolume;
        if (this.climateCombo.getValue().equalsIgnoreCase("Calido")) {
            calculatedTankVolume = String.format("%1$,.2f",
                    DataCalculator.volTanCaD(DataCalculator.WARN_CLIMATE_PPM, clResults[1]));
        } else {
            calculatedTankVolume = String.format("%1$,.2f",
                    DataCalculator.volTanCaD(DataCalculator.COLD_CLIMATE_PPM, clResults[1]));
        }
        this.minTankVol.setText(calculatedTankVolume + " litros");
    }

    public void triggerNext() throws Exception {
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());
        Scene future = MainApp.popFuture(ElementsController.class.getSimpleName());
        if (future != null) {
            stage.setScene(future);
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/DesignElements.fxml"));

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
