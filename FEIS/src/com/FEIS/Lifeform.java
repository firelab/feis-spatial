package com.FEIS;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/*
 * Lifeform lookup data for types of animal, plant fungus and linchen 
 */
import java.util.List;

public class Lifeform {
	private int lifeformId = 0;	    
	private String lifeform = "";
	
	public Lifeform() {}
	
	public int getLifeformId() {return lifeformId;}
    public void setLifeformId(int newValue){lifeformId = newValue;}
	    
    public String getLifeform() {return lifeform;}
	public void setLifeform(String newValue){lifeform = newValue;}
	
	
	public void select(String id)
	{
		int lifeformId = 0;
		 try {
			 lifeformId = Integer.parseInt(id);
		 }
		 catch(Exception ex) { return; }
		 String SQLStr = "SELECT \"LIFEFORM_ID\",\"LIFEFORM\" FROM \"LU_LIFEFORM\" WHERE \"LIFEFORM_ID\" = " + lifeformId + ";";
	
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
					  this.lifeformId = results.getInt("LIFEFORM_ID");	
					} catch (SQLException e) {						
						this.lifeformId=0;
					}	
					try {				
						  this.lifeform = results.getString("LIFEFORM");	
					} catch (SQLException e) {						
						  this.lifeform = "";
					}					
				}
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
	     }
	}
	
	

}
