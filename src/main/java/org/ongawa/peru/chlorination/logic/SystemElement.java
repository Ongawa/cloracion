package org.ongawa.peru.chlorination.logic;

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
     * @return String - A human readable name
     */
    public String getName(); 

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
}
