/**
 * Class SpeciesReviewsQueryBuilder to build postgrest sql statement and query description 
 * for user selected query filters
 */

package com.FEIS;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReviewsQueryBuilder {
	
	private boolean byName = false;
	private String inputName;
	public boolean getByName(){return byName;}
	public void setByName(boolean newValue){byName = newValue;}	
	public String getInputName(){return inputName;}
	public void setInputName(String newValue){inputName = newValue;}	
	private boolean byFedStatus = false;
	private String fedStatus;
	private boolean byNewStates = true;
	private String newStates;
	
	private long fireRegimeId; 
	
	private String description = "";
	private String joinOf = "";
	private String condition = "";	
	private String errMsg = "";
	
	//------------lifeform selections -------------------
	private boolean byLifeForm;
	List<String> lifeformPlants;
	List<String> lifeformAnimals;
	private boolean lichenAll; 
	private boolean fungusAll; 
	private boolean plantsAll; 
	private boolean animalsAll; 
	//------------ end lifeform selections --------------
	
	//------------ geo selections --------------
	private String[] states;
	private String pnvgs; 
	private boolean byPnvg = false;    
	private String bps;
	
	private String plantCommunities;
	private String pnvgRegion = "";     
	private String vegTypeDesc; 
			
	private boolean byNativity = false; 
	private int nativity; 
	private boolean byInvasiveness = false; 
	private int invasiveness;		
		
	// geo area - criteria - state, admin, unit, plant community, map
	private boolean byGeoArea = false;   
	private int geoArea;		
	
	private boolean byState = false;
	private boolean byAdminUnit = false;	
	private String[] unitNames;	
	private String agency;      
	private String region;	    
	
	//map layers
	private boolean byMap = false;
	private boolean byMapAlaska = false;
	private boolean byMapHawaii = false;
	//
	private String inputLongitude;
	private String inputLatitude;
	private int geoRadius; // radius selected
	
	// note: distinct pnvgs selected through vegsys_crosswalk (geodata) prior to calling query builder
	private String selectOtherFields = "with a as(SELECT DISTINCT " +	
	  //new stuff
	  "\"SPECIES\".\"SPP_ID\", " +
	  "\"SPECIES\".\"FEIS_SPP\", " +
	  "\"FIRESTUDY\".\"HTML_PATH\" AS \"FIRESTUDY_HTML_PATH\" , " +
	  "\"FIRESTUDY\".\"TITLE\", " +
	  "\"FIRESTUDY_SPECIES_LINK\".\"FS_ID\", " +
	  //
	  "\"REVIEWS\".\"REVIEW_ID\", "  + 			 
	  "\"REVIEWS\".\"ACRONYM\", "  +  
	  "\"REVIEWS\".\"COMTITLE\", " + 
	  "\"REVIEWS\".\"SCITITLE\", " +
	  "\"REVIEWS\".\"HTML_PATH\", " +
	  "\"REVIEWS\".\"YEAR_WRITTEN\", " +
	  "\"REVIEWS\".\"PDF_PATH\",  " + 
	  "(Select count(distinct(\"FIREREGIME_SPECIES_LINK\".\"FR_ID\")) from  \"FIREREGIME_SPECIES_LINK\" inner join \"FIREREGIME\" on (\"FIREREGIME\".\"FR_ID\" = \"FIREREGIME_SPECIES_LINK\".\"FR_ID\")  where \"FIREREGIME\".\"ENTRY_COMPLETE\" = true AND \"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\" ) as \"FR_REFERENCED_COUNT\" ";
	
	private String joinLifeformLink = "\"REVIEW_LIFEFORM_LINK\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\"";
	private String joinStatesLink = "\"REVIEW_STATES_LINK\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\"";
	private String joinAdminLink = "\"REVIEW_ADMIN_LINK\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\"";
	
	//private String joinVegSysLink = "\"REVIEW_VEGSYS_LINK\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\"";
	private String joinVegSysLink = "\"REVIEW_BPS_LINK\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\"";
	/* Adding this on to search the map based on the new BPS codes */
	private String joinBpsSysLink = "\"REVIEW_BPS_LINK\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\"";
	/* End */
	
	private String joinSpeciesReviews = "\"SPECIES\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\""; // all queries
	

	private String orderBy = "ORDER BY \"REVIEWS\".\"ACRONYM\",\"SPECIES\".\"SCITITLE\"";

	private String fromTables = "";
	//private String fromReviewTables = "\"REVIEWS\",\"NAMES\", \"SPECIES\""; // all queries
	private String fromReviewTables = "\"NAMES\", \"FIRESTUDY\"" + " FULL JOIN " +  "\"FEIS\" . \"FIRESTUDY_SPECIES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"FS_ID\")" +
	" FULL JOIN " + "\"FEIS\".\"SPECIES\" ON (\"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")" +
	" FULL JOIN " + "\"FEIS\".\"REVIEWS\" ON (\"SPECIES\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\")  "; // all queries
	
	
	public ReviewsQueryBuilder (){
	//
	// constructor logic here
	//		
	}
	
	public String getErrMsg(){return errMsg;}
	public void setErrMsg(String newValue){errMsg = newValue;}
	
	//------------------- Life form selection -------------
	public String getDescription(){return description;}
	public void setDescription(String newValue){description = newValue;}
	
	
	public boolean getByLifeForm(){return byLifeForm;}
	public void setByLifeForm(boolean newValue){byLifeForm = newValue;}	
	
	public List<String> getLifeformPlants(){return lifeformPlants;}
	public void setLifeformPlants(List<String> newValue){lifeformPlants = newValue;}
	public List<String> getLifeformAnimals(){return lifeformAnimals;}
	public void setLifeformAnimals(List<String> newValue){lifeformAnimals = newValue;}
	public boolean getLichenAll(){return lichenAll;}
	public void setLichenAll(boolean newValue){lichenAll = newValue;}	
	
	public boolean getFungusAll(){return fungusAll;}
	public void setFungusAll(boolean newValue){fungusAll = newValue;}	
	
	public boolean getPlantsAll(){return plantsAll;}
	public void setPlantsAll(boolean newValue){plantsAll = newValue;}	
	
	public boolean getAnimalsAll(){return animalsAll;}
	public void setAnimalsAll(boolean newValue){animalsAll = newValue;}	
	
	//------------------- Nativity selection -------------
	public boolean getByNativity(){return byNativity;}
	public void setByNativity(boolean newValue){byNativity = newValue;}
	
	public int getNativity(){return nativity;}
	public void setNativity(int newValue){nativity = newValue;}
	
	//------------------- Invasiveness selection -------------
	public boolean getByInvasiveness(){return byInvasiveness;}
	public void setByInvasiveness(boolean newValue){byInvasiveness = newValue;}
	
	public int getInvasiveness(){return invasiveness;}
	public void setInvasiveness(int newValue){invasiveness = newValue;}	
	
	//new stuff
	public boolean getByFedStatus(){return byFedStatus;}
	public void setByFedStatus(boolean newValue){byFedStatus = newValue;}
	
	public String getFedStatus(){return fedStatus;}
	public void setFedStatus(String newValue){fedStatus = newValue;}	
	
	public boolean getByNewStates(){return byNewStates;}
	public void setByNewStates(boolean newValue){byNewStates = newValue;}
	
	public String getNewStates(){return newStates;}
	public void setNewStates(String newValue){newStates = newValue;}	
	//
	
	//------------------- Geographic Area selection -------------
	public boolean getByGeoArea(){return byGeoArea;}
	public void setByGeoArea(boolean newValue){byGeoArea = newValue;}
	public int getGeoArea(){return geoArea;}
	public void setGeoArea(int newValue){geoArea = newValue;}
	
	public boolean getByState(){return byState;}
	public void setByState(boolean newValue){byState = newValue;}	
	
	public String[] getStates(){return states;}
	public void setStates(String[] newValue){states = newValue;}
	
	public String getPnvgs(){return pnvgs;}
	public void setPnvgs(String newValue){pnvgs = newValue;}
	
	/* This will replace the pnvgs for the map function */
	public String getBps(){return bps;}
	public void setBps(String newValue){bps = newValue;}
	/* End */
	
	public String getPlantCommunities(){return plantCommunities;}
	public void setPlantCommunities(String newValue){plantCommunities = newValue;}
	
	//--- admin unit
	public boolean getByAdminUnit(){return byAdminUnit;}
	public void setByAdminUnit(boolean newValue){byAdminUnit = newValue;}	
	
	public String[] getUnitNames(){return unitNames;}
	public void setUnitNames(String[] newValue){unitNames = newValue;}
	public String getAgency(){return agency;}
	public void setAgency(String newValue){agency = newValue;}
	public String getRegion(){return region;}
	public void setRegion(String newValue){region = newValue;}
	
	
	//--- plant community
	public boolean getByPnvg(){return byPnvg;}
	public void setByPnvg(boolean newValue){byPnvg = newValue;}
	public String getPnvgRegion(){return pnvgRegion;}
	public void setPnvgRegion(String newValue){pnvgRegion = newValue;}
	public String getVegTypeDesc(){return vegTypeDesc;}
	public void setVegTypeDesc(String newValue) {vegTypeDesc = newValue;}
	
	//--- map 
	public boolean getByMap(){return byMap;}
	public void setByMap(boolean newValue){byMap = newValue;}
	
	//new map layers stuff
	public boolean getByMapAlaska(){return byMapAlaska;}
	public void setByMapAlaska(boolean newValue){byMapAlaska = newValue;}
	public boolean getByMapHawaii(){return byMapHawaii;}
	public void setByMapHawaii(boolean newValue){byMapHawaii = newValue;}
	//
	public String getInputLongitude(){return inputLongitude;}
	public void setInputLongitude(String newValue){inputLongitude = newValue;}
	
	public String getInputLatitude(){return inputLatitude;}
	public void setInputLatitude(String newValue){inputLatitude = newValue;}
	
	public int getGeoRadius(){return geoRadius;}
	public void setGeoRadius(int newValue){geoRadius = newValue;}
	
	
	public String buildQuery()
	{
		String queryString = "";
		boolean queryOK = true;
		
		queryOK = prepareQuery();	
		
		if (queryOK)
			//queryString = selectOtherFields + " FROM " + fromTables + " WHERE " + joinOf + condition +  " " + " AND \"NAMES\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\" AND \"NAME\" ~* '" + queryInputNameString + "'" + 
			queryString = selectOtherFields + " FROM " + fromTables + " WHERE " + joinOf + " " + " AND \"NAMES\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\" " + condition +

			
			" ORDER BY " + "\"REVIEWS\".\"ACRONYM\",\"REVIEWS\".\"SCITITLE\")"
			+ " select case " + "\"SPP_ID\" " + " when lag  (\"SPP_ID\") over () then null else \"SPP_ID\" end, " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"FEIS_SPP\" end as \"FEIS_SPP\", " +
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"TITLE\" end as \"TITLE\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", " +
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"FS_ID\" end as \"FS_ID\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"REVIEW_ID\" end AS \"REVIEW_ID\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", " +
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"COMTITLE\" end as \"COMTITLE\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"SCITITLE\" end as \"SCITITLE\", " +
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"FIRESTUDY_HTML_PATH\" end AS \"FIRESTUDY_HTML_PATH\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"PDF_PATH\" end as \"PDF_PATH\", " +
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" " +
			     " from (select row_number() over () rno, " + "\"SPP_ID\", \"FEIS_SPP\", \"TITLE\",\"FS_ID\", \"COMTITLE\", \"REVIEW_ID\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"SCITITLE\", \"PDF_PATH\", \"FIRESTUDY_HTML_PATH\", \"FR_REFERENCED_COUNT\" " +
			        " from a ) a order by rno, " +  "\"ACRONYM\" , " +  "\"SCITITLE\"" + ";" ;
			
		else
			queryString = "";
		
		System.out.println(queryString);	
		return queryString;
	}
	private boolean prepareQuery() {
		boolean queryOK = true;
	 
		String queryInputNameString = checkQueryString(inputName);
		 
		//Multiple Entries Array
		String[] inputNameArray = queryInputNameString.split("\\s*\\,\\s*");
		//
		
		addJoin(joinSpeciesReviews,"AND");
		addCondition("\"REVIEWS\".\"ENTRY_COMPLETE\" = true ", "AND");
		//if (byNativity || byInvasiveness || byFedStatus)
		if (byNativity || byInvasiveness)
		{
			
			if (nativity > 0)
			{				
				addDescription("Nativity = "+ getNativityDescription(),"AND"); 							
				addCondition("\"REVIEWS\".\"NATIVE_STATUS\"=" + nativity,"AND");							
			}			
		
			if (invasiveness > 0)
			{
				addDescription("Invasiveness = " + getInvasivenessDescription(),"AND");							
				addCondition("\"REVIEWS\".\"INVASIVE_STATUS\"=" + invasiveness,"AND");						
			}
		}
		if (byFedStatus)
		{
			if (fedStatus.length() > 0)
			{
				addDescription("Federal Status = " + getFedStatusDescription(), "AND");
				addCondition("\"REVIEWS\".\"FED_STATUS\"=" + fedStatus, "AND");
			}
		}
		//new stuff
		//
		if (byLifeForm)		
			buildLifeformGroup();
		
		if (byState)		
			buildStateGroup();
		
		//if (byNewStates)
			//buildStateGroup();
		
		if (byPnvg)		
			buildPnvgGroup();
		
		if (byMap)
		{
		     queryOK = buildMapGroup();
		}
		
		//new map stuff
		if (byMapAlaska)
		{
			queryOK = buildMapAlaskaGroup();
		}
		if (byMapHawaii)
		{
			queryOK = buildMapHawaiiGroup();
		}
		//end
		
		//new stuff
		if (!isBlank(inputName))
		{
			addDescription("Name = "+ queryInputNameString, "AND");
			
			//multiple entries in simple search
			addCondition("(","AND");
			for(int i=0;i<inputNameArray.length;i++)
			{
			    System.out.println(inputNameArray[i]);
				addCondition("OR","\"NAMES\".\"NAME\" ~* '" + inputNameArray[i] + "'");
			}					
			condition = condition.substring(0,condition.lastIndexOf("OR"));
			System.out.println(condition);
			
			addCondition(")","");
			//End Multiple Entriess
		}
		//
		
		if (byAdminUnit)
			 queryOK = buildAdminUnitGroup();
		
		addTable(fromReviewTables);
		return queryOK;
	}
	
	String buildMagicString(String fsId) 
	{		
		  String description = fsId;
		  //Create an array of Strings made up of acronyms
		  String[] set = description.split(",");
		  //Hash Set removes duplicate words from the array of String of acronyms
		  Set<String> uniqueWords = new HashSet<String>(Arrays.asList(set));
		  System.out.println(uniqueWords);
		  //Makes the non-duplicated Hash Set into an array of String of acronyms that can be added to the description
		  String[] newSet = uniqueWords.toArray(new String[uniqueWords.size()]);
		  
		  addDescription("Fire Study ID = " , null);
		  for(int i=0; i<newSet.length; i++)
		  {
			  //turns the "null" part of the array of strings of acronyms, into nothing.
			  if (newSet[i].equals("'null'"))
			  {
			      newSet[i] = "";
			      //break;
			  }
			  addDescription(newSet[i], "\n");
	          System.out.println(newSet[i]);
		  }
		  
		  String newQueryString = "SELECT DISTINCT" 
		  + "\"SPECIES\".\"FEIS_SPP\", "
		  + "\"REVIEWS\".\"REVIEW_ID\", "   			 
		  + "\"REVIEWS\".\"ACRONYM\", "  
		  + "\"REVIEWS\".\"COMTITLE\", "
		  + "\"REVIEWS\".\"SCITITLE\", "
		  + "\"REVIEWS\".\"YEAR_WRITTEN\", "
		  + "\"REVIEWS\".\"PDF_PATH\", "
		  
		  + "\"FIRESTUDY\".\"HTML_PATH\" AS \"FIRESTUDY_HTML_PATH\" , "
		  + "\"REVIEWS\".\"HTML_PATH\" AS \"REVIEWS_HTML_PATH\", "
		  + "\"FIRESTUDY_STATES_LINK\".\"ABBR\", " + "\"FIRESTUDY\".\"FS_AUTHORS\", "
		  + "\"FIRESTUDY_SPECIES_LINK\".\"FS_ID\" , " + "\"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\", "
		  + "\"FIRESTUDY\".\"FS_ID\", " + "\"FIRESTUDY\".\"TITLE\", " + "\"FIRESTUDY\".\"HTML_PATH\", "
	      + "\"SPECIES\".\"SPP_ID\", " + "\"REVIEWS\".\"REVIEW_ID\", "
	      + "\"REVIEWS\".\"ACRONYM\", " + "\"SPECIES\".\"REVIEW_ID\",  "
	      + "(Select count(distinct(\"FIREREGIME_SPECIES_LINK\".\"FR_ID\")) from  \"FIREREGIME_SPECIES_LINK\" inner join \"FIREREGIME\" on (\"FIREREGIME\".\"FR_ID\" = \"FIREREGIME_SPECIES_LINK\".\"FR_ID\")  where \"FIREREGIME\".\"ENTRY_COMPLETE\" = true AND \"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" ) as \"FR_REFERENCED_COUNT\" "
		  + " FROM " + "\"FIRESTUDY_SPECIES_LINK\", \"FIRESTUDY\", \"SPECIES\", \"REVIEWS\", \"FIRESTUDY_STATES_LINK\", \"LU_FIRESTUDYTYPES\" " 
		  + " WHERE " +  "\"FIRESTUDY_SPECIES_LINK\".\"FS_ID\" = \"FIRESTUDY\".\"FS_ID\" "
		  + " AND " + "\"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\" "
		  + " AND " + "\"REVIEWS\".\"REVIEW_ID\" = \"SPECIES\".\"REVIEW_ID\" " 
		  + " AND " + "\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_STATES_LINK\".\"FS_ID\" "
		  + " AND " + "\"FIRESTUDY\".\"FS_ID\" = '" + fsId + "'"
		  + " AND \"REVIEWS\".\"ENTRY_COMPLETE\" = true "
		  + " ORDER BY " + "\"REVIEWS\".\"ACRONYM\" , " + "\"REVIEWS\" . \"SCITITLE\""
		  + ";" ;
		  
	      System.out.println(newQueryString);
	      		
		return newQueryString;
	}
	
	
	private boolean buildMapGroup()
	{
		System.out.println("Make Map Group");
		String description="";
		boolean queryUpdated = true;
		if("".equals(inputLongitude) || inputLongitude == null)
		{
			errMsg = "Input Longitude not Specified";
			return false;
		}
		else if ("".equals(inputLatitude) || inputLatitude == null)
		{
			errMsg = "Input Latitude not Specified";
			return false;
		}
		
		double radius = 0.001;		
		if (this.geoRadius == 1)
			description="1/10 mile Geographic search radius centered at ";
		else
			description="50 mile Geographic search radius centered at ";
				
		inputLatitude = inputLatitude.trim();
		inputLongitude = inputLongitude.trim();
        if (!inputLongitude.startsWith("-"))
        {
        	inputLongitude = "-" + inputLongitude;
        }
        description = description + inputLongitude + "," + inputLatitude;

		addDescription(description,"AND");
		/* Removing pnvg search from the map function, will be using BPS instead */
		//String pnvgSelections = "";			
		//String[] veg_codes = pnvgs.split(",");	
		/* End */
		
		String bpsSelections = "";			
		String[] bps_codes = bps.split(",");	
		
		// build code query part
	    /* 
	    for (int i = 0; i < veg_codes.length; i++)	    
	    	pnvgSelections = pnvgSelections.trim() + "'" + veg_codes[i].trim() + "',"; 	
	    
	    if (("".equals(pnvgSelections) || pnvgSelections == null)) 
	    */
		
	    for (int i = 0; i < bps_codes.length; i++)	    
		    bpsSelections = bpsSelections.trim() + "'" + bps_codes[i].trim() + "',"; 	    
		if (("".equals(bpsSelections) || bpsSelections == null)) 
		{
	    	queryUpdated = false;	    
	    	errMsg = "No Results: There are no data for plant communities corresponding to the selected location.";	   	
	    	
		}
	    else
	    {
	    	/* 
	    	if ((pnvgSelections.compareTo("'',") != 0))
	    	{	    	
		    pnvgSelections = pnvgSelections.substring(0, pnvgSelections.length() - 1); 
		    */
	    	
	    	if ((bpsSelections.compareTo("'',") != 0))
	    	{	    	
	    		bpsSelections = bpsSelections.substring(0, bpsSelections.length() - 1); 	
	    	
			/* Removing this to use the new BPS codes for the map */
			//addTable("\"REVIEW_VEGSYS_LINK\"");			
			//addJoin(joinVegSysLink,"AND");			
			//addCondition( "\"REVIEW_VEGSYS_LINK\".\"VEGSYS_CODE\" IN (" + pnvgSelections + ")","AND" );
	        /* End */
			addTable("\"REVIEW_BPS_LINK\"");			
			addJoin(joinBpsSysLink,"AND");			
			addCondition( "\"REVIEW_BPS_LINK\".\"BPS_ID\" IN (" + bpsSelections + ")","AND" );
	    	}
	    	else
	    	{
	    		queryUpdated = false;		    	
		    	errMsg = "No Results: There are no data for plant communities corresponding to the selected location.";
	    	}
	    }
		return queryUpdated;
	}
	
	//new map queries
	private boolean buildMapAlaskaGroup()
	{
		System.out.println("Make Alaska Map Group");
		String description="";
		boolean queryUpdated = true;
		if("".equals(inputLongitude) || inputLongitude == null)
		{
			errMsg = "Input Longitude not Specified";
			return false;
		}
		else if ("".equals(inputLatitude) || inputLatitude == null)
		{
			errMsg = "Input Latitude not Specified";
			return false;
		}
		
		double radius = 0.001;		
		if (this.geoRadius == 1)
			description="1/10 mile Geographic search radius centered at ";
		else
			description="50 mile Geographic search radius centered at ";
				
		inputLatitude = inputLatitude.trim();
		inputLongitude = inputLongitude.trim();
        if (!inputLongitude.startsWith("-"))
        {
        	inputLongitude = "-" + inputLongitude;
        }
        description = description + inputLongitude + "," + inputLatitude;

		addDescription(description,"AND");
		
		String bpsSelections = "";			
		String[] bps_codes = bps.split(",");	
		
	    for (int i = 0; i < bps_codes.length; i++)	    
		    bpsSelections = bpsSelections.trim() + "'" + bps_codes[i].trim() + "',"; 	    
		if (("".equals(bpsSelections) || bpsSelections == null)) 
		{
	    	queryUpdated = false;	    
	    	errMsg = "No Results: There are no data for plant communities corresponding to the selected location.";	   	
	    	
		}
	    else
	    {	
	    	if ((bpsSelections.compareTo("'',") != 0))
	    	{	    	
	    		bpsSelections = bpsSelections.substring(0, bpsSelections.length() - 1); 	
	    		addTable("\"REVIEW_BPS_LINK\"");			
	    		addJoin(joinBpsSysLink,"AND");			
	    		addCondition( "\"REVIEW_BPS_LINK\".\"BPS_ID\" IN (" + bpsSelections + ")","AND" );
	    	}
	    	else
	    	{
	    		queryUpdated = false;		    	
		    	errMsg = "No Results: There are no data for plant communities corresponding to the selected location.";
	    	}
	    }
		return queryUpdated;
	}
	//end
	
	//new map queries
	private boolean buildMapHawaiiGroup()
	{
		System.out.println("Make Hawaii Map Group");
		String description="";
		boolean queryUpdated = true;
		if("".equals(inputLongitude) || inputLongitude == null)
		{
			errMsg = "Input Longitude not Specified";
			return false;
		}
		else if ("".equals(inputLatitude) || inputLatitude == null)
		{
			errMsg = "Input Latitude not Specified";
			return false;
		}
		
		double radius = 0.001;		
		if (this.geoRadius == 1)
			description="1/10 mile Geographic search radius centered at ";
		else
			description="50 mile Geographic search radius centered at ";
				
		inputLatitude = inputLatitude.trim();
		inputLongitude = inputLongitude.trim();
        if (!inputLongitude.startsWith("-"))
        {
        	inputLongitude = "-" + inputLongitude;
        }
        description = description + inputLongitude + "," + inputLatitude;

		addDescription(description,"AND");
		
		String bpsSelections = "";			
		String[] bps_codes = bps.split(",");	
		
	    for (int i = 0; i < bps_codes.length; i++)	    
		    bpsSelections = bpsSelections.trim() + "'" + bps_codes[i].trim() + "',"; 	    
		if (("".equals(bpsSelections) || bpsSelections == null)) 
		{
	    	queryUpdated = false;	    
	    	errMsg = "No Results: There are no data for plant communities corresponding to the selected location.";	   	
	    	
		}
	    else
	    {	
	    	if ((bpsSelections.compareTo("'',") != 0))
	    	{	    	
	    		bpsSelections = bpsSelections.substring(0, bpsSelections.length() - 1); 	
	    		addTable("\"REVIEW_BPS_LINK\"");			
	    		addJoin(joinBpsSysLink,"AND");			
	    		addCondition( "\"REVIEW_BPS_LINK\".\"BPS_ID\" IN (" + bpsSelections + ")","AND" );
	    	}
	    	else
	    	{
	    		queryUpdated = false;		    	
		    	errMsg = "No Results: There are no data for plant communities corresponding to the selected location.";
	    	}
	    }
		return queryUpdated;
	}
	//end
	
	
	private boolean buildAdminUnitGroup()
	{

		//may be multiple selections
		String unitSelections = "";		
		String descriptionUnits = "";
		
		// add query selections
		AdminUnitSearch auUtil = new AdminUnitSearch();
		int count = 0;
		boolean retValue = true;
		if (unitNames != null)
		{
			for (int i=0; i < unitNames.length; i++)
			{
				if (unitNames[i].compareTo("0") != 0)
				{
					count++;
					unitSelections = unitSelections.trim()  + unitNames[i].trim() + ",";	
					auUtil.getUnitName(unitNames[i].trim());	
					descriptionUnits = addOrDescription(descriptionUnits, "Unit Name = " + auUtil.getNames1());						
					//	addDescription("Unit Name = " + auUtil.getNames1(), "OR");	
				}
			}
			if (count > 0) 
			{
				descriptionUnits = descriptionUnits + ")";
				addDescription(descriptionUnits,"AND");
			}
		}
		
		 // retrieve all unit names for search
		if ("".equals(unitSelections) || unitSelections == null)
		{			
			unitSelections = auUtil.getCSVUnitNames(this.agency, this.region);	
			addDescription("Unit Names = All","AND");
		}
		
		if (!("".equals(unitSelections) || unitSelections == null)) 
		{
			unitSelections = unitSelections.substring(0, unitSelections.length() - 1); 
			addTable("\"REVIEW_ADMIN_LINK\"");			
			addJoin(joinAdminLink,"AND");			
			addCondition( "\"REVIEW_ADMIN_LINK\".\"ADMIN_ID\" IN (" + unitSelections + ")","AND" );
		}
		
		return retValue;
	}
	private boolean buildStateGroup()
	{
		
		if ((states == null) || (states.length == 0)) return false;
		String descriptionStates="";
		String prefix="State = ";
		String stateSelections = "";	
		for (int i=0; i< states.length; i++)
		{			
			descriptionStates = addOrDescription(descriptionStates,prefix+states[i].trim());
			stateSelections = stateSelections.trim()  + "'" + states[i].trim() + "',";			
		}
		if (descriptionStates.length() <= 0) return false;
		descriptionStates = descriptionStates + ")";
		addDescription(descriptionStates,"AND");
			
		if (!("".equals(stateSelections) || stateSelections == null)) 
		{
			stateSelections = stateSelections.substring(0, stateSelections.length() - 1); 	
			addTable("\"REVIEW_STATES_LINK\"");			
			addJoin(joinStatesLink,"AND");			
			addCondition("\"REVIEW_STATES_LINK\".\"ABBR\" IN (" + stateSelections + ")","AND" );
		}
		return true;
		
	}
	private boolean buildPnvgGroup()
	{	

		String descriptionPnvgs="";
		String prefix="Plant Community = ";
		String pnvgSelections = "";	
		
		//String[] veg_codes = pnvgs.split(",");	
		String[] veg_codes = bps.split(",");
		String[] desc_codes = plantCommunities.split(",");
		
		// build code query part
	    for (int i = 0; i < veg_codes.length; i++)		    
	    	pnvgSelections = pnvgSelections.trim() + "'" + veg_codes[i].trim() + "',";	    
		System.out.println("This is the PNVG_SELECTIONS: " + pnvgSelections);
	    
	    // build descriptions (may be different length)
	    for (int i = 0; i < desc_codes.length; i++)	    
	    	descriptionPnvgs = addOrDescription(descriptionPnvgs,prefix+desc_codes[i].trim());	
		System.out.println("This is the DESCRIPTION_PNVGS: " + descriptionPnvgs);
	    
	    descriptionPnvgs = descriptionPnvgs + ")";
		addDescription(descriptionPnvgs,"AND");
		
	    if (pnvgSelections.length() <= 0) return false; 
		
		if (!("".equals(pnvgSelections) || pnvgSelections == null)) 
		{
			pnvgSelections = pnvgSelections.substring(0, pnvgSelections.length() - 1); 	
			addTable("\"REVIEW_BPS_LINK\"");		
			addJoin(joinVegSysLink,"AND");			
			addCondition( "\"REVIEW_BPS_LINK\".\"BPS_ID\" IN (" + pnvgSelections + ")","AND" );
		}
		return true;		
		//return queryUpdated;
	}
	
	private void buildLifeformGroup()
	{
		List<String> queryIds = new ArrayList<String>();
		String descriptionLifeforms="";
		String prefix="Life form = ";
		// fungus
		if (fungusAll)
		{			
			descriptionLifeforms = addOrDescription(descriptionLifeforms,prefix+"Fungus = All");
			queryIds.add("15");			
		}
		// lichen
		if (lichenAll)
		{
			descriptionLifeforms = addOrDescription(descriptionLifeforms,prefix+"Lichen = All");			
			queryIds.add("14");			
		}
		// plants 
		if (plantsAll) //12,13,11,6,5,2,1,10
		{
			descriptionLifeforms = addOrDescription(descriptionLifeforms,prefix+"Plants = All");			
			queryIds.add("12"); queryIds.add("13");queryIds.add("11"); queryIds.add("6");
			queryIds.add("5"); queryIds.add("2"); queryIds.add("1"); queryIds.add("10");
		}	
		Lifeform luLifeform = new Lifeform();
		for (int i=0; i < lifeformPlants.size(); i++)
		{
			if (!plantsAll)
			{
				luLifeform.select(lifeformPlants.get(i));
				descriptionLifeforms = addOrDescription(descriptionLifeforms,prefix+luLifeform.getLifeform());	
			}
			queryIds.add(lifeformPlants.get(i));
		}
		// animals
		
		if (animalsAll)
		{
			descriptionLifeforms = addOrDescription(descriptionLifeforms,prefix+"Animals = All");
			//8,3,4,7,9
			queryIds.add("8");queryIds.add("3"); queryIds.add("4"); queryIds.add("7"); queryIds.add("9");
		}
		
		for (int i=0; i < lifeformAnimals.size(); i++)
		{
			if (!animalsAll)
			{
				luLifeform.select(lifeformAnimals.get(i));
				descriptionLifeforms = addOrDescription(descriptionLifeforms,prefix+luLifeform.getLifeform());
			}
				
			queryIds.add(lifeformAnimals.get(i));
		}
		if (!("".equals(descriptionLifeforms) || descriptionLifeforms == null)) 
		{	descriptionLifeforms = descriptionLifeforms + ")";
			addDescription(descriptionLifeforms,"AND");
		}	
		
		String lifeformSelections = "";		
		for (int i=0; i < queryIds.size(); i++)
		{
			lifeformSelections = lifeformSelections.trim()  + queryIds.get(i) + ",";	
		}
		
		
		
		if (!("".equals(lifeformSelections) || lifeformSelections == null)) 
		{
			lifeformSelections = lifeformSelections.substring(0, lifeformSelections.length() - 1); 	
			addTable("\"REVIEW_LIFEFORM_LINK\"");			
			addJoin(joinLifeformLink,"AND");			
			addCondition("\"REVIEW_LIFEFORM_LINK\".\"LIFEFORM_ID\" IN (" + lifeformSelections + ")","AND" );
		}
		
	}
	
	private String getNativityDescription()
	{
		String nativityDesc = "";
		if (nativity == 1)
			nativityDesc = "Native";
		else if (nativity == 2)
		    nativityDesc = "Nonnative";
		    else if (nativity == 3)
		    	nativityDesc = "Both native and nonnative in parts of its US range";
		    
		return nativityDesc;
	}
	private String getInvasivenessDescription()
	{
		String invasiveDesc = "";
		if (invasiveness == 1)
			invasiveDesc = "Invasive";
		else if (invasiveness == 2)
			invasiveDesc = "Noninvasive";
		return invasiveDesc;
	}
	//new stuff
	private String getFedStatusDescription()
	{
		String fedStatusDesc = "";
		if(fedStatus.length() == 4)
			fedStatusDesc = "True";
	    //return input == null ? true : input.trim().length()==0; 

		else if (fedStatus.length() == 5)
			fedStatusDesc = "False";
		System.out.println("here" + fedStatusDesc);	
		return fedStatusDesc;
	}
	//
	
	//-------------------query builder helper methods -------------
	private void addDescription(String strText,String strOperator)
	{
	   if ("".equals(strText) || strText == null) return;
	   if (description.length() > 0)
		   description = description + " " + strOperator + " " + strText + " ";
	   else
		   description = strText + " ";	
	}
	private String addOrDescription(String strCurrent, String strNew)
	{
		if ("".equals(strNew) || strNew == null) return strCurrent;
		if ("".equals(strCurrent) || strCurrent == null) 
		{
			strCurrent = "(" + strNew + " ";
		}
		else
			strCurrent = strCurrent + " OR " + strNew;
		return strCurrent;
		 
	}
	private void addTable(String strText)
	{
		
		if ("".equals(strText) || strText == null) return;
		if (fromTables.length() > 0)
			fromTables = fromTables + "," + strText + " ";
		else
			fromTables = strText;
	}
	private void addJoin(String strText,String strOperator)
	{
	   if ("".equals(strText) || strText == null) return;
	   if (joinOf.length() > 0)
		   joinOf = joinOf + " " + strOperator + " " + strText + " ";
	   else
		   joinOf = strText + " ";	
	}
	private void addCondition(String strText,String strOperator)
	{
	   if ("".equals(strText) || strText == null) return;
	   if (joinOf.length() > 0) // conditions follow joins need the operator first
		   condition = condition + " " + strOperator + " " + strText + " ";
	   else
		   condition = strText + " ";	
	}
	protected String checkQueryString(String queryString)
	{
		String ret = "";
		if(queryString != null) {
		// add escape for apostrophe's
			final StringBuilder result = new StringBuilder();
			final StringCharacterIterator iterator = new StringCharacterIterator(queryString);
			char character = iterator.current();
			while (character != CharacterIterator.DONE)
			{
				if (character == '\'')
				{
					result.append("'");
					result.append(character);
				}
				else result.append(character);
				character = iterator.next();
			}
			ret = result.toString();
		}
		return ret; 
	}
	private boolean isBlank(String input)
    {
	    return input == null ? true : input.trim().length()==0; 
    }
	public long getFireRegimeId() {
		return fireRegimeId;
	}
	public void setFireRegimeId(long fireRegimeId) {
		this.fireRegimeId = fireRegimeId;
	}
	public String buildFireRegimeQuery() {
		
		String queryString = "";
		boolean queryOk = prepareQuery();
		if(queryOk) {
			
			 queryString = "with a as(select iq.*, CASE WHEN iq.fs_count > 0 then 'X' END as \"FIRESTUDY_HTML_PATH\" " +
			 		" from (" + 
					"	select distinct r.\"REVIEW_ID\", s.\"SPP_ID\", s.\"FEIS_SPP\", 'na' as \"TITLE\", 0 as \"FS_ID\", r.\"ACRONYM\", r.\"COMTITLE\", r.\"SCITITLE\", r.\"HTML_PATH\", r.\"YEAR_WRITTEN\", r.\"PDF_PATH\" , " +
					"   (select count(distinct(\"FIREREGIME_SPECIES_LINK\".\"FR_ID\")) from  \"FIREREGIME_SPECIES_LINK\" inner join \"FIREREGIME\" on (\"FIREREGIME\".\"FR_ID\" = \"FIREREGIME_SPECIES_LINK\".\"FR_ID\")  where \"FIREREGIME\".\"ENTRY_COMPLETE\" = true AND \"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = s.\"SPP_ID\" ) as \"FR_REFERENCED_COUNT\", " +
					"	(select" + 
					"		count(distinct(\"FIRESTUDY\".\"FS_ID\")) " + 
					"		from \"FIREREGIME\" inner join \"FIREREGIME_SPECIES_LINK\" on (\"FIREREGIME\".\"FR_ID\" = \"FIREREGIME_SPECIES_LINK\".\"FR_ID\") " + 
					"		left outer join \"FIRESTUDY_SPECIES_LINK\" on (\"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" = \"FIREREGIME_SPECIES_LINK\".\"SPP_ID\") " + 
					"		left outer join \"FIRESTUDY\" on (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"FS_ID\") " + 
					"		where \"FIRESTUDY\".\"ENTRY_COMPLETE\" = true" + 
					"		and \"FIREREGIME\".\"ENTRY_COMPLETE\" = true" + 
					"		and \"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" = s.\"SPP_ID\"" + 
					"		and \"FIREREGIME\".\"FR_ID\" = fr.\"FR_ID\"  ) as fs_count, " + 
					"fr.\"FR_ID\"" + 
					"	from \"REVIEWS\" r" + 
					"		left outer join \"SPECIES\" s on (s.\"REVIEW_ID\" = r.\"REVIEW_ID\")" + 
					"		left outer join \"FIREREGIME_SPECIES_LINK\" frsl on (frsl.\"SPP_ID\" = s.\"SPP_ID\")" + 
					"		inner join \"FIREREGIME\" fr on (frsl.\"FR_ID\" = fr.\"FR_ID\")" + 
					"	where r.\"ENTRY_COMPLETE\" = true" + 
					"	and fr.\"ENTRY_COMPLETE\" = true " + 
					"	and fr.\"FR_ID\" = " + fireRegimeId +  
					" order by \"ACRONYM\", \"SCITITLE\" " + 
					") iq )"  
			+ " select case " + "\"SPP_ID\" " + " when lag  (\"SPP_ID\") over () then null else \"SPP_ID\" end, " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"FEIS_SPP\" end as \"FEIS_SPP\", " +
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"TITLE\" end as \"TITLE\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", " +
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"FS_ID\" end as \"FS_ID\", " + 
			      // " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"REVIEW_ID\" end AS \"REVIEW_ID\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", " +
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"COMTITLE\" end as \"COMTITLE\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"SCITITLE\" end as \"SCITITLE\", " +
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"FIRESTUDY_HTML_PATH\" end AS \"FIRESTUDY_HTML_PATH\", " + 
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"PDF_PATH\" end as \"PDF_PATH\", " +
			       " case \"REVIEW_ID\" when lag(\"REVIEW_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" " +
			     " from (select row_number() over () rno, " + "\"SPP_ID\", \"FEIS_SPP\", \"TITLE\",\"FS_ID\", \"COMTITLE\", \"REVIEW_ID\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"SCITITLE\", \"PDF_PATH\", \"FIRESTUDY_HTML_PATH\", \"FR_REFERENCED_COUNT\" " +
			        " from a ) a order by rno, " +  "\"ACRONYM\" , " +  "\"SCITITLE\"" + ";" ;
		}
		addDescription("Fire Regime Id = " + fireRegimeId, "AND");
		return queryString;
	}
	
}
