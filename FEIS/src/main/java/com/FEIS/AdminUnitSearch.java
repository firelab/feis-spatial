package com.FEIS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminUnitSearch {
	private int adminId = 0;	    
	private String names1 = "";
	private final Logger log = Logger.getLogger(this.getClass().getName());
	
	public AdminUnitSearch () {}
	public int getAdminId() {return adminId;}
    public void setLifeformId(int newValue){adminId = newValue;}
	    
    public String getNames1() {return names1;}
	public void setNames1(String newValue){names1 = newValue;}
	
	public ResultSet getAgencies() // dropdown 1 - select an agency
	{
	    String SQLStr = "SELECT \"AGENCY_ABB\", \"AGENCY\" FROM \"LU_AGENCY\" ORDER BY \"LU_AGENCY\".\"AGENCY\";";
        DbAccess DMan = new DbAccess(SQLStr);
	    return DMan.ExecuteQuery(); 
	}
	
	public ResultSet getRegions(String selectedAgencyAbb) // dropdown 2 - select a region
	{
		String SQLStr = "SELECT " +
		   "\"LU_AGENCYREGIONS\".\"REGION_ID\", "  + 
		   "\"LU_AGENCYREGIONS\".\"REGION_DESC\" "  + 
		  "FROM \"LU_AGENCYREGIONS\" WHERE " + 		 		 
		  "\"LU_AGENCYREGIONS\".\"AGENCY_ABB\" = '" + selectedAgencyAbb + "' ORDER BY \"LU_AGENCYREGIONS\".\"SORT_ORDER\";";	
		  //"\"LU_AGENCYREGIONS\".\"AGENCY_ABB\" = '" + selectedAgencyAbb + "' ORDER BY \"LU_AGENCYREGIONS\".\"REGION_DESC\";";		
		
        DbAccess DMan = new DbAccess(SQLStr);
	    return DMan.ExecuteQuery(); 
	}
	
	public ResultSet getUnitNames(String selectedAgency, String selectedRegion) // dropdown 3 - select Unit Name
	{
		String SQLStr = "";
		SQLStr = "SELECT " +
	    "\"ADMIN\".\"ADMIN_ID\", "  + 
	    "\"ADMIN\".\"NAME1\" "  + 		  
	    "FROM \"ADMIN\" WHERE " + 
	    "\"AGENCY_ABB\" = '" + selectedAgency + "' AND \"REGION_ID\" =" + selectedRegion + " ORDER BY \"ADMIN\".\"NAME1\";";	
		
		System.out.println(SQLStr);
		DbAccess DMan = new DbAccess(SQLStr);
		return DMan.ExecuteQuery(); 	
	}
	
	public String getCSVUnitNames(String selectedAgency, String selectedRegion)
	{
		 String adminIds = "";
		
		 ResultSet results =  getUnitNames(selectedAgency, selectedRegion);
		 if (results != null)
		 {
			  try {
				results.beforeFirst();
				} catch (SQLException e1) {
				
				return adminIds;
			  }
		      try {
				while (results.next())
				{
				  try {				
					  int adminId = results.getInt("ADMIN_ID");	
				  adminIds = adminIds.trim()  + adminId + ",";
				} catch (SQLException e) {
					log.log(Level.SEVERE,"getAllUnitNames: " + e.getMessage());
				}									
			}
			
		} catch (SQLException e) {
			 log.log(Level.SEVERE,"getAllUnitNames: " + e.getMessage());
		}
		}
		
		return adminIds;
		
	}
	
	
	public Map<Integer, String> getUnitNames(String[] ids) {
		
		Map<Integer, String> ret = new HashMap<Integer, String>();
		try { 
		String inList = "";
		for (String id : ids) {
			try {
				int adminId = Integer.parseInt(id.trim());
				if(adminId != 0) { 
					inList+=adminId + ",";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(inList.length() > 0) { 
			inList = inList.substring(0, inList.length()-1);
		}
		String sql = "Select distinct \"ADMIN_ID\", \"NAME1\" from \"ADMIN\" where \"ADMIN_ID\" in (" + inList + ")";
		 DbAccess DMan = new DbAccess(sql);
		 ResultSet results =  DMan.ExecuteQuery();
		 if (results != null)
	     {

				while (results.next())
				{
					  ret.put(results.getInt("ADMIN_ID"), results.getString("NAME1"));	
				}
	     }
		 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ret; 
	}
	
	
	
	public void getUnitName(String id) // description for unit name
	{
		int adminId = 0;
		 try {
			 adminId = Integer.parseInt(id);
		 }
		 catch(Exception ex) { return; }
		 String SQLStr = "SELECT \"ADMIN_ID\",\"NAME1\" FROM \"ADMIN\" WHERE \"ADMIN_ID\" = " + adminId + ";";
		 
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
					  this.adminId = results.getInt("ADMIN_ID");	
					} catch (SQLException e) {
						this.adminId=0;
					}	
					try {				
						  this.names1 = results.getString("NAME1");	
					} catch (SQLException e) {
						  this.names1 = "";
					}					
				}
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
	     }
	}
	
	

}
