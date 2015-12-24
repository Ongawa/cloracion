package org.ongawa.peru.chlorination.persistence.elements;

import java.time.LocalDateTime;

public class MeasuredFlow {
	/**
	 * @author Kiko
	 */
	
	private int measuredFlowId;
	private LocalDateTime date;
	private double flow;
	private MeasuringPoint measuringPoint;
	private String comments;
	
	public MeasuredFlow(int measuredFlowId, LocalDateTime date, double flow, MeasuringPoint measuringPoint){
		super();
		this.measuredFlowId = measuredFlowId;
		this.date = date;
		this.flow = flow;
		this.measuringPoint = measuringPoint;
	}
	
	public MeasuredFlow(LocalDateTime date, double flow, MeasuringPoint measuringPoint){
		super();
		this.measuredFlowId = -1;
		this.date = date;
		this.flow = flow;
		this.measuringPoint = measuringPoint;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
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

	public int getMeasuredFlowId() {
		return measuredFlowId;
	}
}
