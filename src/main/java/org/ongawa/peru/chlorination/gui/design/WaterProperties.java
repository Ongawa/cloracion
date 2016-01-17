package org.ongawa.peru.chlorination.gui.design;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.gui.ClAlert;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.DataValidator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WaterProperties implements Initializable{

    @FXML
    private TextField waterTurbidity;

    @FXML
    private TextField waterPH;

    public void triggerBack() {
        // Add future
        Scene current =  MainApp.getStage().getScene();
        MainApp.pushFuture(this.getClass().getSimpleName(), current);
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }

    public void triggerNext() throws Exception {
        String validationResult = DataValidator.checkWaterQ(waterTurbidity.getText(),waterPH.getText());
        
        if (validationResult.length() < 1) {
            Stage stage = MainApp.getStage();
            MainApp.pushHistory(stage.getScene());
            
            Scene future  = MainApp.popFuture(CLCalculator.class.getSimpleName());
            if (future != null){
                stage.setScene(future);
                return;
            }
            
            DataLoader dataloader = DataLoader.getDataLoader();
            // TODO: set as static
            dataloader.setValue("ph", waterPH.getText());
            dataloader.setValue("turbidity", waterTurbidity.getText());

            FXMLLoader loader = new FXMLLoader();
            Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/CLCalculation.fxml"));

            Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
            scene.getStylesheets().add("/styles/styles.css");
            stage.setScene(scene);
        } else {
            ClAlert alert = new ClAlert(validationResult);
            alert.show();
            System.out.println(validationResult);
        }
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
    }
    
    
}
