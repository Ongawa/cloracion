package org.ongawa.peru.chlorination.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

/**
 * 
 * Singleton class to handle the data model.
 * 
 * @author Alberto Mardomingo
 *
 */
public class DataLoader {
    
    private static DataLoader dataLoaderInstance = null;
    
    protected DataLoader() {
        // Empty constructor for the moment 
    }
    
    public static synchronized DataLoader getDataLoader(){
        if (dataLoaderInstance == null) {
            dataLoaderInstance = new DataLoader();
        }
        return dataLoaderInstance;
    }
    
    // TODO: Change all this for some sort of datasource. XML file?
    
    public List<String> getBasins() {
        List<String> basinList = new ArrayList<String>();
        basinList.add("Manzanayocc");
        return basinList;
    }
    
    /**
     * 
     * 
     * 
     * @param basin - The name of the basin we want the cities from.
     * @return
     */
    public List<String> getTowns(String basin) {
        List<String> listToReturn = new ArrayList<String>();
        if (basin.equals("Manzanayocc")){
            listToReturn.add("Nueva Esperanza");
            listToReturn.add("Manzanayocc");
            listToReturn.add("Pampaspata");
            //TODO: fill the others
        }
        
        return listToReturn;
    }
    
    
    public List<String> getSystems(String basin, String town){
        List<String> systemsList = new ArrayList<String>();
        if (basin.equals("Manzanayocc")){
            if (town.equals("Nueva Esperanza")){
                systemsList.add("Nueva Esperanza");
            } else if (town.equals("Manzanayocc")) {
                systemsList.add("Loma");
                systemsList.add("base");
            } else {
                systemsList.add("Sector alto");
                systemsList.add("Sector central");
            }
                
        }
        return systemsList;
    }
    
}
