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
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.CubicreservoirRecord;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoir;
import org.ongawa.peru.chlorination.persistence.elements.Pipe;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValve;
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
    
    public void calculateFamilPay() {
        
        DataLoader dataloader = DataLoader.getDataLoader();
        WaterSystem wsystem = dataloader.getSelectedWaterSystem();        
        String families = String.valueOf(wsystem.getFamiliesNum());
        String kgmes = dataloader.getValue("kgmes");
        // TODO: We need to store the desinfect results.
        double desinfectCL = getDesinfectCL(wsystem);
        double[] calcResults = DataCalculator.gastosCl(this.clPrice.getText(), kgmes, this.nDesinfects.getText(), "0", families);
        double[] familyTotals = DataCalculator.cuotaFam(calcResults[0], this.repairPay.getText(), this.jassPay.getText(), this.workerPay.getText(), families);
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
    
    private double getDesinfectCL(WaterSystem wsystem) {
        try {
            IDataSource ds = DataSourceFactory.getInstance().getDefaultDataSource();
            List<Pipe> pipes = ds.getPipes(wsystem);
            List<CubicReservoir> creservoirs = ds.getCubicReservoirs(wsystem);
            List<ReliefValve> valves = ds.getReliefValves(wsystem);
            
            // TODO: Go over every element and get the grand total of the CL for the desinfection.
        } catch(Exception e) {
            //TODO: Handle exception 
        }
        return 0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }
}
