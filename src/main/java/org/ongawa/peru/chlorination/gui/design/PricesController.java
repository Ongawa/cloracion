package org.ongawa.peru.chlorination.gui.design;

import org.ongawa.peru.chlorination.MainApp;

import javafx.scene.Scene;

public class PricesController {
    
    public void calculateFamilPay() {
        
    }
    
    public void triggerBack() {
        
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }
}
