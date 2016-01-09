package org.ongawa.peru.chlorination.logic.elements;

import org.ongawa.peru.chlorination.logic.SystemElement;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Regular conduction Pipe
 * 
 * @author Alberto Mardomingo
 *
 */
public class Pipe implements SystemElement{
    
    public static final int REQUIRED_CL_QUANTITY = 50;
    public static final int RETENTION_TIME = 2;
    public static final String TYPE_NAME = "Tuberia";
    
    
    private org.ongawa.peru.chlorination.persistence.elements.Pipe dbPipe;
    
    /**
     * Creates a Pipe from a db pipe
     * 
     * @param dbPipe a db connected pipe
     */
    public Pipe (org.ongawa.peru.chlorination.persistence.elements.Pipe dbPipe) {
        this.dbPipe = dbPipe;
    }

    /**
     * Creates a single pipe
     * 
     * @param name - A human readable name
     * @param length - The length of the pipe
     * @param diameter - The diameter of the pipe
     * @param waterSystem - The associated waterSystem
     */
    public Pipe (String name, double length, double diameter, WaterSystem waterSystem) {
        this.dbPipe = new org.ongawa.peru.chlorination.persistence.elements.Pipe(diameter, length, waterSystem);
        this.dbPipe.setCount(1);
    }
    
    /**
     * Creates a number of pipes 
     * 
     * @param name - A human readable name
     * @param length - The length of the pipe
     * @param diameter - The diameter of the pipe
     * @param waterSystem - The associated waterSystem
     * @param count - The number of similar pipes
     */
    public Pipe (String name, double length, double diameter, WaterSystem waterSystem, int count) {
        this.dbPipe = new org.ongawa.peru.chlorination.persistence.elements.Pipe(diameter, length, waterSystem);
        this.dbPipe.setCount(count);
    }
    
    /**
     * Sets the number of elements
     * 
     * @param count
     */
    public void setCount(int count){
        this.dbPipe.setCount(count);
    }
    
    /**
     * Sets the human readable name
     * 
     * @param name
     */
    public void setName(String name){
        this.dbPipe.setName(name);
    }
    
    /**
     * 
     * Returns the volume of a single element
     * 
     * @return double - the volume in m^3
     */
    @Override
    public double getVolume() {
        return this.dbPipe.getVolume();
    }

    /**
     * Returns the human readable name
     * 
     * @return string - the name
     */
    @Override
    public StringProperty getName() {
        return new SimpleStringProperty(dbPipe.getName());
    }
    
    @Override
    public StringProperty getTypeName(){
        return new SimpleStringProperty(TYPE_NAME);
    }

    /**
     * Returns the number of elements in the system
     * 
     * @return int - the elements
     */
    @Override
    public int getCount() {
        return this.dbPipe.getCount();
    }

    /**
     * Returns the combined volume of all the elements in the system
     * 
     * @return double the volume in m^3
     */
    @Override
    public double getCombinedVolume() {
        return this.dbPipe.getCombinedVolume();
    }
    
    @Override
    public int getConcentration(){
        return REQUIRED_CL_QUANTITY;
    }
    
    /*public void setRequiredConcentration(int concentration){
        this.requiredConcentration = concentration;
    }*/
    
    public double getDiameter(){
        return this.dbPipe.getDiameter();
    }
    
    public double getLength(){
        return this.dbPipe.getLength();
    }

    public void save() {
        try {
            IDataSource ds = DataSourceFactory.getInstance().getDefaultDataSource();
            if (this.dbPipe.getPipeId() <0) {
                ds.addPipe(this.dbPipe);
            } else {
                ds.editPipe(this.dbPipe);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
