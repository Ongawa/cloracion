package org.ongawa.peru.chlorination.gui.manage;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.HelpStage;
import org.ongawa.peru.chlorination.MainApp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChlorinationWindow  implements Initializable{

    @FXML
    private TextField naturalCaudal;
    
    @FXML
    private TextField chlorableCaudal;
    
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
                if (Integer.parseInt(naturalCaudal.textProperty().get()) < Integer.parseInt(newValue)){
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
    
    public void triggerCalculation(){
        // TODO: Do the calculations
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
