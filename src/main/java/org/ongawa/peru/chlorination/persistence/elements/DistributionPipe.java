package org.ongawa.peru.chlorination.persistence.elements;

/**
 * 
 * @author kiko
 *
 */
public class DistributionPipe extends Pipe {

	public DistributionPipe(double diameter, double length, WaterSystem waterSystem) {
		super(diameter, length, waterSystem);
	}

	public DistributionPipe(int pipeId, double diameter, double length, WaterSystem waterSystem) {
		super(pipeId, diameter, length, waterSystem);
	}
}