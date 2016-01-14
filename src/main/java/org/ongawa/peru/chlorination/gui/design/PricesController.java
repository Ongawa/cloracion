package org.ongawa.peru.chlorination.gui.design;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.db.DataSource;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.DataCalculator;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.SystemElement;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.CubicreservoirRecord;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoir;
import org.ongawa.peru.chlorination.persistence.elements.Pipe;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValve;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

import javafx.collections.ObservableList;
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
    private TextField repairPay;
    
    @FXML
    private TextField jassPay;
    
    
    @FXML
    private TextField workerPay;
    
    @FXML
    private Label yearTotal;
    
    @FXML
    private Label monthTotal;
    
    @FXML
    private Label monthlyFamilyPay;
    
    @FXML
    private Label monthlyFamilyPayNoCl;
    
    @FXML
    private Label regularCl;
    
    @FXML
    private Label desinfectCl;
    
    public void calculateFamilPay() {
        
        DataLoader dataloader = DataLoader.getDataLoader();
        WaterSystem wsystem = dataloader.getSelectedWaterSystem();        
        String families = String.valueOf(wsystem.getFamiliesNum());
        String kgmes = dataloader.getValue("kgmes");
        // TODO: We need to store the desinfect results.
        double desinfectCLPrice = getDesinfectCL(Double.valueOf(this.clPrice.getText()));
        double[] calcResults = DataCalculator.gastosCl(this.clPrice.getText(), kgmes, this.nDesinfects.getText(), desinfectCLPrice, families);
        double[] familyTotals = DataCalculator.cuotaFam(calcResults[0], this.repairPay.getText(), this.jassPay.getText(), this.workerPay.getText(), families);
        
        this.regularCl.setText(String.format("%1$,.2f", Double.valueOf(kgmes)) + " kg/mes");
        this.desinfectCl.setText("0 kg/desinfección");
        
        this.yearTotal.setText(String.format("%1$,.2f", familyTotals[1]*12) + " soles/año");
        this.monthTotal.setText(String.format("%1$,.2f", familyTotals[1]) + " soles/mes");
        this.monthlyFamilyPay.setText(String.format("%1$,.2f", familyTotals[0]) + " soles/mes");
        this.monthlyFamilyPayNoCl.setText(String.format("%1$,.2f", calcResults[1]) + "soles/mes");
    }
    
    public void triggerBack() {
        
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }
    
    private double getDesinfectCL(double clPrice) {
        double totalRequired = 0;
        try {
            ObservableList<SystemElement> elements = DataLoader.getDataLoader().getDesinfectResults();

            // Calculate the desinfection for every element
            for (SystemElement elem : elements) {
                double[] elemResults = DataCalculator.desinfection(elem.getCount(), elem.getVolume(),
                        elem.getConcentration(), 70);
                double vol = elem.getVolume();
                elem.setDesinfectionResults(elemResults);
                
                // gr to kg
                totalRequired += elemResults[1]/1000;
            }
            

        } catch(Exception e) {
            //TODO: Handle exception 
        }
        
        return totalRequired*clPrice;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }
}
