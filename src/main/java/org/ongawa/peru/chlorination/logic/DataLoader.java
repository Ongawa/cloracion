package org.ongawa.peru.chlorination.logic;


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
    
}
