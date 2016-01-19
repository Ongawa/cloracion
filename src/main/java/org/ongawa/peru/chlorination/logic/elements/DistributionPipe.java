package org.ongawa.peru.chlorination.logic.elements;

import org.apache.commons.lang.NullArgumentException;
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
public class DistributionPipe extends org.ongawa.peru.chlorination.persistence.elements.DistributionPipe implements SystemElement {
	
	public static final int REQUIRED_CL_QUANTITY = 50;
    public static final int RETENTION_TIME = 2;
	public static final String TYPE_NAME = "Tubería de distribución";
	
	private static Logger log;
	static{
		log = LoggerFactory.getLogger(DistributionPipe.class);
	}
	
	private double[] desinfectionResults;

	public DistributionPipe(double diameter, double length, WaterSystem waterSystem) {
		super(diameter, length, waterSystem);
	}

	public DistributionPipe(int pipeId, double diameter, double length, WaterSystem waterSystem) {
		super(pipeId, diameter, length, waterSystem);
	}
	
	public DistributionPipe(org.ongawa.peru.chlorination.persistence.elements.DistributionPipe distributionPipe){
		super(distributionPipe.getPipeId(), distributionPipe.getDiameter(), distributionPipe.getLength(), distributionPipe.getWaterSystem());
		this.setElementName(distributionPipe.getElementName());
		this.setCount(distributionPipe.getCount());
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
                this.setPipeId(ds.addDistributionPipe(this).getPipeId());
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
}