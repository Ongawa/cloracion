package org.ongawa.peru.chlorination.persistence.elements;

import org.apache.commons.lang.NullArgumentException;

/**
 * 
 * @author kiko
 *
 */
public class PipeDesinfection extends ElementDesinfection{
	
	private Pipe pipe;

	public PipeDesinfection(Pipe pipe, Desinfection desinfection, int count, double chlorineQty, double demandSpoons, double retentionTime) {
		super(desinfection, count, chlorineQty, demandSpoons, retentionTime);
		if(pipe == null)
			throw new NullArgumentException("pipe");
		
		this.pipe = pipe;
	}
	
	public Pipe getPipe(){
		return this.pipe;
	}
}
