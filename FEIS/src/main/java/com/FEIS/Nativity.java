package com.FEIS;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Nativity {
	private int statusId = 0;	    
	private String nativity = "";
	
	public Nativity () {}
	public int getStatusId() {return statusId;}
    public void setStatusId(int newValue){statusId = newValue;}
	    
    public String getNativity() {return nativity;}
	public void setNativity(String newValue){nativity = newValue;}
	
	public void select(String id)
	{
	 String SQLStr = "SELECT \"STATUS_ID\",\"NATIVITY\" FROM \"LU_NATIVESTATUS\" WHERE \"STATUS_ID\" = " + id + ";";
	 DbAccess DMan = new DbAccess(SQLStr);
	 ResultSet results =  DMan.ExecuteQuery();
	 if (results != null)
	 {
		  try {
			results.beforeFirst();
			} catch (SQLException e1) {
			
			return;
		  }
	      try {
			while (results.next())
			{
			  try {				
				  this.statusId = results.getInt("STATUS_ID");	
			} catch (SQLException e) {
				this.statusId=0;
			}	
			try {				
				  this.nativity = results.getString("NATIVITY");	
			} catch (SQLException e) {
				  this.nativity = "";
				}					
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	 }
		
	}
	
}


