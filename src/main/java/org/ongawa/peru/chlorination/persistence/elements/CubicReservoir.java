package org.ongawa.peru.chlorination.persistence.elements;

/**
 * @author Kiko
 */
public class CubicReservoir extends Reservoir{

	public CubicReservoir(double width, double length, double height, WaterSystem waterSystem) {
		super(width, length, height, waterSystem);
	}

	public CubicReservoir(int reservoirId, double width, double length, double height, WaterSystem waterSystem) {
		super(reservoirId, width, length, height, waterSystem);
	}
}