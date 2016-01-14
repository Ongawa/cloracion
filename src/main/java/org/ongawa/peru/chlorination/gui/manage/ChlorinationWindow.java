package org.ongawa.peru.chlorination.gui.manage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.HelpStage;
import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.gui.ClAlert;
import org.ongawa.peru.chlorination.logic.DataCalculator;
import org.ongawa.peru.chlorination.logic.DataValidator;

import com.sun.javafx.image.impl.ByteIndexed.Getter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChlorinationWindow  implements Initializable{
    
    
    /* User inputs*/
    @FXML
    private TextField naturalCaudal;
    @FXML
    private TextField chlorableCaudal;
    @FXML
    private TextField clPurity;
    @FXML
    private TextField tankVolume;
    @FXML
    private TextField rechargeTime;
    @FXML
    private TextField dripTime;
    @FXML
    private TextField clDemand;

    /* Results */
    @FXML
    private Label clKgQuin;
    @FXML
    private Label clKgMonth;
    @FXML
    private Label dripMin;
    @FXML
    private Label dripMlMin;

    
    private String basin;
    
    private String town;
    
    private String systemName;
    
    private int familiesCount;
    private int inhabitants;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        this.naturalCaudal.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("Old: "+oldValue);
                System.out.println("New" + newValue);
            }
        });
        
        this.chlorableCaudal.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //I can only chlorate a caudal smaller than the natural one.
                if (Double.valueOf(naturalCaudal.textProperty().get()) < Double.valueOf(newValue)){
                    chlorableCaudal.setStyle("-fx-text-fill: red;");
                    // TODO: Add a text alert
                } else {
                    chlorableCaudal.setStyle("-fx-text-fill: black;");
                }
            }
        });
        
    }
    
    
    
    public String getBasin() {
        return basin;
    }



    public void setBasin(String basin) {
        this.basin = basin;
    }



    public String getTown() {
        return town;
    }



    public void setTown(String town) {
        this.town = town;
    }



    public String getSystemName() {
        return systemName;
    }



    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    
    public TextField getNaturalCaudal() {
        return naturalCaudal;
    }



    public void setNaturalCaudal(TextField naturalCaudal) {
        this.naturalCaudal = naturalCaudal;
    }


    public void setFamiliesCount(int familiesCount) {
        this.familiesCount = familiesCount;
    }
    
    public int getFamiliesCount() {
        return this.familiesCount;
    }

    public int getInhabitants() {
        return inhabitants;
    }


    public void setInhabitants(int inhabitants) {
        this.inhabitants = inhabitants;
    }

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
    
    
    /* General methods */

    /**
     * Checks the values, and shows a popup is the
     * data in the fields is not valid.
     * 
     * @return
     */
    public String isDataValid() {
        ArrayList<String> fieldValues = new ArrayList<String>();
        fieldValues.add(this.naturalCaudal.getText());
        fieldValues.add(this.chlorableCaudal.getText());
        fieldValues.add(this.clPurity.getText());
        fieldValues.add(this.tankVolume.getText());
        fieldValues.add(this.rechargeTime.getText());
        fieldValues.add(this.dripTime.getText());
        fieldValues.add(this.clDemand.getText());
        String formatError =  DataValidator.checkChlorinationData(fieldValues);
        if(formatError.length() < 1 )
            return formatError;
        
        String errorString= DataValidator.checkCaud(Double.valueOf(this.naturalCaudal.getText()));
        if (errorString.length() < 1)
            return errorString;
        errorString = DataValidator.checkCaud(Double.valueOf(this.chlorableCaudal.getText()));
        if (errorString.length() < 1)
            return errorString;
        
        errorString = DataValidator.checkHoraGote(Double.valueOf(this.dripTime.getText()));
        if (errorString.length() < 1)
            return errorString;
        
        errorString = DataValidator.checkTiemRecar(Double.valueOf(this.rechargeTime.getText()));
        if (errorString.length() < 1)
            return errorString;
        
        errorString = DataValidator.checkPure(Double.valueOf(this.clPurity.getText()));
        if (errorString.length() < 1)
            return errorString;
        return "";
    }
    
    public void triggerCalculation(){
        String errorMessage = isDataValid(); 
        if (errorMessage.length() < 1) {
            double[] clResults = DataCalculator.chlorination(this.chlorableCaudal.getText(),
                                this.clPurity.getText(), this.tankVolume.getText(),
                                this.rechargeTime.getText(), this.dripTime.getText(), this.clDemand.getText());
            
            this.clKgQuin.setText(String.format("%1$,.2f", clResults[1]) + " kg/periodo"); //TODO: Change the string to trecarga
            this.clKgMonth.setText(String.format("%1$,.2f", clResults[2]) + " kg/mes");
            
            this.dripMin.setText(String.format("%1$,.2f", clResults[5]) + " gotas/min");
            this.dripMlMin.setText(String.format("%1$,.2f", clResults[4]) + " ml/min");
        } else {
            ClAlert alert = new ClAlert(errorMessage);
            try {
                alert.show();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public void triggerSave() {
        // TODO: Save the results
    }
    
    public void triggerPrint() {
        // TODO: Print the results
    }
   
    public void triggerBack() {
        
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }
}
