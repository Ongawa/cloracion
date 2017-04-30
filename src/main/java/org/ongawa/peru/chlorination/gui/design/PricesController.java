package org.ongawa.peru.chlorination.gui.design;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.db.DataSource;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.DataCalculator;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.SystemElement;
import org.ongawa.peru.chlorination.modules.reports.DesignReport;
import org.ongawa.peru.chlorination.modules.reports.DesinfectionReport;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.CubicreservoirRecord;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoir;
import org.ongawa.peru.chlorination.persistence.elements.Pipe;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValve;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

import com.itextpdf.text.DocumentException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    
    @FXML
    private Button printButton;
    
    private String clType;
    
    public void calculateFamilPay() {
        
        DataLoader dataloader = DataLoader.getDataLoader();
        WaterSystem wsystem = dataloader.getSelectedWaterSystem();        
        String families = String.valueOf(wsystem.getFamiliesNum());
        String kgmes = dataloader.getValue("kgmes");
        // TODO: We need to store the desinfect results.
        //double desinfectCLPrice = getDesinfectCL(Double.valueOf(this.clPrice.getText()));
        double[] calcResults = DataCalculator.gastosCl(this.clPrice.getText(), kgmes, this.nDesinfects.getText(), getDesinfectCL(), families);
        double[] familyTotals = DataCalculator.cuotaFam(calcResults[0], this.repairPay.getText(), this.jassPay.getText(), this.workerPay.getText(), families);
        
        ObservableList<SystemElement> desResults = dataloader.getDesinfectResults();
        double clPurity = Double.valueOf(dataloader.getValue("clPurity"));
        double kgDesinfect = 0;
        for (SystemElement elem: desResults) {
            double[] elemResults = DataCalculator.desinfection(elem.getCount(), elem.getVolume(),
                    elem.getConcentration(), clPurity);
            double vol = elem.getVolume();
            elem.setDesinfectionResults(elemResults);
            
            // gr to kg
            kgDesinfect += elemResults[1]/1000;        }
        this.regularCl.setText(String.format("%1$,.2f", Double.valueOf(kgmes)) + " kg/mes");
        this.desinfectCl.setText(String.format("%1$,.2f",kgDesinfect)+ " kg/desinfección");;
        
        this.yearTotal.setText(String.format("%1$,.2f", familyTotals[1]*12) + " soles/año");
        this.monthTotal.setText(String.format("%1$,.2f", familyTotals[1]) + " soles/mes");
        this.monthlyFamilyPay.setText(String.format("%1$,.2f", familyTotals[0]) + " soles/mes");
        this.monthlyFamilyPayNoCl.setText(String.format("%1$,.2f", calcResults[1]) + "soles/mes");
        
        // Set all the needed values for the report
        // TODO: WE really need to refactor this.
        dataloader.setValue("solescl", String.format("%1$,.2f", Double.valueOf(kgmes)*Double.valueOf(this.clPrice.getText())));
        dataloader.setValue("solesDes", String.format("%1$,.2f", getDesinfectCL()*Double.valueOf(this.nDesinfects.getText())));
        
        dataloader.setValue("sapSpares", this.repairPay.getText());
        dataloader.setValue("jassManage", this.jassPay.getText());
        dataloader.setValue("workerPay", this.workerPay.getText());
        dataloader.setValue("yearTotal", String.format("%1$,.2f", familyTotals[1]*12));
        
        dataloader.setValue("justCL", String.format("%1$,.2f", calcResults[1] * 12 * Double.valueOf(families)));
        dataloader.setValue("famCuot", String.format("%1$,.2f", familyTotals[0] * 12) );
        
    }
    
    public void triggerBack() {
        //  Add future
        Scene current =  MainApp.getStage().getScene();
        MainApp.pushFuture(this.getClass().getName(), current);
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }
    
    private double getDesinfectCL() {
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
        
        return totalRequired;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        this.clType = DataLoader.getDataLoader().getValue("clType");
    }
    
    public void triggerSave(){
        // Enable the print button
        this.printButton.setDisable(false);
    }

    public void triggerPrint() {
                
        // TODO: Print the results
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                DesignReport dreport = new DesignReport( DataLoader.getDataLoader().getSelectedWaterSystem(),
                                                         file, new Locale("es", "ES"), ""); 
                dreport.createReport();
                // Open the file with the default editor
                Thread t = new Thread(new Runnable() {
                    
                    @Override
                    public void run() {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException | DocumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
}
