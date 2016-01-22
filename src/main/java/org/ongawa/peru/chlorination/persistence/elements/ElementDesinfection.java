package org.ongawa.peru.chlorination.persistence.elements;

import org.apache.commons.lang.NullArgumentException;

/**
 * 
 * @author kiko
 *
 */
public abstract class ElementDesinfection {
	
	private Desinfection desinfection;
	private int count;
	private double chlorineQty;
	private double demandSpoons;
	private double retentionTime;
	
	public ElementDesinfection(Desinfection desinfection, int count, double chlorineQty, double demandSpoons, double retentionTime) {
		if(desinfection == null)
			throw new NullArgumentException("desinfection");
		
		this.desinfection = desinfection;
		this.count = count;
		this.chlorineQty = chlorineQty;
		this.demandSpoons = demandSpoons;
		this.retentionTime = retentionTime;
	}
	
	public Desinfection getDesinfection(){
		return this.desinfection;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getChlorineQty() {
		return chlorineQty;
	}

	public void setChlorineQty(double chlorineQty) {
		this.chlorineQty = chlorineQty;
	}

	public double getDemandSpoons() {
		return demandSpoons;
	}

	public void setDemandSpoons(double demandSpoons) {
		this.demandSpoons = demandSpoons;
	}

	public double getRetentionTime() {
		return retentionTime;
	}

	public void setRetentionTime(double retentionTime) {
		this.retentionTime = retentionTime;
	}
}