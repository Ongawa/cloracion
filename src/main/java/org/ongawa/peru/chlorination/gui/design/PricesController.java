package org.ongawa.peru.chlorination.gui.design;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.DataCalculator;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PricesController implements Initializable {
    
    @FXML
    private TextField clPrice;
    
    @FXML
    private TextField nDesinfects;
    
    @FXML
    private Label yearTotal;
    
    @FXML
    private Label monthTotal;
    
    @FXML
    private Label monthlyFamilyPay;
    
    @FXML
    private Label monthlyFamilyPayNoCl;
    
    public void calculateFamilPay() {
        
        DataLoader dataloader = DataLoader.getDataLoader();
        WaterSystem wsystem = dataloader.getSelectedWaterSystem();        
        String families = String.valueOf(wsystem.getFamiliesNum());
        String kgmes = dataloader.getValue("kgmes");
        // TODO: We need to store the desinfect results.
        double[] calcResults = DataCalculator.gastosCl(this.clPrice.getText(), kgmes, this.nDesinfects.getText(), "0", families);
        double[] familyTotals = DataCalculator.cuotaFam(calcResults[0], "0", "0", "0", families);
        this.yearTotal.setText(String.format("%1$,.2f", familyTotals[1]*12) + " soles/año");
        this.monthTotal.setText(String.format("%1$,.2f", familyTotals[1]) + " soles/mes");
        this.monthlyFamilyPay.setText(String.format("%1$,.2f", familyTotals[0]) + " soles/mes");
        this.monthlyFamilyPayNoCl.setText(String.format("%1$,.2f", calcResults[0]) + "soles/mes");
    }
    
    public void triggerBack() {
        
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }
}
