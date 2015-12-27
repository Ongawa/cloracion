package org.ongawa.peru.chlorination.logic.elements;

import org.ongawa.peru.chlorination.logic.SystemElement;

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
     * The length of this pipe
     */
    private double length;
    
    /**
     * The diameter of the pipe
     */
    private double diameter;
    
    /**
     * The required chlorine concentration
     */
    private int requiredConcentration;
    
    /**
     * Creates a single pipe
     * 
     * @param name - A human readable name
     * @param length - The length of the pipe
     * @param diameter - The diameter of the pipe
     */
    public Pipe (String name, double length, double diameter) {
        this.name  = new SimpleStringProperty(name);
        this.length = length;
        this.diameter = diameter;
        
        // Default count 1
        this.count = 1;
        this.requiredConcentration = REQUIRED_CL_QUANTITY;
        this.typeName = new SimpleStringProperty(TYPE_NAME);
    }
    
    /**
     * Creates a number of pipes 
     * 
     * @param name - A human readable name
     * @param length - The length of the pipe
     * @param diameter - The diameter of the pipe
     * @param count - The number of similar pipes
     */
    public Pipe (String name, double length, double diameter, int count) {
        this.name  = new SimpleStringProperty(name);
        this.length = length;
        this.diameter = diameter;
        this.count = count;
        
        this.requiredConcentration = REQUIRED_CL_QUANTITY;
        this.typeName = new SimpleStringProperty(TYPE_NAME);
    }
    
    /**
     * Sets the number of elements
     * 
     * @param count
     */
    public void setCount(int count){
        this.count = count;
    }
    
    /**
     * Sets the human readable name
     * 
     * @param name
     */
    public void setName(String name){
        this.name.set(name);
    }
    
    /**
     * 
     * Returns the volume of a single element
     * 
     * @return double - the volume in m^3
     */
    @Override
    public double getVolume() {
        double surface = this.diameter * Math.pow(Math.PI, 2) /2;
        return surface*this.length;
    }

    /**
     * Returns the human readable name
     * 
     * @return string - the name
     */
    @Override
    public StringProperty getName() {
        return this.name;
    }
    
    @Override
    public StringProperty getTypeName(){
        return this.typeName;
    }

    /**
     * Returns the number of elements in the system
     * 
     * @return int - the elements
     */
    @Override
    public int getCount() {
        return this.count;
    }

    /**
     * Returns the combined volume of all the elements in the system
     * 
     * @return double the volume in m^3
     */
    @Override
    public double getCombinedVolume() {
        return getVolume()*this.count;
    }
    
    @Override
    public int getConcentration(){
        return this.requiredConcentration;
    }
    
    public void setRequiredConcentration(int concentration){
        this.requiredConcentration = concentration;
    }
    
    public double getDiameter(){
        return this.diameter;
    }
    
    public double getLength(){
        return this.length;
    }

}
