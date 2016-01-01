package org.ongawa.peru.chlorination.gui.design;

import org.ongawa.peru.chlorination.MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WaterProperties {
 
    public void triggerBack() {
        
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }
    
    public void triggerNext() throws Exception{
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());

        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/CLCalculation.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);

    }
}
