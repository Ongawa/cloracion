package org.ongawa.peru.chlorination.gui.desinfect;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.SystemElement;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
    
  
    private ObservableList<SystemElement> resultElements;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        DataLoader loader = DataLoader.getDataLoader();
        
        this.resultElements = loader.getDesinfectResults();
        this.resultsTable.setItems(resultElements);
        
        this.nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeName());
        this.elementCountColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getCount())));
    }
    
    public void triggerBack() {
        
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }

}
