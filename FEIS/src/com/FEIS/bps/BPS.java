package com.FEIS.bps;

public class BPS 
{
	String name; 
	int id;
	public BPS(int int1, String string) {
		id = int1;
		name = string;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	} 
}