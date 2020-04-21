package com.FEIS;
import java.sql.ResultSet;

public class GeoData {
	public GeoData (){}	
	
	 
	public ResultSet getPNVGCode(String point_id, double radius)
	{		
		  String SQLStr = "SELECT \"public\".\"PNVG\".\"PNVG_CODE\" " +
          " FROM \"public\".\"PNVG\"  LEFT JOIN \"public\".\"GOOGLE_POINTS\" " +
          " ON ST_DWithin(\"public\".\"PNVG\".\"the_geom\", \"public\".\"GOOGLE_POINTS\".\"the_geom\"," + radius + ")" +
          " WHERE \"public\".\"GOOGLE_POINTS\".\"gid\" = '" + point_id + "';";
          DbAccess DMan = new DbAccess(SQLStr);
          
	      return DMan.ExecuteQuery(); 
	}
	
	public ResultSet getVegSysCode(String pnvg_code)
	{
		 String SQLStr = "SELECT DISTINCT \"VEGSYS_CODE_2\" FROM \"VEGSYS_CROSSWALK\" WHERE \"VEGSYS_CODE_1\" IN (" + pnvg_code + ");";
		 DbAccess DMan = new DbAccess(SQLStr);
		 return DMan.ExecuteQuery(); 
	}
	
	/* New stuff for BPS search on maps */
	public ResultSet getBpsCode(String lat, String longi, double radius)
	{		
		return getBpsCode("bps_us", lat, longi, radius);
	}
	
	public ResultSet getBpsCodeAlaska(String lat, String longi, double radius)
	{		
		return getBpsCode("bps_ak", lat, longi, radius);
	}
	
	
	public ResultSet getBpsCode(String table, String lat, String longi, double radius) {
		  String SQLStr = "SELECT \"public\".\"" + table + "\".\"bps_model\" " +
		          " FROM \"public\".\"" + table + "\"   " +
		          "WHERE ST_DWithin(\"public\".\"" + table + "\".\"the_geom\", ST_SetSRID(ST_MakePoint(" + longi + ", " + lat + "), 4326) , " + radius + ")";
		          DbAccess DMan = new DbAccess(SQLStr);
		          
			      return DMan.ExecuteQuery();
	}
	
	public ResultSet getBpsCodeHawaii(String lat, String longi, double radius)
	{		
		return getBpsCode("bps_hi", lat, longi, radius);
 
	}
	
	public ResultSet getBpsSysCode(String bps_code)
	{
		 //String SQLStr = "SELECT DISTINCT \"BPS_ID\" FROM \"REVIEW_BPS_LINK\" WHERE \"BPS_ID\" IN (" + bps_code + ");";
		 String SQLStr = "SELECT DISTINCT \"BPS_ID\" FROM \"BPS\" WHERE \"BPS_CODE\" IN (" + bps_code + ");";
		 DbAccess DMan = new DbAccess(SQLStr);
		 return DMan.ExecuteQuery(); 
	}
	
	//special function to get fs_id for firestudy map searches using bps
	
	public ResultSet getBpsFSID(String bps_code_final)
	{
		 //String SQLStr = "SELECT DISTINCT \"BPS_ID\" FROM \"REVIEW_BPS_LINK\" WHERE \"BPS_ID\" IN (" + bps_code + ");";
		 String SQLStr = "SELECT DISTINCT \"FS_ID\" FROM \"FIRESTUDY_BPS_LINK\" WHERE \"BPS_ID\" IN (" + bps_code_final + ");";
		 DbAccess DMan = new DbAccess(SQLStr);
		 return DMan.ExecuteQuery(); 
	}
	
	public ResultSet getBpsFRID(String bps_code_final)
	{
		 //String SQLStr = "SELECT DISTINCT \"BPS_ID\" FROM \"REVIEW_BPS_LINK\" WHERE \"BPS_ID\" IN (" + bps_code + ");";
		 String SQLStr = "SELECT DISTINCT \"FIREREGIME_BPS_LINK\".\"FR_ID\" FROM \"FIREREGIME_BPS_LINK\" inner join \"FIREREGIME\" on (\"FIREREGIME\".\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\") " +
		 " WHERE \"FIREREGIME\".\"ENTRY_COMPLETE\" = true and \"BPS_ID\" IN (" + bps_code_final + ");";
		 DbAccess DMan = new DbAccess(SQLStr);
		 return DMan.ExecuteQuery(); 
	}
	
	/* End */
	
	//I added this to fix the map on the fire studies advanced search page.
	public ResultSet getVegSysCodeFS(String pnvg_code)
	{
		 String SQLStr = "SELECT DISTINCT \"VEGSYS_CODE_1\" FROM \"VEGSYS_CROSSWALK\" WHERE \"VEGSYS_CODE_1\" IN (" + pnvg_code + ");";
		 DbAccess DMan = new DbAccess(SQLStr);
		 return DMan.ExecuteQuery(); 
	}
	
	public ResultSet getStates()
	{
		 String SQLStr = "SELECT \"ABBR\", \"STATE_NAME\"  FROM \"STATES\" ORDER BY \"STATE_NAME\";";
	     DbAccess DMan = new DbAccess(SQLStr);
 		 return DMan.ExecuteQuery(); 	
	}
	
	
	
	
	
}
