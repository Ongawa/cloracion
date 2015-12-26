package org.ongawa.peru.chlorination.logic.elements;

import org.ongawa.peru.chlorination.logic.SystemElement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * A cubic reservoir for the system.
 * 
 * @author Alberto Mardomingo
 *
 */
public class CubicReservoir implements SystemElement{

    public static final int REQUIRED_CL_QUANTITY = 200;
    public static final String TYPE_NAME = "Reservorio";
    
    /**
     * A human readable name
     */
    private StringProperty name;
    
    /**
     * The type name human readable.
     * 
     */
    private StringProperty typeName;
    
    /**
     * The number of similar elements
     */
    private int count;
    
    /**
     * Dimensions of the tank to get the volume
     * 
     */
    private double length;
    private double width;
    private double heigtht;
    
    /**
     * The required chlorine concentration
     */
    private int requiredConcentration;
    
    
    /**
     * Creates a single Reservoir
     * 
     * @param name - The name of the reservoir
     * @param lehgth
     * @param width
     * @param height
     */
    public CubicReservoir(String name, double lehgth, double width, double height){
        this.name = new SimpleStringProperty(name);
        this.count = 1;
        this.length = lehgth;
        this.width = width;
        this.heigtht = height;
        
        this.requiredConcentration = REQUIRED_CL_QUANTITY;
        this.typeName = new SimpleStringProperty(TYPE_NAME);
    }
    
    /**
     * Creates a number of Reservoirs
     * 
     * @param name - The name of the reservoir
     * @param lehgth
     * @param width
     * @param height
     */
    public CubicReservoir(String name, double lehgth, double width, double height, int count){
        this.name = new SimpleStringProperty(name);
        this.count = 1;
        this.length = lehgth;
        this.width = width;
        this.heigtht = height;
        
        this.requiredConcentration = REQUIRED_CL_QUANTITY;
        this.typeName = new SimpleStringProperty(TYPE_NAME);
    }
    
    @Override
    public double getVolume() {
        return this.heigtht*this.length*this.width;
    }

    @Override
    public StringProperty getName() {
        return this.name;
    }
    
    @Override
    public StringProperty getTypeName(){
        return this.typeName;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public double getCombinedVolume() {
        return this.getVolume()*this.count;
    }
    
    @Override
    public int getConcentration(){
        return this.requiredConcentration;
    }
    
    public void setRequiredConcentration(int concentration){
        this.requiredConcentration = concentration;
    }
    
    public void setName(String newName) {
        this.name.set(newName);
    }

}
