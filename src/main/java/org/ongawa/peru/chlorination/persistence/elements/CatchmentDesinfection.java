package org.ongawa.peru.chlorination.persistence.elements;

import org.apache.commons.lang.NullArgumentException;

/**
 * 
 * @author kiko
 *
 */
public class CatchmentDesinfection extends ElementDesinfection{

	private Catchment catchment;
	
	public CatchmentDesinfection(Catchment catchment, Desinfection desinfection, int count, double chlorineQty, double demandSpoons, double retentionTime) {
		super(desinfection, count, chlorineQty, demandSpoons, retentionTime);
		if(catchment == null)
			throw new NullArgumentException("catchment");
		
		this.catchment = catchment;
	}
	
	public Catchment getCatchment(){
		return this.catchment;
	}
}
