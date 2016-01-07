package org.ongawa.peru.chlorination.gui.design;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.DataValidator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WaterProperties {

    @FXML
    private TextField waterTurbidity;

    @FXML
    private TextField waterPH;

    public void triggerBack() {

        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }

    public void triggerNext() throws Exception {
        String validationResult = DataValidator.checkWaterQ(Double.parseDouble(waterTurbidity.getText()),
                Double.parseDouble(waterPH.getText()));

        if (validationResult.length() < 1) {
            Stage stage = MainApp.getStage();
            MainApp.pushHistory(stage.getScene());
            
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
            // TODO: proper alert
            System.out.println(validationResult);
        }
    }
}
