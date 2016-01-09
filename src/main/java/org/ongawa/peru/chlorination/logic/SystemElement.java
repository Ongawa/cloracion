package org.ongawa.peru.chlorination.logic;

import javafx.beans.property.StringProperty;

public interface SystemElement {

    
    /**
     * Calculates the volume of this element.
     * 
     * @return the volume in m^3
     */
    public double getVolume();
    
    /**
     * Gives a human readable name for this element
     * 
     * @return StringProperty - A human readable name
     */
    public StringProperty getName();
    
    /**
     * Returns a human readable name for the type of this element
     * 
     * @return 
     */
    public StringProperty getTypeName();

    /**
     * Returns the number of elements like this in the system
     * 
     * @return int - the number of elements 
     */
    
    public int getCount();
    
    
    /**
     * Returns the combined volume of all the elements in the system
     * Equivalent to getVolume()*getCount()
     * 
     */
    public double getCombinedVolume();

    /**
     * Returns the required concentration per element.
     * 
     * @return
     */
    public int getConcentration();
    
    /**
     * Saves the element into the database
     * 
     */
    public void save();
}
