package org.ongawa.peru.chlorination.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class AlertController {
    
    @FXML
    private FlowPane alertRoot;
    
    public void closeWindow(final ActionEvent event)  {
        ((Stage) this.alertRoot.getScene().getWindow()).close();
    }

}
