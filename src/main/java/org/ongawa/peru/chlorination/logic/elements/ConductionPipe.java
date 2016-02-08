package org.ongawa.peru.chlorination.logic.elements;

import org.ongawa.peru.chlorination.logic.SystemElement;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * @author kiko
 *
 */
public class ConductionPipe extends org.ongawa.peru.chlorination.persistence.elements.ConductionPipe implements SystemElement {

	public static final int REQUIRED_CL_QUANTITY = 50;
    public static final int RETENTION_TIME = 2;
	public static final String TYPE_NAME = "Tubería de conducción";
	
	private static Logger log;
	static{
		log = LoggerFactory.getLogger(ConductionPipe.class);
	}
	
	private double[] desinfectionResults;
	
	public ConductionPipe(double diameter, double length, WaterSystem waterSystem) {
		super(diameter, length, waterSystem);
	}

	public ConductionPipe(int pipeId, double diameter, double length, WaterSystem waterSystem) {
		super(pipeId, diameter, length, waterSystem);
	}
	
	public ConductionPipe(org.ongawa.peru.chlorination.persistence.elements.ConductionPipe conductionPipe){
		super(conductionPipe.getPipeId(), conductionPipe.getDiameter(), conductionPipe.getLength(), conductionPipe.getWaterSystem());
		this.setElementName(conductionPipe.getElementName());
		this.setCount(conductionPipe.getCount());
	}

	@Override
	public StringProperty getName() {
		return new SimpleStringProperty(this.getElementName());
	}

	@Override
	public StringProperty getTypeName() {
		return new SimpleStringProperty(TYPE_NAME);
	}

	@Override
	public int getConcentration() {
		return REQUIRED_CL_QUANTITY;
	}

	@Override
	public void save() {
		try {
            IDataSource ds = DataSourceFactory.getInstance().getDefaultDataSource();
            if (this.getPipeId() == -1) {
                this.setPipeId(ds.addConductionPipe(this).getPipeId());
            } else {
                ds.editPipe(this);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        	log.warn(e.toString());
        }
	}

	@Override
    public void setDesinfectionResults(double[] desinfectionResults){
        this.desinfectionResults = desinfectionResults;
    }
    
    @Override
    public double[] getDesinfectionResults() {
        return this.desinfectionResults;
    }

    @Override
    public int getRetentionTime() {
        return RETENTION_TIME;
    }
}