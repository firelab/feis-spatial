/**
 * Class FireStudyQueryBuilder to build postgres query statement and description 
 * for user selected query filters by Name OR One of the selected Geographic Search Items
 */

package com.FEIS;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.logging.Logger;

public class FireStudyQueryBuilder {
	private String description = "";
	private String desc = "";
	private String joinOf = "";
	private String condition = "";
	private String errMsg = "";

	private String acronym = "";

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String newValue) {
		acronym = newValue;
	}

	private String fsID = "";

	public String getFSID() {
		return fsID;
	}

	public void setFSID(String newValue) {
		fsID = newValue;
	}

	boolean activate = false;

	// ------------ geo selections --------------
	private String[] states;
	private String pnvgs;
	private String plantCommunities;

	// geo area - criteria - state, admin, unit, plant community, map
	private boolean byGeoArea = false;
	private int geoArea;

	private boolean byName = false;
	private String inputFSName;
	// New Stuff
	private String inputAuthor;
	private String inputTitle;
	// ////////////////////////////////
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

	// note: distinct pnvgs selected through vegsys_crosswalk (geodata) prior to
	// calling query builder
	private String selectFireStudyFields = "with a as(SELECT DISTINCT "
			+ "\"FIRESTUDY\".\"FS_ID\", "
			+ "\"FIRESTUDY\".\"FS_TYPE\", "
			+ "\"FIRESTUDY\".\"TITLE\", "
			+ "\"FIRESTUDY\".\"FS_AUTHORS\", "
			+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\", "
			+ "\"REVIEWS\".\"HTML_PATH\" AS \"REVIEWS_HTML_PATH\" , "
			+ "\"REVIEWS\".\"ACRONYM\", "
			+ "\"FIRESTUDY\".\"HTML_PATH\", "
			+ "\"FIRESTUDY\".\"YEAR_WRITTEN\", "
			+ "\"LU_FIRESTUDYTYPES\".\"Type\", "
			+ "(Select count(distinct(\"FIREREGIME_SPECIES_LINK\".\"FR_ID\")) from  \"FIREREGIME_SPECIES_LINK\"  where \"FIREREGIME_SPECIES_LINK\".\"SPP_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" ) as \"FR_REFERENCED_COUNT\" ";;



	private String joinStatesLink = "\"FIRESTUDY_STATES_LINK\".\"FS_ID\" = \"FIRESTUDY\".\"FS_ID\"";
	private String joinFirestudyReviews = "\"SPECIES\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\"";
	private String joinAdminLink = "\"FIRESTUDY_ADMIN_LINK\".\"FS_ID\" = \"FIRESTUDY\".\"FS_ID\"";
	// get rid of this for bps
	// private String joinVegSysLink =
	// "\"FIRESTUDY_VEGSYS_LINK\".\"FS_ID\" = \"FIRESTUDY\".\"FS_ID\"";
	private String joinVegSysLink = "\"FIRESTUDY_BPS_LINK\".\"FS_ID\" = \"FIRESTUDY\".\"FS_ID\"";
	private String joinFirestudyTypes = "\"FIRESTUDY\".\"FS_TYPE\" = \"LU_FIRESTUDYTYPES\".\"Type_ID\""; // all
																											// queries
	private String joinFirestudySpeciesLink = "\"FIRESTUDY_SPECIES_LINK\".\"FS_ID\" = \"FIRESTUDY\".\"FS_ID\"";
	private String joinFirestudySpecies = "\"SPECIES\".\"SPP_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\"";
	private String joinFirestudyNames = "\"NAMES\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\"";

	private String fromTables = "";
	private String joins = " LEFT OUTER JOIN "
			+ " \"FIRESTUDY_STATES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_STATES_LINK\".\"FS_ID\")"
			+ " INNER JOIN "
			+ " \"FIRESTUDY_SPECIES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"FS_ID\")"
			+
			" INNER JOIN "
			+ "\"SPECIES\" ON (\"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")";
 
	private String joinsTwo = " LEFT OUTER JOIN "
			+ " \"FIRESTUDY_STATES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_STATES_LINK\".\"FS_ID\")"
			+ " INNER JOIN "
			+ " \"FIRESTUDY_SPECIES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"FS_ID\")"
			+ " INNER JOIN "
			+ "\"SPECIES\" ON (\"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")"
			+
			// " INNER JOIN " +
			// "\"REVIEWS\" ON (\"SPECIES\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\")";
			" LEFT OUTER JOIN "
			+ "\"REVIEWS\" ON (\"SPECIES\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\")";

	private String joinsThree = " LEFT OUTER JOIN "
			+ " \"FIRESTUDY_STATES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_STATES_LINK\".\"FS_ID\")"
			+ " INNER JOIN "
			+ " \"FIRESTUDY_SPECIES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"FS_ID\")"
			+ " INNER JOIN "
			+ "\"SPECIES\" ON (\"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")"
			+
			// " INNER JOIN " +
			// "\"REVIEWS\" ON (\"SPECIES\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\")"
			// +
			" LEFT OUTER JOIN "
			+ "\"REVIEWS\" ON (\"SPECIES\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\")"
			+ " INNER JOIN "
			+ "\"FIRESTUDY_ADMIN_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_ADMIN_LINK\".\"FS_ID\")";

	private String joinsFive = " LEFT OUTER JOIN "
			+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")";

	private String fromFireStudyTables = "\"LU_FIRESTUDYTYPES\",\"FIRESTUDY\""; // all
																				// queries
	private String fromFireStudyTablesTwo = "\"LU_FIRESTUDYTYPES\",\"REVIEWS\",\"FIRESTUDY\""; // all
																								// queries
	private String fromFireStudyTablesThree = "\"LU_FIRESTUDYTYPES\",\"REVIEWS\",\"NAMES\",\"FIRESTUDY\""; // all
	private long frId;
																											// queries
	private static Logger logger = Logger.getLogger(FireStudyQueryBuilder.class.getName());

	public FireStudyQueryBuilder() {
		//
		// constructor logic here
		//
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

	public String getInputFSName() {
		return inputFSName;
	}

	public void setInputFSName(String newValue) {
		inputFSName = newValue;
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

		addJoin(joinFirestudyTypes, "AND");
		addCondition("\"FIRESTUDY\".\"ENTRY_COMPLETE\" = true ", "AND");
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

		addTable(fromFireStudyTables);
		if (queryOK)
			// queryString = selectFireStudyFields + " FROM " + fromTables +
			// " LEFT OUTER JOIN " +
			// " \"FIRESTUDY_STATES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_STATES_LINK\".\"FS_ID\")"
			// + " INNER JOIN " +
			// " \"FIRESTUDY_SPECIES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"FS_ID\")"
			// + " INNER JOIN " +
			// "\"SPECIES\" ON (\"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")"
			// + " INNER JOIN " +
			// "\"REVIEWS\" ON (\"SPECIES\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\")"
			// + " WHERE " + joinOf + condition +
			queryString = selectFireStudyFields
					+ " FROM "
					+ fromTables
					+ " LEFT OUTER JOIN "
					+ " \"FIRESTUDY_STATES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_STATES_LINK\".\"FS_ID\")"
					+ " INNER JOIN "
					+ " \"FIRESTUDY_SPECIES_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"FS_ID\")"
					+ " INNER JOIN "
					+ "\"SPECIES\" ON (\"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\")"
					+ " LEFT OUTER JOIN "
					+ "\"REVIEWS\" ON (\"SPECIES\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\")"
					+ " WHERE "
					+ joinOf
					+ condition
					+ " ORDER BY "
					+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
					+ "\"FIRESTUDY\" . \"TITLE\")"
					+ " select case "
					+ "\"FS_ID\" "
					+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
					+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
					+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
					+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
					+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
					+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
					+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
					+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
					+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
					+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\",  "
					+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" " 
					+ " from (select row_number() over () rno, "
					+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
					+ " from a ) a order by rno, " + "\"ABBR\" , "
					+ "\"TITLE\"" + ";";

		else
			queryString = "";

		System.out.println(queryString);
		return queryString;
	}

	public String buildNameQuery() {
		logger.info("BEGIN : buildNameQuery");
		String queryString = "";
		String queryInputNameString = "";
		String queryInputAuthorString = "";
		String queryInputTitleString = "";
		addJoin(joinFirestudyTypes, "AND");
		addCondition("\"FIRESTUDY\".\"ENTRY_COMPLETE\" = true ", "AND");
		if (byName) {
			queryInputNameString = checkQueryString(inputFSName);
			queryInputTitleString = checkQueryString(inputTitle);
			// Multiple Entries Array
			String[] inputNameArray = queryInputNameString.split("\\s*\\,\\s*");
			//
			queryInputAuthorString = checkQueryString(inputAuthor);
			if (!queryInputNameString.isEmpty()) {
				if (!queryInputTitleString.isEmpty()) {
					if (!queryInputAuthorString.isEmpty()) {
						addDescription("Name = " + inputFSName + ", Title = "
								+ inputTitle + ", Author = " + inputAuthor,
								"OR");
					} else {
						addDescription("Name = " + inputFSName + ", Title = "
								+ inputTitle, "OR");
					}
				} else if (queryInputTitleString.isEmpty()
						&& !queryInputAuthorString.isEmpty()) {
					addDescription("Name = " + inputFSName + ", Author = "
							+ inputAuthor, "OR");
				} else {
					addDescription("Name = " + inputFSName, "OR");
				}
			} else if (!queryInputTitleString.isEmpty()) {
				addDescription("Title = " + inputTitle, "OR");
			} else if (!queryInputAuthorString.isEmpty()) {
				addDescription("Author =" + inputAuthor, "OR");
			}
			addTable("\"NAMES\"");
			addJoin(joinFirestudyNames, "AND");
			addJoin(joinFirestudySpecies, "AND");
			addJoin(joinFirestudySpeciesLink, "AND");
			// this needed to be changed to 'fromFireStudyTablesTwo' in order to
			// get 'Pyrularia pubera' and others like it to work.
			addTable(fromFireStudyTables);

			// YYY - Check
			if (!isBlank(inputFSName) && !isBlank(inputTitle)
					&& !isBlank(inputAuthor)) {
				boolean queryOK = true;
				if (byState) {
					System.out.println("byState ACTIVATED");
					buildStateGroup();
				}
				if (byPnvg) {
					activate = true;
					System.out.println("byPnvg ACTIVATED");
					buildPnvgGroup();
				}

				if (byMap) {
					activate = true;
					System.out.println("byMap ACTIVATED");
					queryOK = buildMapGroup();
				}
				// new map stuff
				if (byMapAlaska) {
					activate = true;
					queryOK = buildMapAlaskaGroup();
				}
				if (byMapHawaii) {
					activate = true;
					queryOK = buildMapHawaiiGroup();
				}
				// end
				if (byAdminUnit) {
					activate = true;
					System.out.println("byAdminUnit ACTIVATED");
					queryOK = buildAdminUnitGroup();

					addCondition("(\"NAMES\".\"NAME\" ~* '"
							+ queryInputNameString + "'", "AND");
					addCondition("\"FIRESTUDY\".\"TITLE\" ~* '"
							+ queryInputTitleString + "'", "AND");
					addCondition("\"FIRESTUDY\".\"FS_AUTHORS\" ~* '"
							+ queryInputAuthorString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromFireStudyTablesThree
							+ joins
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				} else {
					addCondition("(\"NAMES\".\"NAME\" ~* '"
							+ queryInputNameString + "'", "AND");
					addCondition("\"FIRESTUDY\".\"TITLE\" ~* '"
							+ queryInputTitleString + "'", "AND");
					addCondition("\"FIRESTUDY\".\"FS_AUTHORS\" ~* '"
							+ queryInputAuthorString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromFireStudyTablesThree
							+ joins
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				}
			}

			// YYN - Check
			if (!isBlank(inputFSName) && !isBlank(inputTitle)
					&& isBlank(inputAuthor)) {
				boolean queryOK = true;
				if (byState) {
					System.out.println("byState ACTIVATED");
					buildStateGroup();
				}
				if (byPnvg) {
					activate = true;
					System.out.println("byPnvg ACTIVATED");
					buildPnvgGroup();
				}

				if (byMap) {
					activate = true;
					System.out.println("byMap ACTIVATED");
					queryOK = buildMapGroup();
				}
				// new map stuff
				if (byMapAlaska) {
					activate = true;
					queryOK = buildMapAlaskaGroup();
				}
				if (byMapHawaii) {
					activate = true;
					queryOK = buildMapHawaiiGroup();
				}
				// end
				if (byAdminUnit) {
					activate = true;
					System.out.println("byAdminUnit ACTIVATED");
					queryOK = buildAdminUnitGroup();

					addCondition("(\"NAMES\".\"NAME\" ~* '"
							+ queryInputNameString + "'", "AND");
					addCondition("\"FIRESTUDY\".\"TITLE\" ~* '"
							+ queryInputTitleString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromFireStudyTablesThree
							+ joins
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				} else {
					addCondition("(\"NAMES\".\"NAME\" ~* '"
							+ queryInputNameString + "'", "AND");
					addCondition("\"FIRESTUDY\".\"TITLE\" ~* '"
							+ queryInputTitleString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromFireStudyTablesThree
							+ joins
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				}
			}
			// YNY - Check
			if (!isBlank(inputFSName) && isBlank(inputTitle)
					&& !isBlank(inputAuthor)) {
				boolean queryOK = true;
				if (byState) {
					System.out.println("byState ACTIVATED");
					buildStateGroup();
				}
				if (byPnvg) {
					activate = true;
					System.out.println("byPnvg ACTIVATED");
					buildPnvgGroup();
				}

				if (byMap) {
					activate = true;
					System.out.println("byMap ACTIVATED");
					queryOK = buildMapGroup();
				}
				// new map stuff
				if (byMapAlaska) {
					activate = true;
					queryOK = buildMapAlaskaGroup();
				}
				if (byMapHawaii) {
					activate = true;
					queryOK = buildMapHawaiiGroup();
				}
				// end
				if (byAdminUnit) {
					activate = true;
					System.out.println("byAdminUnit ACTIVATED");
					queryOK = buildAdminUnitGroup();

					addCondition("(\"NAMES\".\"NAME\" ~* '"
							+ queryInputNameString + "'", "AND");
					addCondition("\"FIRESTUDY\".\"FS_AUTHORS\" ~* '"
							+ queryInputAuthorString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromFireStudyTablesThree
							+ joins
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				} else {
					addCondition("(\"NAMES\".\"NAME\" ~* '"
							+ queryInputNameString + "'", "AND");
					addCondition("\"FIRESTUDY\".\"FS_AUTHORS\" ~* '"
							+ queryInputAuthorString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromFireStudyTablesThree
							+ joins
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				}
			}
			// YNN - Check
			if (!isBlank(inputFSName) && isBlank(inputTitle)
					&& isBlank(inputAuthor)) {
				System.out.println("YNN");
				boolean queryOK = true;
				if (byState) {
					System.out.println("byState ACTIVATED");
					buildStateGroup();
				}
				if (byPnvg) {
					activate = true;
					System.out.println("byPnvg ACTIVATED");
					buildPnvgGroup();
				}

				if (byMap) {
					activate = true;
					System.out.println("byMap ACTIVATED");
					queryOK = buildMapGroup();
				}
				// new map stuff
				if (byMapAlaska) {
					activate = true;
					System.out.println("YNN ALASKA ACTIVATED");
					queryOK = buildMapAlaskaGroup();
				}
				if (byMapHawaii) {
					activate = true;
					queryOK = buildMapHawaiiGroup();
				}
				// end
				if (byAdminUnit) {
					activate = true;
					System.out.println("byAdminUnit ACTIVATED");
					queryOK = buildAdminUnitGroup();

					addCondition("(\"NAMES\".\"NAME\" ~* '"
							+ queryInputNameString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromFireStudyTablesThree
							+ joins
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\",  "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				} else {
					// multiple entries in simple search
					addCondition("(", "AND");
					for (int i = 0; i < inputNameArray.length; i++) {
						System.out.println(inputNameArray[i]);
						addCondition("OR", "\"NAMES\".\"NAME\" ~* '"
								+ inputNameArray[i] + "'");
					}
					condition = condition.substring(0,
							condition.lastIndexOf("OR"));
					System.out.println(condition);

					addCondition(")", "");
					// End Multiple Entries

					// addCondition("(\"NAMES\".\"NAME\" ~* '" +
					// queryInputNameString + "')","AND" );
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromFireStudyTablesThree
							+ joins
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				}
			}
			// NNN - Check
			if (isBlank(inputFSName) && isBlank(inputTitle)
					&& isBlank(inputAuthor)) {
				// Do Nothing : Should Return EVERYTHING
			}
			// NNY - Check
			if (isBlank(inputFSName) && isBlank(inputTitle)
					&& !isBlank(inputAuthor)) {
				boolean queryOK = true;
				if (byState) {
					System.out.println("byState ACTIVATED");
					buildStateGroup();
				}
				if (byPnvg) {
					activate = true;
					System.out.println("byPnvg ACTIVATED");
					buildPnvgGroup();
				}

				if (byMap) {
					activate = true;
					System.out.println("byMap ACTIVATED");
					queryOK = buildMapGroup();
				}
				// new map stuff
				if (byMapAlaska) {
					activate = true;
					queryOK = buildMapAlaskaGroup();
				}
				if (byMapHawaii) {
					activate = true;
					queryOK = buildMapHawaiiGroup();
				}
				// end
				if (byAdminUnit) {
					activate = true;
					System.out.println("NNY 1");
					System.out.println("byAdminUnit ACTIVATED");
					queryOK = buildAdminUnitGroup();

					addCondition("(\"FIRESTUDY\".\"FS_AUTHORS\" ~* '"
							+ queryInputAuthorString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromTables
							+ joinsThree
							+ joinsFive
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				} else {
					System.out.println("NNY 2");
					addCondition("(\"FIRESTUDY\".\"FS_AUTHORS\" ~* '"
							+ queryInputAuthorString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromTables
							+ joinsTwo
							+ joinsFive
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				}
			}
			// NYN - Check
			if (isBlank(inputFSName) && !isBlank(inputTitle)
					&& isBlank(inputAuthor)) {
				boolean queryOK = true;
				if (byState) {
					System.out.println("byState ACTIVATED");
					buildStateGroup();
				}
				if (byPnvg) {
					activate = true;
					System.out.println("byPnvg ACTIVATED");
					buildPnvgGroup();
				}

				if (byMap) {
					activate = true;
					System.out.println("byMap ACTIVATED");
					queryOK = buildMapGroup();
				}
				// new map stuff
				if (byMapAlaska) {
					activate = true;
					queryOK = buildMapAlaskaGroup();
				}
				if (byMapHawaii) {
					activate = true;
					queryOK = buildMapHawaiiGroup();
				}
				// end
				if (byAdminUnit) {
					activate = true;
					System.out.println("byAdminUnit ACTIVATED");
					queryOK = buildAdminUnitGroup();

					addCondition("(\"FIRESTUDY\".\"TITLE\" ~* '"
							+ queryInputTitleString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromTables
							+ joinsThree
							+ joinsFive
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\" "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				} else {
					addCondition("(\"FIRESTUDY\".\"TITLE\" ~* '"
							+ queryInputTitleString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromTables
							+ joinsTwo
							+ joinsFive
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				}
			}
			// NYY - Check
			if (isBlank(inputFSName) && !isBlank(inputTitle)
					&& !isBlank(inputAuthor)) {
				boolean queryOK = true;
				if (byState) {
					System.out.println("byState ACTIVATED");
					buildStateGroup();
				}
				if (byPnvg) {
					activate = true;
					System.out.println("byPnvg ACTIVATED");
					buildPnvgGroup();
				}

				if (byMap) {
					activate = true;
					System.out.println("byMap ACTIVATED");
					queryOK = buildMapGroup();
				}
				// new map stuff
				if (byMapAlaska) {
					activate = true;
					queryOK = buildMapAlaskaGroup();
				}
				if (byMapHawaii) {
					activate = true;
					queryOK = buildMapHawaiiGroup();
				}
				// end
				if (byAdminUnit) {
					activate = true;
					System.out.println("byAdminUnit ACTIVATED");
					queryOK = buildAdminUnitGroup();

					addCondition("(\"FIRESTUDY\".\"TITLE\" ~* '"
							+ queryInputTitleString + "'", "AND");
					addCondition("\"FIRESTUDY\".\"FS_AUTHORS\" ~* '"
							+ queryInputAuthorString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromTables
							+ joinsThree
							+ joinsFive
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+
							// "\"ACRONYM\", " +
							//
							" case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				} else {
					// does this work?
					addCondition("(\"FIRESTUDY\".\"FS_AUTHORS\" ~* '"
							+ queryInputAuthorString + "'", "AND");
					addCondition("\"FIRESTUDY\".\"TITLE\" ~* '"
							+ queryInputTitleString + "')", "AND");
					queryString = selectFireStudyFields
							+ " FROM "
							+ fromTables
							+ joinsTwo
							+ joinsFive
							+ " WHERE "
							+ joinOf
							+ condition
							+

							" ORDER BY "
							+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
							+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
							+ " select case "
							+ "\"FS_ID\" "
							+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
							+
							// "\"ACRONYM\", " +
							//
							" case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
							+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\" "
							+ " from (select row_number() over () rno, "
							+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\" "
							+ " from a ) a order by rno, " + "\"ABBR\" , "
							+ "\"FS_AUTHORS\"" + ";";
				}
			}
		}
	
		
		logger.info("This is the name query : " + queryString);
		return queryString;
	}

	public String buildHomeNameQuery() {
		logger.info(
				"BEGIN : buildHomeNameQuery");
		String queryString = "";
		String queryInputNameString = "";
		addJoin(joinFirestudyTypes, "AND");
		if (byName) {
			queryInputNameString = checkQueryString(inputFSName);
			// Multiple Entries Array
			String[] inputNameArray = queryInputNameString.split("\\s*\\,\\s*");
			//
			addDescription("Name = " + inputFSName, "OR");
			addTable("\"NAMES\"");
			addJoin(joinFirestudyNames, "AND");
			addJoin(joinFirestudySpecies, "AND");
			addJoin(joinFirestudySpeciesLink, "AND");
			//addJoin(joinFirestudyReviews, "AND");
			addTable(fromFireStudyTablesTwo);
			addCondition("\"FIRESTUDY\".\"ENTRY_COMPLETE\" = true ", "AND");
			
			// YNN - Check
			if (!isBlank(inputFSName) && isBlank(inputTitle)
					&& isBlank(inputAuthor)) {
				boolean queryOK = true;
				// multiple entries in simple search
				addCondition("(", "AND");
				for (int i = 0; i < inputNameArray.length; i++) {
					System.out.println(inputNameArray[i]);
					addCondition("OR", "\"NAMES\".\"NAME\" ~* '"
							+ inputNameArray[i] + "'");
				}
				condition = condition.substring(0, condition.lastIndexOf("OR"));
				System.out.println(condition);

				addCondition(")", "");
				// End Multiple Entries

				queryString = selectFireStudyFields
						+ " FROM "
						+ fromTables
						+ joins
						+ " WHERE "
						+ joinOf
						+ condition
						+

						" ORDER BY "
						+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
						+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
						+ " select case "
						+ "\"FS_ID\" "
						+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
						+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
						+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
						+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
						+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
						+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
						+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
						+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
						+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
						+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
						+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
						+ " from (select row_number() over () rno, "
						+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
						+ " from a ) a order by rno, " + "\"ABBR\" , "
						+ "\"FS_AUTHORS\"" + ";";
			}
		}
		System.out.println("This is the HOME name query " + queryString);
		return queryString;
	}

	private boolean isBlank(String input) {
		return input == null ? true : input.trim().length() == 0;
	}

	// END NEW TEST STUFF

	String buildMagicString(String sppAcronym) {
		String description = sppAcronym;
		addDescription("Acronym = " + description, null);
 
 
		addCondition("(\"NAMES\".\"NAME\" ~* '" + sppAcronym + "')", "AND");
		String newQueryString = selectFireStudyFields
				+ " FROM "
				+ "\"NAMES\",\"LU_FIRESTUDYTYPES\",\"FIRESTUDY\" "
				+ fromTables
				+ joinsTwo
				+ " WHERE "
				+ "\"FIRESTUDY\".\"FS_TYPE\" = \"LU_FIRESTUDYTYPES\".\"Type_ID\" "
				+ " AND "
				+ "\"NAMES\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\" "
				+ " AND "
				+ "\"SPECIES\".\"SPP_ID\" = \"FIRESTUDY_SPECIES_LINK\".\"SPP_ID\" "
				+ " AND "
				+ "\"FIRESTUDY_SPECIES_LINK\".\"FS_ID\" = \"FIRESTUDY\".\"FS_ID\" "
				+ " AND "
				+ condition
				+

				" ORDER BY "
				+ "\"FIRESTUDY_STATES_LINK\".\"ABBR\" , "
				+ "\"FIRESTUDY\" . \"FS_AUTHORS\")"
				+ " select case "
				+ "\"FS_ID\" "
				+ " when lag  (\"FS_ID\") over () then null else \"FS_ID\" end, "
				+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_TYPE\" end as \"FS_TYPE\", "
				+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"TITLE\" end as \"TITLE\", "
				+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FS_AUTHORS\" end as \"FS_AUTHORS\", "
				+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ABBR\" end as \"ABBR\", "
				+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"REVIEWS_HTML_PATH\" end AS \"REVIEWS_HTML_PATH\", "
				+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"ACRONYM\" end AS \"ACRONYM\", "
				+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"HTML_PATH\" end as \"HTML_PATH\", "
				+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"YEAR_WRITTEN\" end as \"YEAR_WRITTEN\", "
				+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"Type\" end as \"Type\", "
				+ " case \"FS_ID\" when lag(\"FS_ID\") over () then null else \"FR_REFERENCED_COUNT\" end as \"FR_REFERENCED_COUNT\" "
				+ " from (select row_number() over () rno, "
				+ "\"FS_ID\", \"FS_TYPE\", \"TITLE\",\"FS_AUTHORS\", \"ABBR\", \"REVIEWS_HTML_PATH\", \"ACRONYM\", \"HTML_PATH\", \"YEAR_WRITTEN\", \"Type\", \"FR_REFERENCED_COUNT\" "
				+ " from a ) a order by rno, " + "\"ABBR\" , "
				+ "\"FS_AUTHORS\"" + ";";
		/* end old junk */

		System.out.println("Is this the failed query?"); // Yes it is...This is
															// the query that
															// occurs when you
															// select an FS item
															// from species
															// reviews
		System.out.println(newQueryString);

		return newQueryString;
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
			System.out.println("MARKER");
			queryUpdated = false;
			errMsg = "No Results: There are no data for plant communities corresponding to the selected location.";
		} else {
			System.out.println("MARKER 2");
			System.out.println(bpsSelections);
			if ((bpsSelections.compareTo("'',") != 0)) {
				System.out.println("MARKER 3");
				bpsSelections = bpsSelections.substring(0,
						bpsSelections.length() - 1);
				// if (!byName)
				if (bpsSelections != "''" && !byName) {
					System.out.println("MARKER 4");
//					addTable("\"FIRESTUDY_BPS_LINK\"");
//					addExtra("INNER JOIN "
//							+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")");
					addCondition("\"FIRESTUDY\".\"FS_ID\" IN (" + bpsSelections
							+ ")", "AND");
				} else if (bpsSelections != "''") {
					System.out.println("MARKER 5");
					// addTable("\"FIRESTUDY_BPS_LINK\"");
					addExtra("INNER JOIN "
							+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")");
					addCondition("\"FIRESTUDY\".\"FS_ID\" IN (" + bpsSelections
							+ ")", "AND");
				}
			} else {
				bpsSelections = "0";
				System.out.println("MARKER 6");
				// addTable("\"FIRESTUDY_BPS_LINK\"");
				addExtra("INNER JOIN "
						+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")");
				addCondition("\"FIRESTUDY\".\"FS_ID\" IN (" + bpsSelections
						+ ")", "AND");
				// queryUpdated = false;
				// errMsg =
				// "No Results: There are no data for plant communities corresponding to the selected location.";
			}
		}
		return queryUpdated;
	}

	// new map queries
	private boolean buildMapAlaskaGroup() {
		System.out.println("Make Alaska Map Group");
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
			System.out.println("MARKER 2");
			System.out.println(bpsSelections);
			if ((bpsSelections.compareTo("'',") != 0)) {
				System.out.println("MARKER 3");
				bpsSelections = bpsSelections.substring(0,
						bpsSelections.length() - 1);
				if (bpsSelections != "''" && !byName) {
					System.out.println("MARKER 4");
					addTable("\"FIRESTUDY_BPS_LINK\"");
					addExtra("INNER JOIN "
							+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")");
					addCondition("\"FIRESTUDY\".\"FS_ID\" IN (" + bpsSelections
							+ ")", "AND");
				} else if (bpsSelections != "''") {
					System.out.println("MARKER 5");
					// addTable("\"FIRESTUDY_BPS_LINK\"");
					addExtra("INNER JOIN "
							+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")");
					addCondition("\"FIRESTUDY\".\"FS_ID\" IN (" + bpsSelections
							+ ")", "AND");
				}
			} else {
				bpsSelections = "0";
				System.out.println("MARKER 6");
				// addTable("\"FIRESTUDY_BPS_LINK\"");
				addExtra("INNER JOIN "
						+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")");
				addCondition("\"FIRESTUDY\".\"FS_ID\" IN (" + bpsSelections
						+ ")", "AND");
				// queryUpdated = false;
				// errMsg =
				// "No Results: There are no data for plant communities corresponding to the selected location.";
			}
		}
		return queryUpdated;
	}

	// end

	// new map queries
	private boolean buildMapHawaiiGroup() {
		System.out.println("Make Hawaii Map Group");
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
			System.out.println("MARKER 2");
			System.out.println(bpsSelections);
			if ((bpsSelections.compareTo("'',") != 0)) {
				System.out.println("MARKER 3");
				bpsSelections = bpsSelections.substring(0,
						bpsSelections.length() - 1);
				if (bpsSelections != "''" && !byName) {
					System.out.println("MARKER 4");
					addTable("\"FIRESTUDY_BPS_LINK\"");
					addExtra("INNER JOIN "
							+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")");
					addCondition("\"FIRESTUDY\".\"FS_ID\" IN (" + bpsSelections
							+ ")", "AND");
				} else if (bpsSelections != "''") {
					System.out.println("MARKER 5");
					// addTable("\"FIRESTUDY_BPS_LINK\"");
					addExtra("INNER JOIN "
							+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")");
					addCondition("\"FIRESTUDY\".\"FS_ID\" IN (" + bpsSelections
							+ ")", "AND");
				}
			} else {
				bpsSelections = "0";
				System.out.println("MARKER 6");
				// addTable("\"FIRESTUDY_BPS_LINK\"");
				addExtra("INNER JOIN "
						+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")");
				addCondition("\"FIRESTUDY\".\"FS_ID\" IN (" + bpsSelections
						+ ")", "AND");
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
			for (int i = 0; i < unitNames.length; i++) {
				if (unitNames[i].compareTo("0") != 0) {
					count++;
					unitSelections = unitSelections.trim()
							+ unitNames[i].trim() + ",";
					auUtil.getUnitName(unitNames[i].trim());
					descriptionUnits = addOrDescription(descriptionUnits,
							"Unit Name = " + auUtil.getNames1());
				}
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
			System.out.println("I made it!!!");
			unitSelections = unitSelections.substring(0,
					unitSelections.length() - 1);
			if (activate) {
				System.out.println("Admin Table Marker 1");
				addTable("\"FIRESTUDY_ADMIN_LINK\"");
				addExtra("INNER JOIN "
						+ "\"FIRESTUDY_ADMIN_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_ADMIN_LINK\".\"FS_ID\")");
			} else if (!activate) {
				System.out.println("Admin Table Marker 2");
				addTable("\"FIRESTUDY_ADMIN_LINK\"");
			}
			System.out.println("Admin Table Marker 3");
			addJoin(joinAdminLink, "AND");
			addCondition("\"FIRESTUDY_ADMIN_LINK\".\"ADMIN_ID\" IN ("
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
			// addTable("\"FIRESTUDY_STATES_LINK\"");
			addJoin(joinStatesLink, "AND");
			addCondition("\"FIRESTUDY_STATES_LINK\".\"ABBR\" IN ("
					+ stateSelections + ")", "AND");
		}
		return true;

	}

	private boolean buildPnvgGroup() {
		// NOTE: pnvg codes are selected from the vegsys crosswalk first
		// (geoData)

		String descriptionPnvgs = "";
		String prefix = "Plant Community = ";
		String pnvgSelections = "";

		// String[] veg_codes = pnvgs.split(",");
		String[] veg_codes = bps.split(",");
		String[] desc_codes = plantCommunities.split(",");

		// build code query part
		for (int i = 0; i < veg_codes.length; i++) {
			pnvgSelections = pnvgSelections.trim() + "'" + veg_codes[i].trim()
					+ "',";
			// descriptionPnvgs =
			// addOrDescription(descriptionPnvgs,prefix+veg_codes[i].trim());
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
				// change bps
				// addExtra("INNER JOIN " +
				// "\"FIRESTUDY_VEGSYS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_VEGSYS_LINK\".\"FS_ID\")");
				addExtra("INNER JOIN "
						+ "\"FIRESTUDY_BPS_LINK\" ON (\"FIRESTUDY\".\"FS_ID\" = \"FIRESTUDY_BPS_LINK\".\"FS_ID\")");
			} else if (!activate) {
				// change bps
				// addTable("\"FIRESTUDY_VEGSYS_LINK\"");
				addTable("\"FIRESTUDY_BPS_LINK\"");
			}
			addJoin(joinVegSysLink, "AND");
			addCondition("\"FIRESTUDY_BPS_LINK\".\"BPS_ID\" IN ("
					+ pnvgSelections + ")", "AND");
		}
		return true;
	}

	// -------------------query builder helper methods -------------
	private void addDescription(String strText, String strOperator) {
		if ("".equals(strText) || strText == null)
			return;
		if (description.length() > 0)
			description = description + " " + strOperator + " " + strText + " ";
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
		if (joinOf.length() > 0) // conditions follow joins need the operator
									// first
			condition = condition + " " + strOperator + " " + strText + " ";
		else
			condition = strText + " ";
	}

	protected String checkQueryString(String queryString) {
		// add escape for apostrophe's
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

	public void setFireRegimeId(long frId) {
		this.frId = frId;
	}

	public String buildFireRegimeQuery() {
		String query = "select distinct fs.\"FS_ID\",  " +
				"			 fr.\"FR_ID\", 	 " + 
				"			 fs.\"TITLE\", " + 
				"			 fs.\"FS_AUTHORS\", " + 
				"			 fstl.\"ABBR\", " + 
				"			 fs.\"HTML_PATH\", " + 
				"			 fs.\"YEAR_WRITTEN\", " + 
				"			 (Select count(distinct(fr2.\"FR_ID\")) from  \"FIREREGIME_SPECIES_LINK\" frsl2 inner join \"FIREREGIME\" fr2 on (fr2.\"FR_ID\" = frsl2.\"FR_ID\") where frsl2.\"SPP_ID\" = frsl.\"SPP_ID\" and fr2.\"ENTRY_COMPLETE\" = true ) as \"FR_REFERENCED_COUNT\", " + 
				"			 'na' AS \"REVIEWS_HTML_PATH\", " + 
				"			 'na' as \"ACRONYM\"" + 
				"	from \"FIRESTUDY\" fs " + 
				"	inner join \"FIRESTUDY_STATES_LINK\" fstl on (fstl.\"FS_ID\" = fs.\"FS_ID\")" + 
				"	inner join \"FIRESTUDY_SPECIES_LINK\" fssl on (fssl.\"FS_ID\" = fs.\"FS_ID\")" + 
				"	inner join \"FIREREGIME_SPECIES_LINK\" frsl on (fssl.\"SPP_ID\" = frsl.\"SPP_ID\")" + 
				"	inner join \"FIREREGIME\" fr on (fr.\"FR_ID\" = frsl.\"FR_ID\")" + 
				"	inner join \"SPECIES\" s on (s.\"SPP_ID\" = fssl.\"SPP_ID\") " + 
				"	inner join \"REVIEWS\" r on (r.\"REVIEW_ID\" = s.\"REVIEW_ID\") " + 
				"where " + 
				"	fs.\"ENTRY_COMPLETE\" = true " + 
				"	and fr.\"ENTRY_COMPLETE\" = true " + 
				"	and fr.\"FR_ID\" = " + frId + 
				" order by fstl.\"ABBR\", fs.\"TITLE\"";
		addDescription("Fire Regime ID = " + frId, "AND");
		return query; 
	}

}
