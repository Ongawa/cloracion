package org.ongawa.peru.chlorination.logic.elements;

import org.ongawa.peru.chlorination.logic.SystemElement;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

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
    
    private org.ongawa.peru.chlorination.persistence.elements.CubicReservoir dbReservoir;

    
    /**
     * Creates a cubic reservoir from a db reservoir
     * 
     * @param dbPipe a db connected reservoir
     */
    public CubicReservoir(org.ongawa.peru.chlorination.persistence.elements.CubicReservoir dbReservoir) {
        this.dbReservoir = dbReservoir;
    }
    
    /**
     * Creates a single Reservoir
     * 
     * @param name - The name of the reservoir
     * @param lehgth
     * @param width
     * @param height
     */
    public CubicReservoir(String name, double length, double width, double height, WaterSystem waterSystem){
        this.dbReservoir = new org.ongawa.peru.chlorination.persistence.elements.CubicReservoir( width, length, height, waterSystem);
        this.dbReservoir.setName(name);
        this.dbReservoir.setCount(1);
    }
    
    /**
     * Creates a number of Reservoirs
     * 
     * @param name - The name of the reservoir
     * @param lehgth
     * @param width
     * @param height
     */
    public CubicReservoir(String name, double length, double width, double height,WaterSystem waterSystem,  int count){
        this.dbReservoir = new org.ongawa.peru.chlorination.persistence.elements.CubicReservoir( width, length, height, waterSystem);
        this.dbReservoir.setName(name);
        this.dbReservoir.setCount(count);
    }
    
    @Override
    public double getVolume() {
        return this.dbReservoir.getVolume();
    }

    @Override
    public StringProperty getName() {
        return new SimpleStringProperty(this.dbReservoir.getName());
    }
    
    @Override
    public StringProperty getTypeName(){
        return new SimpleStringProperty(TYPE_NAME);
    }

    @Override
    public int getCount() {
        return this.dbReservoir.getCount();
    }

    @Override
    public double getCombinedVolume() {
        return this.getCombinedVolume();
    }
    
    @Override
    public int getConcentration(){
        return REQUIRED_CL_QUANTITY;
    }
    
/*    public void setRequiredConcentration(int concentration){
        this.requiredConcentration = concentration;
    }*/
    
    public void setName(String newName) {
        this.dbReservoir.setName(newName);
    }
    
    public double getWidth() {
        return this.dbReservoir.getWidth();
    }

    public void setWidth(double width) {
        this.dbReservoir.setWidth(width);
    }

    public double getHeigtht() {
        return this.dbReservoir.getHeight();
    }

    public void setHeigtht(double height) {
        this.dbReservoir.setHeight(height);
    }
    
    public double getLength() {
        return this.dbReservoir.getLength();
    }
    
    public void setLength(double length) {
        this.dbReservoir.setLength(length);
    }
    

    @Override
    public void save() {
        try {
            IDataSource ds = DataSourceFactory.getInstance().getDefaultDataSource();
            if (this.dbReservoir.getCubbicReservoirId() < 0) {
                this.dbReservoir = ds.addCubicReservoir(this.dbReservoir);
            } else {
                ds.editCubicReservoir(this.dbReservoir);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
                
    }

}
