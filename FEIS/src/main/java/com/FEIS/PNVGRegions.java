package com.FEIS;
import java.io.Serializable;

public class PNVGRegions implements Serializable {
	
	private String regionId = "";
	private String regionDesc = "";
	
	public PNVGRegions () {}
	public String getRegionId() {return regionId;}
    public void setRegionId(String newValue){regionId = newValue;}
	    
    public String getRegionDesc() {return regionDesc;}
	public void setRegionDesc(String newValue){regionDesc = newValue;}

}
