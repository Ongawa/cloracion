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
public class Catchment extends org.ongawa.peru.chlorination.persistence.elements.Catchment implements SystemElement {

	public static final int REQUIRED_CL_QUANTITY = 50;
	public static final int RETENTION_TIME = 2;
	public static final String TYPE_NAME = "Captación";
	
	private static Logger log;
	static{
		log = LoggerFactory.getLogger(ConductionPipe.class);
	}
	
	private double[] desinfectionResults;
	
	public Catchment(double width, double length, double height, WaterSystem waterSystem) {
		super(width, length, height, waterSystem);
	}

	public Catchment(int reservoirId, double width, double length, double height, WaterSystem waterSystem) {
		super(reservoirId, width, length, height, waterSystem);
	}
	
	public Catchment(org.ongawa.peru.chlorination.persistence.elements.Catchment catchment){
		super(catchment.getReservoirId(), catchment.getWidth(), catchment.getLength(), catchment.getHeight(), catchment.getWaterSystem());
		this.setElementName(catchment.getElementName());
		this.setCount(catchment.getCount());
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
    
    public void setName(String newName) {
        this.setElementName(newName);
    }
    
    public org.ongawa.peru.chlorination.persistence.elements.Catchment getDbCatchment() {
        return this;
    }

	@Override
	public void save() {
		try {
            IDataSource ds = DataSourceFactory.getInstance().getDefaultDataSource();
            if (this.getReservoirId() == -1) {
                this.setReservoirId(ds.addCatchment(this).getReservoirId());
            } else {
                ds.editCatchment(this);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        	log.warn(e.toString());
        }
	}

	@Override
	public void setDesinfectionResults(double[] results) {
		this.desinfectionResults = results;
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