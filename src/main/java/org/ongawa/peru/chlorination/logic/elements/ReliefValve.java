package org.ongawa.peru.chlorination.logic.elements;

import org.ongawa.peru.chlorination.logic.SystemElement;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReliefValve implements SystemElement {

    public static final int REQUIRED_CL_QUANTITY = 200;
    public static final String TYPE_NAME = "CPR";

    private org.ongawa.peru.chlorination.persistence.elements.ReliefValve dbValve;
    
    private double[] desinfectionResults;

    
    /***
     * 
     * Creates a valve from the db
     * 
     * @param the db connection
     * 
     */
    public ReliefValve(org.ongawa.peru.chlorination.persistence.elements.ReliefValve dbValve) {
        this.dbValve = dbValve;
    }
    
    /**
     * Creates a single Relief Valve
     * 
     * @param name
     *            - The name of the reservoir
     * @param lehgth
     * @param width
     * @param height
     */
    public ReliefValve(String name, double length, double width, double height, WaterSystem waterSystem) {
        this.dbValve = new org.ongawa.peru.chlorination.persistence.elements.ReliefValve(width, length, height, waterSystem);
        this.dbValve.setName(name);
        this.dbValve.setCount(1);
    }

    /**
     * Creates a number of Reservoirs
     * 
     * @param name
     *            - The name of the reservoir
     * @param lehgth
     * @param width
     * @param height
     */
    public ReliefValve(String name, double length, double width, double height,  WaterSystem waterSystem, int count) {
        this.dbValve = new org.ongawa.peru.chlorination.persistence.elements.ReliefValve(width, length, height, waterSystem);
        this.dbValve.setName(name);
        this.dbValve.setCount(count);
    }

    @Override
    public double getVolume() {
        return this.dbValve.getVolume();
    }

    @Override
    public StringProperty getName() {
        return new SimpleStringProperty(this.dbValve.getName());
    }
    
    public void setName(String name) {
        this.dbValve.setName(name);
    }
    

    @Override
    public StringProperty getTypeName() {
        return new SimpleStringProperty(TYPE_NAME);
    }

    @Override
    public int getCount() {
        return this.dbValve.getCount();
    }

    @Override
    public double getCombinedVolume() {
        return this.dbValve.getCombinedVolume();
    }

    @Override
    public int getConcentration() {
        return REQUIRED_CL_QUANTITY;
    }

    public double getLength() {
        return this.dbValve.getLength();
    }

    public void setLength(double length) {
        this.dbValve.setLength(length);
    }

    public double getWidth() {
        return this.dbValve.getHeight();
    }

    public void setWidth(double width) {
        this.dbValve.setWidth(width);
    }

    public double getHeigtht() {
        return this.dbValve.getHeight();
    }

    public void setHeigtht(double height) {
        this.dbValve.setHeight(height);
    }
    

    @Override
    public void setDesinfectionResults(double[] desinfectionResults){
        this.desinfectionResults = desinfectionResults;
    }
    
    @Override
    public double[] getDesinfectionResults() {
        return this.desinfectionResults;
    }
    
    public org.ongawa.peru.chlorination.persistence.elements.ReliefValve getDbValve() {
        return this.dbValve;
    }

    @Override
    public void save() {
        try {
            IDataSource ds = DataSourceFactory.getInstance().getDefaultDataSource();
            if (this.dbValve.getReliefValveId() < 0) {
                // the valve did not exist in the db
                this.dbValve = ds.addReliefValve(this.dbValve);
            } else{
                // update
                ds.editReliefValve(this.dbValve);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
