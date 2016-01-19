package org.ongawa.peru.chlorination.persistence.elements;

/**
 * 
 * @author kiko
 *
 */
public class Catchment extends Reservoir {

	public Catchment(double width, double length, double height, WaterSystem waterSystem) {
		super(width, length, height, waterSystem);
	}

	public Catchment(int reservoirId, double width, double length, double height, WaterSystem waterSystem) {
		super(reservoirId, width, length, height, waterSystem);
	}
}