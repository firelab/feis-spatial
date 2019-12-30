package com.FEIS;

import java.sql.ResultSet;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FireRegimeQueryBuilder {
	private String description = "";
	private String desc = "";
	private String joinOf = "";
	private String condition = "";
	private String errMsg = "";
	private String acronym = "";
	private String frID = "";
	private String fsId = "";
	boolean activate = false;
	// ------------ geo selections --------------
	private String[] states;
	private String pnvgs;
	private String plantCommunities;

	// geo area - criteria - state, admin, unit, plant community, map
	private boolean byGeoArea = false;
	private int geoArea;

	private boolean byName = false;
	private String fireRegimeName;

	private String inputAuthor;
	private String inputTitle;

	private boolean byState = false;
	private boolean byAdminUnit = false;
	private String[] unitNames;
	private String agency;
	private String region;

	private boolean byPnvg = false;
	private boolean byMap = false;
	// new
	private boolean byMapAlaska = false;
	private boolean byMapHawaii = false;
	// end

	// new
	private String bps;
	// end

	private String inputLongitude;
	private String inputLatitude;
	private int geoRadius; // radius selected

	private static final Logger log = Logger
			.getLogger(FireRegimeQueryBuilder.class.getName());

	// note: distinct pnvgs selected through vegsys_crosswalk (geodata) prior to
	// calling query builder
	private String selectFireRegimeFields = " select distinct fr.\"FR_ID\", fr.\"TITLE\", \"MONTH_WRITTEN\", \"YEAR_WRITTEN\", \"QUALITY\", \"NUM_FEIS_TAXA\", \"NUM_NON_FEIS_TAXA\", "
			+ "\"NUM_PAGES\", \"NUM_CITATIONS\", \"HTML_PATH\", fr.\"CREATED\", fr.\"UPDATED\", fr.\"NOTES\", \"ENTRY_COMPLETE\", "
			+ "\"FULL_TITLE\", \"BPS_MININTERVAL\", \"BPS_MAXINTERVAL\", \"MIN_REPLACE\", \"MAX_REPLACE\", \"MIN_MIXED\", \"MAX_MIXED\", "
			+ "\"MIN_LOW\", \"MAX_LOW\", \"NUM_FRG-I\", \"NUM_FRG-II\", \"NUM_FRG-III\", \"NUM_FRG-IV\", \"NUM_FRG-V\", "
			+ "\"NUM_FRG-NA\", \"HTML_PATH_APPENDIXA\", \"FEIS_MININTERVAL\", \"FEIS_MAXINTERVAL\" , lub.\"BPSREGION\", b.\"BIOPHYSICAL_SETTING_NAME\", "
			+ "(select count(distinct(\"FIRESTUDY\".\"FS_ID\")) from \"FIRESTUDY_SPECIES_LINK\", \"FIREREGIME_SPECIES_LINK\", "
			+ " \"FIRESTUDY\" where \"FIRESTUDY\".\"ENTRY_COMPLETE\" = true and  \"FIRESTUDY_SPECIES_LINK\".\"FS_ID\" = \"FIRESTUDY\".\"FS_ID\" and "
			+ " \"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" = \"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" and  \"FIREREGIME_SPECIES_LINK\".\"FR_ID\" = fr.\"FR_ID\") as fire_study_count, "
			+ " (select count(distinct(\"REVIEWS\".\"REVIEW_ID\")) from \"FIREREGIME_SPECIES_LINK\", \"SPECIES\", \"REVIEWS\" "
			+ "					  where \"REVIEWS\".\"ENTRY_COMPLETE\" = true and  \"REVIEWS\".\"REVIEW_ID\" = \"SPECIES\".\"REVIEW_ID\" "
			+ "					  and  \"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\" "
			+ "					  and  \"FIREREGIME_SPECIES_LINK\".\"FR_ID\" = fr.\"FR_ID\") as review_count   ";
	private String fromTables = "";

	private String joinStatesLink = "frsl.\"FR_ID\" = fr.\"FR_ID\"";

	private String joinAdminLink = "\"FIREREGIME_ADMIN_LINK\".\"FR_ID\" = fr.\"FR_ID\"";

	private String joinVegSysLink = "l.\"FR_ID\" = fr.\"FR_ID\"";
	// queries
	private String joinFireRegimeSpeciesLink = "\"FIREREGIME_SPECIES_LINK\".\"FR_ID\" = fr.\"FR_ID\"";
	private String joinFireRegimeSpecies = "\"SPECIES\".\"SPP_ID\" = \"FIREREGIME_SPECIES_LINK\".\"SPP_ID\"";
	private String joinFireRegimeNames = "n.\"SPP_ID\" = \"FIREREGIME_SPECIES_LINK\".\"SPP_ID\"";
	private String orderBy = "ORDER BY lub.\"BPSREGION\" , fr. \"TITLE\"";

	private String joins = " LEFT OUTER JOIN "
			+ " \"FIREREGIME_STATES_LINK\" frsl ON (fr.\"FR_ID\" = frsl.\"FR_ID\")"
			+ " left outer join \"FIREREGIME_BPS_LINK\" l on (l.\"FR_ID\" = fr.\"FR_ID\") "
			+ " left outer join \"BPS\" b on (b.\"BPS_ID\" = l.\"BPS_ID\") "
			+ " left outer join \"LU_BPSREGIONS\" lub on (lub.\"BPSREGION_ID\" = b.\"BPSREGION_ID\") ";

	private String joinsTwo = " LEFT OUTER JOIN "
			+ " \"FIREREGIME_STATES_LINK\" frsl ON (fr.\"FR_ID\" = frsl.\"FR_ID\")"
			+ " INNER JOIN "
			+ " \"FIREREGIME_SPECIES_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_SPECIES_LINK\".\"FR_ID\")"
			+ " INNER JOIN "
			+ "\"SPECIES\" ON (\"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")";

	private String joinsThree = " LEFT OUTER JOIN "
			+ " \"FIREREGIME_STATES_LINK\" frsl ON (fr.\"FR_ID\" = frsl.\"FR_ID\")"
			+ " INNER JOIN "
			+ " \"FIREREGIME_SPECIES_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_SPECIES_LINK\".\"FR_ID\")"
			+ " INNER JOIN "
			+ "\"SPECIES\" ON (\"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")"
			+ " INNER JOIN "
			+ "\"FIREREGIME_ADMIN_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_ADMIN_LINK\".\"FR_ID\")";

	private String joinsFive = " LEFT OUTER JOIN "
			+ "\"FIREREGIME_BPS_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\")";

	private String fromFireRegimeTables = " \"FIREREGIME\" fr ";

	private String fromFireRegimeTablesTwo = "\"NAMES\" n,  \"FIREREGIME\" fr ";

	private String fromFireRegimeTablesThree = " \"NAMES\" n, \"FIREREGIME\" fr";
	private int speciesId;

	private static Logger logger = Logger
			.getLogger(FireRegimeQueryBuilder.class.getName());
	private String speciesName;

	public FireRegimeQueryBuilder() {
		//
		// constructor logic here
		//
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String newValue) {
		acronym = newValue;
	}

	public String getFRID() {
		return frID;
	}

	public void setFRID(String newValue) {
		frID = newValue;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String newValue) {
		errMsg = newValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String newValue) {
		description = newValue;
	}

	public boolean getByName() {
		return byName;
	}

	public void setByName(boolean newValue) {
		byName = newValue;
	}

	public String getFireRegimeName() {
		return fireRegimeName;
	}

	public void setFireRegimeName(String newValue) {
		fireRegimeName = newValue;
	}

	// New Stuff
	public String getInputAuthor() {
		return inputAuthor;
	}

	public void setInputAuthor(String newValue) {
		inputAuthor = newValue;
	}

	public String getInputTitle() {
		return inputTitle;
	}

	public void setInputTitle(String newValue) {
		inputTitle = newValue;
	}

	// //////////////////////////////////////////////////////////////////

	// ------------------- Geographic Area selection -------------

	/* This will replace the pnvgs for the map function */
	public String getBps() {
		return bps;
	}

	public void setBps(String newValue) {
		bps = newValue;
	}

	/* End */

	public boolean getByGeoArea() {
		return byGeoArea;
	}

	public void setByGeoArea(boolean newValue) {
		byGeoArea = newValue;
	}

	public int getGeoArea() {
		return geoArea;
	}

	public void setGeoArea(int newValue) {
		geoArea = newValue;
	}

	public boolean getByState() {
		return byState;
	}

	public void setByState(boolean newValue) {
		byState = newValue;
	}

	public String[] getStates() {
		return states;
	}

	public void setStates(String[] newValue) {
		states = newValue;
	}

	public String getPnvgs() {
		return pnvgs;
	}

	public void setPnvgs(String newValue) {
		pnvgs = newValue;
	}

	public String getPlantCommunities() {
		return plantCommunities;
	}

	public void setPlantCommunities(String newValue) {
		plantCommunities = newValue;
	}

	// --- admin unit
	public boolean getByAdminUnit() {
		return byAdminUnit;
	}

	public void setByAdminUnit(boolean newValue) {
		byAdminUnit = newValue;
	}

	public String[] getUnitNames() {
		return unitNames;
	}

	public void setUnitNames(String[] newValue) {
		unitNames = newValue;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String newValue) {
		agency = newValue;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String newValue) {
		region = newValue;
	}

	// --- plant community
	public boolean getByPnvg() {
		return byPnvg;
	}

	public void setByPnvg(boolean newValue) {
		byPnvg = newValue;
	}

	// --- map
	public boolean getByMap() {
		return byMap;
	}

	public void setByMap(boolean newValue) {
		byMap = newValue;
	}

	// new map stuff
	public boolean getByMapAlaska() {
		return byMapAlaska;
	}

	public void setByMapAlaska(boolean newValue) {
		byMapAlaska = newValue;
	}

	public boolean getByMapHawaii() {
		return byMapHawaii;
	}

	public void setByMapHawaii(boolean newValue) {
		byMapHawaii = newValue;
	}

	// end
	public String getInputLongitude() {
		return inputLongitude;
	}

	public void setInputLongitude(String newValue) {
		inputLongitude = newValue;
	}

	public String getInputLatitude() {
		return inputLatitude;
	}

	public void setInputLatitude(String newValue) {
		inputLatitude = newValue;
	}

	public int getGeoRadius() {
		return geoRadius;
	}

	public void setGeoRadius(int newValue) {
		geoRadius = newValue;
	}

	public String buildGeoQuery() {
		logger.info("BEGIN : buildGeoQuery");
		String queryString = "";
		boolean queryOK = true;

		if (byState)
			buildStateGroup();

		if (byPnvg)
			buildPnvgGroup();

		if (byMap) {
			queryOK = buildMapGroup();
		}
		// new map stuff
		if (byMapAlaska) {
			activate = true;
			//
			queryOK = buildMapAlaskaGroup();
		}
		if (byMapHawaii) {
			activate = true;
			//
			queryOK = buildMapHawaiiGroup();
		}
		// end
		if (byAdminUnit) {
			activate = true;
			//
			queryOK = buildAdminUnitGroup();
		}

		addCondition("fr.\"ENTRY_COMPLETE\" = true ", "AND");
		addTable(fromFireRegimeTables);
		if (queryOK) {

			queryString = selectFireRegimeFields
					+ " FROM "
					+ fromTables
					+ " LEFT OUTER JOIN "
					+ " \"FIREREGIME_STATES_LINK\" frsl ON (fr.\"FR_ID\" = frsl.\"FR_ID\")"
					+ " INNER JOIN "
					+ " \"FIREREGIME_SPECIES_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_SPECIES_LINK\".\"FR_ID\")"
					+ " INNER JOIN "
					+ "\"SPECIES\" ON (\"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")"
					+ " left outer join \"FIREREGIME_BPS_LINK\" l on (l.\"FR_ID\" = fr.\"FR_ID\") "
					+ " left outer join \"BPS\" b on (b.\"BPS_ID\" = l.\"BPS_ID\") "
					+ " left outer join \"LU_BPSREGIONS\" lub on (lub.\"BPSREGION_ID\" = b.\"BPSREGION_ID\") "
					+ " WHERE " + joinOf + condition + " ORDER BY "
					+ "fr . \"TITLE\"";
		} else {
			queryString = "";
		}
		log.info(queryString);
		return queryString;
	}

	public String buildNameQuery() {
		logger.info("BEGIN : buildNameQuery");
		String queryString = "";
		String queryInputNameString = checkQueryString(fireRegimeName);

		String queryInputTitleString = checkQueryString(inputTitle);
		addCondition("fr.\"ENTRY_COMPLETE\" = true ", "AND");

		joinOf = " ";
		addNamePortion(queryInputNameString);
		addTitlePortion(queryInputTitleString);
		addStatePortion();
		addPnvgPortion();
		if(!addUSMapPortion()) {
			return null;
		}
		if(!addHIMapPortion()) {
			return null;
		}
		if(!addAKMapPortion()) {
			return null;
		}
		addAdminUnitPortion();

		addTable(fromFireRegimeTables);

		queryString = selectFireRegimeFields + " FROM " + fromTables + joins
				+ joinOf + " WHERE " + condition + " ORDER BY "
				+ "lub.\"BPSREGION\" , " + "fr . \"TITLE\"";

		logger.info("This is the name query : " + queryString);
		return queryString;

	}

	private boolean addUSMapPortion() {
		if (byMap) {
			return buildMapGroup();
		}
		return true;
	}
	
	private boolean addHIMapPortion() {
		if (byMapHawaii) {
			return buildMapHawaiiGroup();
		}
		return true;
	}
	
	private boolean addAKMapPortion() {
		if (byMapAlaska) {
			return buildMapAlaskaGroup();
		}
		return true;
	}

	private void addAdminUnitPortion() {
		if (byAdminUnit) {
			buildAdminUnitGroup();
		}
		
	}

	private void addNamePortion(String queryInputNameString) {
		log.info("addNamePortion : BEGIN " + queryInputNameString + " : "
				+ byName + " : " + isBlank(queryInputNameString));
		if (byName && !isBlank(queryInputNameString)) {
			addExtra(" INNER JOIN  \"FIREREGIME_SPECIES_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_SPECIES_LINK\".\"FR_ID\") ");
			addExtra(" INNER JOIN \"SPECIES\" ON (\"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")");
			addCondition(joinFireRegimeNames, "AND");
			addCondition(joinFireRegimeSpecies, "AND");
			addCondition(joinFireRegimeSpeciesLink, "AND");

			// Multiple Entries Array
			String[] inputNameArray = queryInputNameString.split("\\s*\\,\\s*");

			addTable("\"NAMES\" n");
			joinOf = " ";
			// multiple entries in simple search
			addCondition("(", "AND");
			for (int i = 0; i < inputNameArray.length; i++) {
				log.info(inputNameArray[i]);
				addCondition("OR", "n.\"NAME\" ~* '" + inputNameArray[i] + "'");
				addDescription("Name = " + inputNameArray[i], "OR");
			}
			condition = condition.substring(0, condition.lastIndexOf("OR"));
			addCondition(")", "");
		}
		log.info(condition);
	}
	
	private void addSpeciesPortion(int querySpeciesId) {
		log.info("addSpeciesPortion : BEGIN " + querySpeciesId);
			addExtra(" INNER JOIN  \"FIREREGIME_SPECIES_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_SPECIES_LINK\".\"FR_ID\") ");
		//	addExtra(" INNER JOIN \"SPECIES\" ON (\"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")");
			
			//addCondition(joinFireRegimeSpecies, "AND");
			addCondition(joinFireRegimeSpeciesLink, "AND");

			addCondition("\"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = " + querySpeciesId, "AND"); 
		    speciesName = lookupSpeciesName(querySpeciesId);
			addDescription("Species = " + speciesName, "AND");

		log.info(condition);
	}
	

	private String lookupSpeciesName(int querySpeciesId) {
		String ret = "";
		try{ 
			String q = "select \"COMMONNAME\" from \"SPECIES\" where \"SPP_ID\" = " + querySpeciesId;
			DbAccess da = new DbAccess(q);
			
			ResultSet queryResults = da.ExecuteQuery();
			if(queryResults.next()) {
				ret = queryResults.getString(1);
			}
			queryResults.close();
			
		}
		catch(Exception e) { 
			logger.log(Level.INFO, "unable to get species name for id " + querySpeciesId, e);
		}
		return ret;
	}

	private void addPnvgPortion() {
		if (byPnvg) {
			buildPnvgGroup();
		}
	}

	private void addStatePortion() {
		if (byState) {
			buildStateGroup();
		}
	}

	private void addTitlePortion(String queryInputTitleString) {
		if (!isBlank(queryInputTitleString)) {
			addCondition("fr.\"TITLE\" ~* '" + queryInputTitleString + "'",
					"AND");

				addDescription("Title = " + inputTitle, "AND");
		}

	}

	public String buildHomeNameQuery() {
		logger.info("BEGIN : buildHomeNameQuery");
		String queryString = "";
		String queryInputNameString = "";

		if (byName) {
			queryInputNameString = checkQueryString(fireRegimeName);
			// Multiple Entries Array
			String[] inputNameArray = queryInputNameString.split("\\s*\\,\\s*");
			//
			addDescription("Name = " + fireRegimeName, "OR");

			addJoin(joinFireRegimeNames, "AND");
			addJoin(joinFireRegimeSpecies, "AND");
			addJoin(joinFireRegimeSpeciesLink, "AND");

			addTable(fromFireRegimeTablesTwo);
			addCondition("fr.\"ENTRY_COMPLETE\" = true ", "AND");

			// YNN - Check
			if (!isBlank(fireRegimeName) && isBlank(inputTitle)
					&& isBlank(inputAuthor)) {

				addCondition("(", "AND");
				for (int i = 0; i < inputNameArray.length; i++) {
					log.info(inputNameArray[i]);
					addCondition("OR", "n.\"NAME\" ~* '" + inputNameArray[i]
							+ "'");
				}
				condition = condition.substring(0, condition.lastIndexOf("OR"));
				log.info(condition);

				addCondition(")", "");
				// End Multiple Entries

				queryString = selectFireRegimeFields + " FROM " + fromTables
						+ joins + " WHERE " + joinOf + condition + " ORDER BY "
						+ "lub.\"BPSREGION\" , " + " fr.\"TITLE\"";
			}
		}
		log.info("HOME name query " + queryString);
		return queryString;
	}

	private boolean isBlank(String input) {
		return input == null ? true : input.trim().length() == 0;
	}

	private boolean buildMapGroup() {
		String description = "";
		boolean queryUpdated = true;
		if ("".equals(inputLongitude) || inputLongitude == null) {
			errMsg = "Input Longitude not Specified";
			return false;
		} else if ("".equals(inputLatitude) || inputLatitude == null) {
			errMsg = "Input Longitude not Specified";
			return false;
		}

		if (this.geoRadius == 1)
			description = "1/10 mile Geographic search radius centered at ";
		else
			description = "50 mile Geographic search radius centered at ";

		inputLatitude = inputLatitude.trim();
		inputLongitude = inputLongitude.trim();
		if (!inputLongitude.startsWith("-")) {
			inputLongitude = "-" + inputLongitude;
		}
		description = description + inputLongitude + "," + inputLatitude;

		addDescription(description, "AND");
		String bpsSelections = "";
		String[] bps_codes = bps.split(",");

		for (int i = 0; i < bps_codes.length; i++)
			bpsSelections = bpsSelections.trim() + "'" + bps_codes[i].trim()
					+ "',";
		if (("".equals(bpsSelections) || bpsSelections == null)) {
			log.info("MARKER");
			queryUpdated = false;
			errMsg = "No Results: There are no data for plant communities corresponding to the selected location.";
		} else {
		 
			log.info(bpsSelections);
			if ((bpsSelections.compareTo("'',") != 0)) {
			 
				bpsSelections = bpsSelections.substring(0,
						bpsSelections.length() - 1);

				if (bpsSelections != "''" && !byName) {
					log.info("MARKER 4");

					addCondition("fr.\"FR_ID\" IN (" + bpsSelections + ")",
							"AND");
				} else if (bpsSelections != "''") {
		 
					addExtra("INNER JOIN "
							+ "\"FIREREGIME_BPS_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\")");
					addCondition("fr.\"FR_ID\" IN (" + bpsSelections + ")",
							"AND");
				}
			} else {
				bpsSelections = "0";
		 
				addExtra("INNER JOIN "
						+ "\"FIREREGIME_BPS_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\")");
				addCondition("fr.\"FR_ID\" IN (" + bpsSelections + ")", "AND");
			}
		}
		return queryUpdated;
	}

	// new map queries
	private boolean buildMapAlaskaGroup() {
		log.info("Make Alaska Map Group");
		String description = "";
		boolean queryUpdated = true;
		if ("".equals(inputLongitude) || inputLongitude == null) {
			errMsg = "Input Longitude not Specified";
			return false;
		} else if ("".equals(inputLatitude) || inputLatitude == null) {
			errMsg = "Input Latitude not Specified";
			return false;
		}

		double radius = 0.001;
		if (this.geoRadius == 1)
			description = "1/10 mile Geographic search radius centered at ";
		else
			description = "50 mile Geographic search radius centered at ";

		inputLatitude = inputLatitude.trim();
		inputLongitude = inputLongitude.trim();
		if (!inputLongitude.startsWith("-")) {
			inputLongitude = "-" + inputLongitude;
		}
		description = description + inputLongitude + "," + inputLatitude;

		addDescription(description, "AND");

		String bpsSelections = "";
		String[] bps_codes = bps.split(",");

		for (int i = 0; i < bps_codes.length; i++)
			bpsSelections = bpsSelections.trim() + "'" + bps_codes[i].trim()
					+ "',";
		if (("".equals(bpsSelections) || bpsSelections == null)) {
			queryUpdated = false;
			errMsg = "No Results: There are no data for plant communities corresponding to the selected location.";
		} else {
			log.info("MARKER 2");
			log.info(bpsSelections);
			if ((bpsSelections.compareTo("'',") != 0)) {
				log.info("MARKER 3");
				bpsSelections = bpsSelections.substring(0,
						bpsSelections.length() - 1);
				if (bpsSelections != "''" && !byName) {
					log.info("MARKER 4");
				//	addTable("\"FIREREGIME_BPS_LINK\"");
					addExtra("INNER JOIN "
							+ "\"FIREREGIME_BPS_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\")");
					addCondition("fr.\"FR_ID\" IN (" + bpsSelections + ")",
							"AND");
				} else if (bpsSelections != "''") {
					log.info("MARKER 5");
					addExtra("INNER JOIN "
							+ "\"FIREREGIME_BPS_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\")");
					addCondition("fr.\"FR_ID\" IN (" + bpsSelections + ")",
							"AND");
				}
			} else {
				bpsSelections = "0";
				log.info("MARKER 6");
				// addTable("\"FIRESTUDY_BPS_LINK\"");
				addExtra("INNER JOIN "
						+ "\"FIREREGIME_BPS_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\")");
				addCondition("fr.\"FR_ID\" IN (" + bpsSelections + ")", "AND");
			}
		}
		return queryUpdated;
	}

	// end

	// new map queries
	private boolean buildMapHawaiiGroup() {
		log.info("Make Hawaii Map Group");
		String description = "";
		boolean queryUpdated = true;
		if ("".equals(inputLongitude) || inputLongitude == null) {
			errMsg = "Input Longitude not Specified";
			return false;
		} else if ("".equals(inputLatitude) || inputLatitude == null) {
			errMsg = "Input Latitude not Specified";
			return false;
		}

		double radius = 0.001;
		if (this.geoRadius == 1)
			description = "1/10 mile Geographic search radius centered at ";
		else
			description = "50 mile Geographic search radius centered at ";

		inputLatitude = inputLatitude.trim();
		inputLongitude = inputLongitude.trim();
		if (!inputLongitude.startsWith("-")) {
			inputLongitude = "-" + inputLongitude;
		}
		description = description + inputLongitude + "," + inputLatitude;

		addDescription(description, "AND");

		String bpsSelections = "";
		String[] bps_codes = bps.split(",");

		for (int i = 0; i < bps_codes.length; i++)
			bpsSelections = bpsSelections.trim() + "'" + bps_codes[i].trim()
					+ "',";
		if (("".equals(bpsSelections) || bpsSelections == null)) {
			queryUpdated = false;
			errMsg = "No Results: There is no data for plant communities corresponding to the selected location.";

		} else {
			log.info("MARKER 2");
			log.info(bpsSelections);
			if ((bpsSelections.compareTo("'',") != 0)) {
				log.info("MARKER 3");
				bpsSelections = bpsSelections.substring(0,
						bpsSelections.length() - 1);
				if (bpsSelections != "''" && !byName) {
					log.info("MARKER 4");
			//		addTable("\"FIREREGIME_BPS_LINK\"");
					addExtra("INNER JOIN "
							+ "\"FIREREGIME_BPS_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\")");
					addCondition("fr.\"FR_ID\" IN (" + bpsSelections + ")",
							"AND");
				} else if (bpsSelections != "''") {
					log.info("MARKER 5");
					// addTable("\"FIRESTUDY_BPS_LINK\"");
					addExtra("INNER JOIN "
							+ "\"FIREREGIME_BPS_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\")");
					addCondition("fr.\"FR_ID\" IN (" + bpsSelections + ")",
							"AND");
				}
			} else {
				bpsSelections = "0";
				log.info("MARKER 6");
				// addTable("\"FIRESTUDY_BPS_LINK\"");
				addExtra("INNER JOIN "
						+ "\"FIREREGIME_BPS_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\")");
				addCondition("fr.\"FR_ID\" IN (" + bpsSelections + ")", "AND");
				// queryUpdated = false;
				// errMsg =
				// "No Results: There are no data for plant communities corresponding to the selected location.";
			}
		}
		return queryUpdated;
	}

	// end

	private boolean buildAdminUnitGroup() {
		// may be multiple selections
		String unitSelections = "";
		String descriptionUnits = "";
		// add query selections
		AdminUnitSearch auUtil = new AdminUnitSearch();
		int count = 0;
		boolean retValue = true;
		if (unitNames != null) {
			Map<Integer, String> unitNamesMap = auUtil.getUnitNames(unitNames);
			
			for (Entry<Integer, String> e : unitNamesMap.entrySet()) {
				descriptionUnits = addOrDescription(descriptionUnits,
						"Unit Name = " + e.getValue());
				unitSelections = unitSelections.trim()
						+ e.getKey() + ",";
				count++;
			}
			if (count > 0) {
				descriptionUnits = descriptionUnits + ")";
				addDescription(descriptionUnits, "AND");
			}
		}

		// retrieve all unit names for search
		if ("".equals(unitSelections) || unitSelections == null) {
			unitSelections = auUtil.getCSVUnitNames(this.agency, this.region);
			addDescription("Unit Names = ALL", "AND");
		}

		if (!("".equals(unitSelections) || unitSelections == null)) {

			unitSelections = unitSelections.substring(0,
					unitSelections.length() - 1);
			
			addExtra("INNER JOIN \"FIREREGIME_ADMIN_LINK\" fral ON (fr.\"FR_ID\" = fral.\"FR_ID\")");
			
			addCondition("fral.\"ADMIN_ID\" IN ("
					+ unitSelections + ")", "AND");
		}

		return retValue;
	}

	private boolean buildStateGroup() {

		if ((states == null) || (states.length == 0))
			return false;
		String descriptionStates = "";
		String prefix = "State = ";
		String stateSelections = "";
		for (int i = 0; i < states.length; i++) {

			descriptionStates = addOrDescription(descriptionStates, prefix
					+ states[i].trim());
			stateSelections = stateSelections.trim() + "'" + states[i].trim()
					+ "',";

		}
		if (descriptionStates.length() <= 0)
			return false;
		descriptionStates = descriptionStates + ")";
		addDescription(descriptionStates, "AND");

		if (!("".equals(stateSelections) || stateSelections == null)) {
			stateSelections = stateSelections.substring(0,
					stateSelections.length() - 1);

			addJoin(joinStatesLink, "AND");
			addCondition("frsl.\"ABBR\" IN (" + stateSelections + ")", "AND");
		}
		return true;

	}

	private boolean buildPnvgGroup() {
		String descriptionPnvgs = "";
		String prefix = "Plant Community = ";
		String pnvgSelections = "";

		String[] veg_codes = bps.split(",");
		String[] desc_codes = plantCommunities.split(",");

		// build code query part
		for (int i = 0; i < veg_codes.length; i++) {
			pnvgSelections = pnvgSelections.trim() + "'" + veg_codes[i].trim()
					+ "',";

		}
		// build descriptions (may be different length)
		for (int i = 0; i < desc_codes.length; i++)
			descriptionPnvgs = addOrDescription(descriptionPnvgs, prefix
					+ desc_codes[i].trim());
		descriptionPnvgs = descriptionPnvgs + ")";
		addDescription(descriptionPnvgs, "AND");

		if (pnvgSelections.length() <= 0)
			return false;

		if (!("".equals(pnvgSelections) || pnvgSelections == null)) {
			pnvgSelections = pnvgSelections.substring(0,
					pnvgSelections.length() - 1);
			if (activate) {
				addExtra("INNER JOIN "
						+ "\"FIREREGIME_BPS_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_BPS_LINK\".\"FR_ID\")");
			} else if (!activate) {
				// change bps
				// addTable("\"FIRESTUDY_VEGSYS_LINK\"");
			//addTable("\"FIREREGIME_BPS_LINK\"");
			}
			//addJoin(joinVegSysLink, "AND");
			addCondition("l.\"BPS_ID\" IN (" + pnvgSelections + ")", "AND");
		}
		return true;
	}

	private void addDescription(String strText, String strOperator) {
		if ("".equals(strText) || strText == null)
			return;
		if (description.length() > 0)
			description += " " + strOperator + " " + strText + " ";
		else
			description = strText + " ";
	}

	private void addDesc(String strText, String strOperator) {
		if ("".equals(strText) || strText == null)
			return;
		if (desc.length() > 0)
			desc = desc + " " + strOperator + " " + strText + " ";
		else
			desc = strText + " ";
	}

	private String addOrDescription(String strCurrent, String strNew) {
		if ("".equals(strNew) || strNew == null)
			return strCurrent;
		if ("".equals(strCurrent) || strCurrent == null) {
			strCurrent = "(" + strNew + " ";
		} else
			strCurrent = strCurrent + " OR " + strNew;
		return strCurrent;

	}

	private void addTable(String strText) {

		if ("".equals(strText) || strText == null)
			return;
		if (fromTables.length() > 0)
			fromTables = fromTables + "," + strText + " ";
		else
			fromTables = strText;
	}

	private void addJoin(String strText, String strOperator) {
		if ("".equals(strText) || strText == null)
			return;
		if (joinOf.length() > 0)
			joinOf = joinOf + " " + strOperator + " " + strText + " ";
		else
			joinOf = strText + " ";
	}

	private void addExtra(String strText) {

		if ("".equals(strText) || strText == null)
			return;
		if (joins.length() > 0)
			joins = joins + strText + " ";
		else
			joins = strText;
	}

	private void addCondition(String strText, String strOperator) {
		if ("".equals(strText) || strText == null)
			return;
		if(condition.length() > 0) {
			condition += " " + strOperator + " " + strText + " ";
		}
		else
		{
			condition += " "  + strText + " ";
		}
//		if (joinOf.length() > 0) // conditions follow joins need the operator
//									// first
//			condition += " " + strOperator + " " + strText + " ";
//		else
//			condition += strText + " ";
	}

	protected String checkQueryString(String queryString) {
		// add escape for apostrophe's
		if (queryString == null)
			return "";
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				queryString);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '\'') {
				result.append("'");
				result.append(character);
			} else
				result.append(character);
			character = iterator.next();
		}
		return result.toString();
	}

	public void setSpecies(int speciesId) {
		this.speciesId = speciesId;
		
	}

	public String buildSpeciesQuery() {
		logger.info("BEGIN : buildSpeciesQuery");
		String queryString = "";
				
		addCondition("fr.\"ENTRY_COMPLETE\" = true ", "AND");

		joinOf = " ";
		addSpeciesPortion(speciesId);
		addTable(fromFireRegimeTables);

		queryString = selectFireRegimeFields + " FROM " + fromTables + joins
				+ joinOf + " WHERE " + condition + " ORDER BY "
				+ "lub.\"BPSREGION\" , " + "fr.\"TITLE\"";

		logger.info("buildSpeciesQuery : " + queryString);
		return queryString;
		
	}

	public String getSpeciesName() {
		return speciesName;
	}

	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}

	public void setFireStudyId(String fsId) {
		this.fsId = fsId; 
		
	}

	public String buildFireStudiesQuery() {
		logger.info("BEGIN : buildFireStudiesQuery");
		String queryString = "";
				
		addCondition("fr.\"ENTRY_COMPLETE\" = true ", "AND");

		joinOf = " ";
		addFirestudyPortion(fsId);
		addTable(fromFireRegimeTables);
	 

		queryString = selectFireRegimeFields + " FROM " + fromTables + joins
				+ joinOf + " WHERE " + condition + " ORDER BY "
				+ "lub.\"BPSREGION\" , " + "fr.\"TITLE\"";

		logger.info("buildFireStudiesQuery : " + queryString);
		return queryString;
	}

	private void addFirestudyPortion(String fsId2) {
 
		log.info("addFirestudyPortion : BEGIN " + fsId2);
		addExtra(" INNER JOIN  \"FIREREGIME_SPECIES_LINK\" ON (fr.\"FR_ID\" = \"FIREREGIME_SPECIES_LINK\".\"FR_ID\") ");
		addExtra(" INNER JOIN \"FIRESTUDY_SPECIES_LINK\" ON (\"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\")");
		
		//addCondition(joinFireRegimeSpecies, "AND");
		addCondition(joinFireRegimeSpeciesLink, "AND");

		//addCondition("\"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" in (" + "select \"SPP_ID\" from \"FIRESTUDY_SPECIES_LINK\" where \"FS_ID\" = " + fsId2  + ")",  "AND"); 
		addCondition("\"FIRESTUDY_SPECIES_LINK\".\"FS_ID\" = " + fsId2, "AND");
		
	    
		addDescription("Fire Study = " + fsId2, "AND");

	log.info(condition);
	}

}
