package org.ongawa.peru.chlorination.persistence.elements;

import org.apache.commons.lang.NullArgumentException;

/**
 * 
 * @author kiko
 *
 */
public class CubicReservoirDesinfection extends ElementDesinfection{

	private CubicReservoir cubicReservoir;
	
	public CubicReservoirDesinfection(CubicReservoir cubicReservoir, Desinfection desinfection, int count, double chlorineQty, double demandSpoons, double retentionTime) {
		super(desinfection, count, chlorineQty, demandSpoons, retentionTime);
		if(cubicReservoir == null)
			throw new NullArgumentException("cubicReservoir");
		
		this.cubicReservoir = cubicReservoir;
	}
	
	public CubicReservoir getCubicReservoir(){
		return this.cubicReservoir;
	}
}