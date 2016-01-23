package org.ongawa.peru.chlorination.persistence.elements;

import org.apache.commons.lang.NullArgumentException;

/**
 * 
 * @author kiko
 *
 */
public class ReliefValveDesinfection extends ElementDesinfection{

	private ReliefValve reliefValve;
	
	public ReliefValveDesinfection(ReliefValve reliefValve, Desinfection desinfection, int count, double chlorineQty, double demandSpoons, double retentionTime) {
		super(desinfection, count, chlorineQty, demandSpoons, retentionTime);
		
		if(reliefValve == null)
			throw new NullArgumentException("reliefValve");
		
		this.reliefValve = reliefValve;
	}
	
	public ReliefValve getReliefValve(){
		return this.reliefValve;
	}
}