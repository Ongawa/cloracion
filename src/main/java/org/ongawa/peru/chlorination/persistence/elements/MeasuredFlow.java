package org.ongawa.peru.chlorination.persistence.elements;

import java.sql.Timestamp;

public class MeasuredFlow {
	/**
	 * @author Kiko
	 */
	
	private Timestamp date;
	private double flow;
	private MeasuringPoint measuringPoint;
	private String comments;
	
	public MeasuredFlow(Timestamp date, double flow, MeasuringPoint measuringPoint){
		super();
		this.date = date;
		this.flow = flow;
		this.measuringPoint = measuringPoint;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public double getFlow() {
		return flow;
	}

	public void setFlow(double flow) {
		this.flow = flow;
	}

	public MeasuringPoint getMeasuringPoint() {
		return measuringPoint;
	}

	public void setMeasuringPoint(MeasuringPoint measuringPoint) {
		this.measuringPoint = measuringPoint;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String toString(){
		return "Measured flow: "+this.getFlow()+"lps on "+this.getDate()+" in Measuring point "+this.measuringPoint+" (Comments: "+this.getComments()+")";
	}
}
