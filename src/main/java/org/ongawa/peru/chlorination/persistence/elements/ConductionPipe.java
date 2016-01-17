package org.ongawa.peru.chlorination.persistence.elements;

public class ConductionPipe extends Pipe {

	public ConductionPipe(double diameter, double length, WaterSystem waterSystem) {
		super(diameter, length, waterSystem);
	}

	public ConductionPipe(int pipeId, double diameter, double length, WaterSystem waterSystem) {
		super(pipeId, diameter, length, waterSystem);
	}
}