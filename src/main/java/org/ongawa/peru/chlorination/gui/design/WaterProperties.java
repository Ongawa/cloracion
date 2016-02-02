package org.ongawa.peru.chlorination.gui.design;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.HelpStage;
import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.gui.ClAlert;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.DataValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WaterProperties implements Initializable {

    @FXML
    private TextField waterTurbidity;

    @FXML
    private TextField waterPH;

    public void triggerBack() {
        // Add future
        Scene current = MainApp.getStage().getScene();
        MainApp.pushFuture(this.getClass().getSimpleName(), current);
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }

    public void triggerNext() throws Exception {
        String validationResult = DataValidator.checkWaterQ(waterTurbidity.getText(), waterPH.getText());

        if (validationResult.length() < 1) {
            Stage stage = MainApp.getStage();
            MainApp.pushHistory(stage.getScene());

            Scene future = MainApp.popFuture(CLCalculator.class.getSimpleName());
            if (future != null) {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
    }

}
