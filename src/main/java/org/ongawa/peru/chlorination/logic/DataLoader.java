package org.ongawa.peru.chlorination.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;

import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

import javafx.collections.ObservableList;

/**
 * 
 * Singleton class to handle the data model.
 * 
 * @author Alberto Mardomingo
 *
 */
public class DataLoader {
    
    private static DataLoader dataLoaderInstance = null;

    private SubBasin selectedSubBasin;
    private Community selectedCommunity;
    private WaterSystem selectedWaterSystem;
    
    
    private ObservableList<SystemElement> desinfectResults;
    
    private HashMap<String, String> persistentValues;
    
    protected DataLoader() {
         this.persistentValues = new HashMap<String, String>();
    }
    
    public static synchronized DataLoader getDataLoader(){
        if (dataLoaderInstance == null) {
            dataLoaderInstance = new DataLoader();
        }
        return dataLoaderInstance;
    }

    public ObservableList<SystemElement> getDesinfectResults() {
        return desinfectResults;
    }

    public void setDesinfectResults(ObservableList<SystemElement> desinfectResults) {
        this.desinfectResults = desinfectResults;
    }
    
    public String getValue(String name) {
        if (this.persistentValues.containsKey(name))
            return this.persistentValues.get(name);
        return null;
    }
    
    public void setValue(String keyName, String value){
        this.persistentValues.put(keyName, value);
    }
    
    public SubBasin getSelectedSubBasin() {
        return selectedSubBasin;
    }

    public void setSelectedSubBasin(SubBasin selectedSubBasin) {
        this.selectedSubBasin = selectedSubBasin;
    }

    public Community getSelectedCommunity() {
        return selectedCommunity;
    }

    public void setSelectedCommunity(Community selectedCommunity) {
        this.selectedCommunity = selectedCommunity;
    }

    public WaterSystem getSelectedWaterSystem() {
        return selectedWaterSystem;
    }

    public void setSelectedWaterSystem(WaterSystem selectedWaterSystem) {
        this.selectedWaterSystem = selectedWaterSystem;
    }
}
