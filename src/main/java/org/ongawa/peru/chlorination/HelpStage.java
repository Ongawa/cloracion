package org.ongawa.peru.chlorination;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelpStage extends Stage {

    String fxmlFile;
    public HelpStage(String helpXML) {
        super();
        this.fxmlFile = helpXML;
    }
}
