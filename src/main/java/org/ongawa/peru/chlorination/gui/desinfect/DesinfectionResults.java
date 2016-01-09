package org.ongawa.peru.chlorination.gui.desinfect;

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.DataCalculator;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.SystemElement;
import org.ongawa.peru.chlorination.logic.elements.Pipe;
import org.ongawa.peru.chlorination.logic.elements.ReliefValve;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.logic.elements.CubicReservoir;
import org.ongawa.peru.chlorination.persistence.elements.CubicReservoirDesinfection;
import org.ongawa.peru.chlorination.persistence.elements.PipeDesinfection;
import org.ongawa.peru.chlorination.persistence.elements.ReliefValveDesinfection;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DesinfectionResults implements Initializable {

    @FXML
    TableView<SystemElement> resultsTable;

    @FXML
    TableColumn<SystemElement, String> nameColumn;

    @FXML
    TableColumn<SystemElement, String> typeColumn;

    @FXML
    TableColumn<SystemElement, String> elementCountColumn;

    @FXML
    TableColumn<SystemElement, String> clRequiredColumn;

    @FXML
    TableColumn<SystemElement, String> spoonsRequiredColumn;

    @FXML
    TableColumn<SystemElement, String> timeRequiredColumn;

    @FXML
    private Label clTotal;

    private ObservableList<SystemElement> resultElements;

    private List<String[]> desinfectResults;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        DataLoader loader = DataLoader.getDataLoader();

        this.resultElements = loader.getDesinfectResults();
        this.resultsTable.setItems(resultElements);

        double totalRequired = 0;

        // Calculate the desinfection for every element
        for (SystemElement elem : resultElements) {
            double[] elemResults = DataCalculator.desinfection(elem.getCount(), elem.getVolume(),
                    elem.getConcentration(), 70);
            elem.setDesinfectionResults(elemResults);
            totalRequired += elemResults[1];
        }

        this.nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeName());
        this.elementCountColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCount())));
        this.clRequiredColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("%1$,.2f",cellData.getValue().getDesinfectionResults()[1])));
        this.spoonsRequiredColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("%1$,.2f",cellData.getValue().getDesinfectionResults()[2])));
        this.timeRequiredColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Por calcular"));
        this.elementCountColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getCount())));

        this.clTotal.setText(String.format("%1$,.2f", totalRequired));

    }

    public void triggerBack() {

        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }

    public void saveDesinfection() {
        IDataSource ds;
        try {
            ds = DataSourceFactory.getInstance().getDefaultDataSource();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            for (SystemElement element : this.resultElements) {
                
                if (element.getClass().equals(Pipe.class)) {
                    PipeDesinfection pdesinfect = new PipeDesinfection(now,
                            ((Pipe) element).getDbPipe());
                    ds.addPipeDesinfection(pdesinfect);

                } else if (element.getClass().equals(CubicReservoir.class)) {
                    CubicReservoirDesinfection reDesinfect = new CubicReservoirDesinfection(now, ((CubicReservoir) element).getDbReservoir());
                    ds.addCubicReservoirDesinfection(reDesinfect);

                } else {
                    ReliefValveDesinfection valDesinfect = new ReliefValveDesinfection(now, ((ReliefValve) element).getDbValve());
                    ds.addReliefValveDesinfection(valDesinfect);
                }
                    
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
