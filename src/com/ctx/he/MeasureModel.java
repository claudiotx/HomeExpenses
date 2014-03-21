package com.ctx.he;

/**
 * @author claudioteixeira
 * This class is our Measure model and contains the data 
 * we will save in the database and show in the user interface
 *
 */
public class MeasureModel {
	// Variables
	private long id;
	private String date;
	private int value;

	// Getters&Setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

}
