package com.FEIS;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * PNVG data for plant communities
 */
public class PNVGSearch {
	private final Logger log = Logger.getLogger(this.getClass().getName());
	public PNVGSearch (){}
	
	// Region - dropdown1
	public ResultSet getAllRegions()
	{
		
	    //String SQLStr = "SELECT \"REGIONS_ID\", \"REGION\" FROM \"LU_PNVGREGIONS\" ORDER BY \"REGION\";";
	    String SQLStr = "SELECT \"BPSREGION_ID\", \"BPSREGION\" FROM \"LU_BPSREGIONS\" ORDER BY \"BPSREGION\";";
		DbAccess DMan = new DbAccess(SQLStr);
	    return DMan.ExecuteQuery(); 
	}
	// Vegtype - dropdown2
	public ResultSet getAllVegType()
	{		
	    String SQLStr = "SELECT \"FORMATION_ID\", \"FORMATION\"  FROM \"LU_BPSFORMATIONS\" ORDER BY \"FORMATION\";";
        DbAccess DMan = new DbAccess(SQLStr);
	    return DMan.ExecuteQuery(); 
	}

	// input PNVG from choices above dropdown1 and dropdown2
	public ResultSet getMatchingPNVG(String region_id, String vegtype_id)
	{
		/*
		String SQLStr = "SELECT " +
				  "\"BPS\".\"BIOPHYSICAL_SETTING_NAME\" "  + 		  
				  "FROM \"LU_BPSREGIONS\", \"BPS\", \"LU_BPSFORMATIONS\" WHERE " + 
				  "\"LU_BPSREGIONS\".\"BPSREGION_ID\" = \"BPS\".\"BPSREGION_ID\" AND " +
				  "\"LU_BPSFORMATIONS\".\"FORMATION_ID\" = \"BPS\".\"FORMATION_ID\" AND " +
				  "\"BPS\".\"FORMATION_ID\" IN ('" + vegtype_id + "') AND " +
				  "\"BPS\".\"BPSREGION_ID\" IN ('" + region_id + "') ORDER BY \"BPS\".\"BIOPHYSICAL_SETTING_NAME\";";
		*/
		
		//took this out because querying for the bps code makes it bring back too many results
		
		
		String SQLStr = "SELECT " +
		  "\"BPS\".\"BPS_CODE\", "  + 
		  "\"BPS\".\"BIOPHYSICAL_SETTING_NAME\", "  + 	
		  "CONCAT(\"BPS\".\"BPS_CODE\",CONCAT(' ', \"BPS\".\"BIOPHYSICAL_SETTING_NAME\")) as DISPLAY " +
		  "FROM \"LU_BPSREGIONS\", \"BPS\", \"LU_BPSFORMATIONS\" WHERE " + 
		  "\"LU_BPSREGIONS\".\"BPSREGION_ID\" = \"BPS\".\"BPSREGION_ID\" AND " +
		  "\"LU_BPSFORMATIONS\".\"FORMATION_ID\" = \"BPS\".\"FORMATION_ID\" AND " +
		  "\"BPS\".\"FORMATION_ID\" IN ('" + vegtype_id + "') AND " +
		  "\"BPS\".\"BPSREGION_ID\" IN ('" + region_id + "') ORDER BY \"BPS\".\"BIOPHYSICAL_SETTING_NAME\", \"BPS\".\"BPS_CODE\";";
		
		
		//this is the original before the bps changes...
		
		/*
		String SQLStr = "SELECT " +
				   "\"PNVG_REGIONS\".\"PNVG_CODE\", "  + 
				  "\"PNVG\".\"PNVG_DESC\" "  + 		  
				  "FROM \"LU_PNVGREGIONS\", \"PNVG_REGIONS\", \"PNVG\" WHERE " + 
				  "\"LU_PNVGREGIONS\".\"REGIONS_ID\" = \"PNVG_REGIONS\".\"REGION_ID\" AND " +
				  "\"PNVG_REGIONS\".\"PNVG_CODE\" = \"PNVG\".\"PNVG_CODE\" AND " +
				  "\"VEGTYPE_ID\" IN ('" + vegtype_id + "') AND " +
				  "\"REGION_ID\" IN ('" + region_id + "') ORDER BY \"PNVG\".\"PNVG_DESC\";";
		*/
		
         DbAccess DMan = new DbAccess(SQLStr);
        
	    return DMan.ExecuteQuery(); 
	}
	public String getCSVPnvgs(String region_id, String vegtype_id)
	{
		 String pnvgs = "";
		 
		 ResultSet results =  getMatchingPNVG(region_id, vegtype_id); 
		 if (results != null)
		 {
			  try {
				results.beforeFirst();
				} catch (SQLException e1) {
				
				return pnvgs;
			  }
		      try {
				while (results.next())
				{
				  try {
					  pnvgs = pnvgs.trim() + "" + results.getString("BPS_CODE") + ",";
				} catch (SQLException e) {						
					log.log(Level.SEVERE,"getCSVPnvgs: " + e.getMessage(), e);
				}									
			}
			
		} catch (SQLException e) {				
			 log.log(Level.SEVERE,"getCSVPnvgs: " + e.getMessage(), e);
		}
		if (pnvgs.length() > 0)
			pnvgs =pnvgs.substring(0, pnvgs.length() - 1);
		}
		log.log(Level.INFO, "getCSVPnvgs: returning (" + pnvgs + ")");
		return pnvgs;
		
	}
	
	
	
}
