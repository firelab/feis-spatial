/**
 * FEISSearch class contains properties for GUI to conduct search.
 */
package com.FEIS;

import com.FEIS.types.FireRegime;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.logging.Logger;

@ManagedBean(name = "searchBean")
@SessionScoped
public class FEISSearch implements Serializable {

	private static final String MAP_SEARCHES_MSG = "Map location searches are based on Biophysical Settings.";
	private static final String PLANT_COMMUNITY_MSG = "";
	private static final String ADMINISTRATIVE_UNIT_MSG = "Administrative unit searches are based on the overlap between agency boundaries and Biophysical Settings.";

	private static Logger logger = Logger
			.getLogger(FEISSearch.class.getName());
	
	String sppAcronym = "";
	String sppFSID = "";
	String inputFrName = "";

	private boolean byMagic;
	private int speciesId; 
	private boolean byMoreMagic;

	// ------ Search by Name page fields -------
	private String inputName; // user input search name on search by name page.
	private boolean byName;

	private String inputFSName;
	private boolean byFSName;

	// New Stuff
	private String inputAuthor;
	private String inputTitle;

	// ------ Search by Other page fields
	// ------------------------------Life Form
	// ----------------------------------------------------------
	private boolean byLifeForm; // selectBooleanCheckBox - LifeForm
	private static Map<String, Object> plantItems; // select options for
													// Lifeform - plants
	static {
		plantItems = new LinkedHashMap<String, Object>();
		plantItems.put("Bryophyte", "12");
		plantItems.put("Cactus", "13");
		plantItems.put("Fern or fern ally", "11");
		plantItems.put("Forb", "6");
		plantItems.put("Graminoid", "5");
		plantItems.put("Shrub", "2");
		plantItems.put("Tree", "1");
		plantItems.put("Vine or liana", "10");
	}

	private String[] plants; // selected plants for lifeForm

	private static Map<String, Object> animalItems; // select options for
													// Lifeform - Animals
	static {
		animalItems = new LinkedHashMap<String, Object>();
		animalItems.put("Amphibian", "8");
		animalItems.put("Arthropod", "3");
		animalItems.put("Bird", "4");
		animalItems.put("Mammal", "7");
		animalItems.put("Reptile", "9");
	}

	private String[] animals; // selected Animals for lifeForm
	private Map<String, String> items;

	private boolean lichenAll; // selectBooleanCheckBox - LifeForm - lichens All
	private boolean fungusAll; // selectBooleanCheckBox - LifeForm - lichens All
	private boolean plantsAll; // Lifeform - plants All
	private boolean animalsAll; // Lifeform - animals All
	// ------------------------------ Life Form
	// ----------------------------------------------------------

	// ------------------------------ Nativity
	// ----------------------------------------------------------
	private boolean byNativity; // selectBooleanCheckBox - nativity
	private int nativity; // selected nativity item value
	// ---------------------------------------------------------------------------------------------------

	// ------------------------------ Invasive
	// --------------------------------------------------
	private boolean byInvasiveness; // selectBooleanCheckBox - invasiveness
	private int invasiveness; // selected invasiveness item value
	// ------------------------------------------------------------------------------------------

	// new stuff
	private boolean byFedStatus;
	private String fedStatus;

	private boolean byNewStates;
	private String newStates;
	//

	// ----- Navigation
	private String outCome = null; // Navigation target page name

	// ------ Report part fields and flags
	private boolean displayReport = false; // flag to indicate if the report
											// panel is rendered.
	private boolean hasResult = false; // flag to indicate if there's any SPP
										// result returned
	private boolean hasLink = false;
	private String reportDescription; // description of report
	private String reviewResultsSQL;
	private String fireStudyResultsSQL;

	private boolean byMagicString;
	private String magicString;

	private boolean byMoreMagicString;
	private String moreMagicString;

	private String reportErrorMsg;

	private String numSPPReturned; // Report summary - number of species
									// returned.
	private String numFSReturned; // Report summary - number of Fire Study
									// returned.

	// new stuff
	private String adminJunk;
	private String mapJunk;
	private String locationJunk;
	private String plantJunk;

	
	private String fsResultsTitleOne;
	private String fsResultsTitleTwo;


	
	private String speciesResultsTitleOne;
	private String speciesResultsTitleTwo;
	// end new stuff for alternate results page titles

	private ArrayList<SPP> speciesList; // Report - SPP list returned.
	private ArrayList<FireStudy> fireStudyList; // Fire Study Search
	private List<FireRegime> fireRegimeList; // Fire Study Search

	private String inputLongitude;
	private String inputLatitude;

	private int geoRadius; // radius selected

	
	private String fireRegimeName = "";
	private String fireRegimeTitle = "";
	private String frResultsTitleOne = "";
	private String frResultsTitleTwo = "";
	private int fireRegimeResultCount = 0; 
	
	// constructor - initialize GUI, such as populating the list, select, etc
	public FEISSearch() {
		// populate plants selectmanyCheckbox
		// this.geoRadius = 1;
		// default becomes 50 mile radius search
		this.geoRadius = 2;
		speciesList = new ArrayList<SPP>();
		fireStudyList = new ArrayList<FireStudy>();
		setInputName("");
		setInputFsName("");
		// New Stuff
		setInputAuthor("");
		setInputTitle("");
		//
		this.byState = false;
		this.byAdminUnit = false;
		// new map layers
		// end new map layers
		this.byPnvg = false;
		this.byMap = false;
		// new map layers
		this.byMapAlaska = false;
		this.byMapHawaii = false;
		//
		setInvasiveness(0);
		// new stuff
		setFedStatus("");
		setNewStates("");
		//
		this.byLifeForm = false;
		setByLifeForm(false);
		this.byNativity = false;
		setByNativity(false);
		setNativity(0);
		this.byInvasiveness = false;
		setByInvasiveness(false);
		// new stuff
		this.byFedStatus = false;
		setByFedStatus(false);
		this.byNewStates = false;
		setByNewStates(false);
		//
		this.byGeoArea = false;
		this.geoArea = 0;
		// new stuff
		this.byOtherArea = false;
		// new
		this.byNewMapLayers = false;
		this.newMapLayers = 0;
		//
		this.otherArea = 0;
		//
		this.byName = false;
		setReportErrorMsg("");
		this.reportDescription = "";
		this.reviewResultsSQL = "";
		this.fireStudyResultsSQL = "";
		this.magicString = "";

	}

	public boolean getByMagic() {
		return byMagic;
	}

	public void setByMagic(boolean newValue) {
		byMagic = newValue;
	}

	public boolean getByMoreMagic() {
		return byMoreMagic;
	}

	public void setByMoreMagic(boolean newValue) {
		byMoreMagic = newValue;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String newValue) {
		inputName = newValue;
	}

	public String getInputFsName() {
		return inputFSName;
	}

	public void setInputFsName(String newValue) {
		inputFSName = newValue;
	}

	// New Stuff
	public String getInputTitle() {
		return inputTitle;
	}

	public void setInputTitle(String newValue) {
		inputTitle = newValue;
	}

	public String getInputAuthor() {
		return inputAuthor;
	}

	public void setInputAuthor(String newValue) {
		inputAuthor = newValue;
	}

	//

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

	public boolean getDisplayReport() {
		return displayReport;
	}

	public void setDisplayReport(boolean newValue) {
		displayReport = newValue;
	}

	public boolean getHasResult() {
		return hasResult;
	}

	public void setHasResult(boolean newValue) {
		hasResult = newValue;
	}

	public boolean getHasLink() {
		return hasLink;
	}

	public void setHasLink(boolean newValue) {
		hasLink = newValue;
	}

	public String getReportErrorMsg() {
		return reportErrorMsg;
	}

	public void setReportErrorMsg(String newValue) {
		if (newValue.trim().length() > 0) {
			if (!newValue.endsWith(".")) {
				newValue += ".";
			}
		}
		reportErrorMsg = newValue;
	}

	public String getReviewResultsSQL() {
		return reviewResultsSQL;
	}

	public void setReviewResultsSQL(String newValue) {
		reviewResultsSQL = newValue;
	}

	public String getNumSPPReturned() {
		return numSPPReturned;
	}

	public void setNumSPPReturned(String newValue) {
		numSPPReturned = newValue;
	}

	public String getAdminJunk() {
		return adminJunk;
	}

	public void setAdminJunk(String newValue) {
		adminJunk = newValue;
	}

	public String getLocationJunk() {
		return locationJunk;
	}

	public void setLocationJunk(String newValue) {
		locationJunk = newValue;
	}

	public String getFsResultsTitleOne() {
		return fsResultsTitleOne;
	}

	public void setFsResultsTitleOne(String newValue) {
		fsResultsTitleOne = newValue;
	}

	public String getFsResultsTitleTwo() {
		return fsResultsTitleTwo;
	}

	public void setFsResultsTitleTwo(String newValue) {
		fsResultsTitleTwo = newValue;
	}

	public String getSpeciesResultsTitleOne() {
		return speciesResultsTitleOne;
	}

	public void setSpeciesResultsTitleOne(String newValue) {
		speciesResultsTitleOne = newValue;
	}

	public String getSpeciesResultsTitleTwo() {
		return speciesResultsTitleTwo;
	}

	public void setSpeciesResultsTitleTwo(String newValue) {
		speciesResultsTitleTwo = newValue;
	}

	public String getMapJunk() {
		return mapJunk;
	}

	public void setMapJunk(String newValue) {
		mapJunk = newValue;
	}

	public String getPlantJunk() {
		return plantJunk;
	}

	public void setPlantJunk(String newValue) {
		plantJunk = newValue;
	}

	public String getReportDescription() {
		return reportDescription;
	}

	public void setReportDescription(String newValue) {
		reportDescription = newValue;
	}

	public String getNumFSReturned() {
		return numFSReturned;
	}

	public void setNumFSReturned(String newValue) {
		numFSReturned = newValue;
	}

	public ArrayList<SPP> getSpeciesList() {
		return speciesList;
	}

	public void setSpeciesList(ArrayList<SPP> newValue) {
		speciesList = newValue;
	}

	public ArrayList<FireStudy> getFireStudyList() {
		return fireStudyList;
	}

	public void setSFireStudyList(ArrayList<FireStudy> newValue) {
		fireStudyList = newValue;
	}

	// ------ Life form ----------------------
	public boolean getByLifeForm() {
		return byLifeForm;
	}

	public void setByLifeForm(boolean newValue) {
		byLifeForm = newValue;
	}

	public String[] getPlants() {
		return plants;
	}

	public void setPlants(String[] newValue) {
		plants = newValue;
	}

	public Map<String, Object> getPlantItemsValue() {
		return plantItems;
	}

	public Map<String, Object> getAnimalItemsValue() {
		return animalItems;
	}

	public String getAnimalItemsInString() {
		return Arrays.toString(animals);
	}

	public String[] getAnimals() {
		return animals;
	}

	public void setAnimals(String[] newValue) {
		this.animals = newValue;
	}

	public boolean getLichenAll() {
		return lichenAll;
	}

	public void setLichenAll(boolean newValue) {
		lichenAll = newValue;
	}

	public boolean getFungusAll() {
		return fungusAll;
	}

	public void setFungusAll(boolean newValue) {
		fungusAll = newValue;
	}

	public boolean getPlantsAll() {
		return plantsAll;
	}

	public void setPlantsAll(boolean newValue) {
		plantsAll = newValue;
	}

	public boolean getAnimalsAll() {
		return animalsAll;
	}

	public void setAnimalsAll(boolean newValue) {
		animalsAll = newValue;
	}

	// Value change event for lifeform change on search by other page
	public void lifeFormChanged(ValueChangeEvent event) {
		// life form unselected, clear all lifeform related properties
		this.byLifeForm = (Boolean) event.getNewValue();
		if (this.byLifeForm == false) {
			if (plants != null)
				plants = null;
			if (animals != null)
				animals = null;
			setPlantsAll(false);
			setAnimalsAll(false);
			setLichenAll(false);
			setFungusAll(false);
			setByLifeForm(false);
		}
		FacesContext.getCurrentInstance().renderResponse();
	}

	// ------- End of life form ----------------------

	// ------------------- Nativity properties -------------
	public boolean getByNativity() {
		return byNativity;
	}

	public void setByNativity(boolean newValue) {
		byNativity = newValue;
	}

	public int getNativity() {
		return nativity;
	}

	public void setNativity(int newValue) {
		nativity = newValue;
	}

	public void nativityChanged(ValueChangeEvent event) {
		this.byNativity = (Boolean) event.getNewValue();
		if (this.byNativity == false) {
			setNativity(0);
			setByNativity(false);
		}
		FacesContext.getCurrentInstance().renderResponse();
	}

	public void nativityListener(AjaxBehaviorEvent event) {

	}

	// ------------- Invasiveness -----------------------------
	public boolean getByInvasiveness() {
		return byInvasiveness;
	}

	public void setByInvasiveness(boolean newValue) {
		byInvasiveness = newValue;
	}

	public int getInvasiveness() {
		return invasiveness;
	}

	public void setInvasiveness(int newValue) {
		invasiveness = newValue;
	}

	public void invasivenessChanged(ValueChangeEvent event) {
		this.byInvasiveness = (Boolean) event.getNewValue();
		if (this.byInvasiveness == false) {
			setInvasiveness(0);
			setByInvasiveness(false);
		}

		FacesContext.getCurrentInstance().renderResponse();
	}

	public void invasivenessListener(AjaxBehaviorEvent event) {

	}

	// new stuff
	public boolean getByFedStatus() {
		return byFedStatus;
	}

	public void setByFedStatus(boolean newValue) {
		byFedStatus = newValue;
	}

	public String getFedStatus() {
		return fedStatus;
	}

	public void setFedStatus(String newValue) {
		fedStatus = newValue;
	}

	public boolean getByNewStates() {
		return byNewStates;
	}

	public void setByNewStates(boolean newValue) {
		byNewStates = newValue;
	}

	public String getNewStates() {
		return newStates;
	}

	public void setNewStates(String newValue) {
		newStates = newValue;
	}

	public void fedStatusChanged(ValueChangeEvent event) {
		this.byFedStatus = (Boolean) event.getNewValue();
		if (this.byFedStatus == false) {
			setFedStatus("");
			setByFedStatus(false);
		}

		FacesContext.getCurrentInstance().renderResponse();
	}

	public void newStatesChanged(ValueChangeEvent event) {
		this.byNewStates = (Boolean) event.getNewValue();
		if (this.byNewStates == false) {
			setNewStates("");
			setByNewStates(false);
		}

		FacesContext.getCurrentInstance().renderResponse();
	}

	// end new stuff

	// --------------------------------------------------------

	// ----------------- Geographic Area --------------------------------
	// ----------------------------- GeoArea
	// ---------------------------------------------
	private boolean byGeoArea; // selectBooleanCheckBox - geoArea
	private int geoArea; // selected geoArea item value

	// new stuff
	private boolean byOtherArea;
	private int otherArea;
	//

	// new
	private boolean byNewMapLayers;
	private int newMapLayers;
	//

	private boolean byState; // User choose to search by state
	private String[] states; // selected states values (Multiple selection)

	private boolean byAdminUnit; // User choose to search by Administrative unit

	private int area;

	private String agency; // User selected agency
	private ArrayList<SelectItem> agencyItems; // selective items for agency
	private boolean agencyOn; // flag to show if any agency selected

	private ArrayList<SelectItem> regionItems; // selective items for region
												// dropdown
	private String region; // User selected region
	private boolean regionOn; // flag to show if any region selected

	private ArrayList<SelectItem> nameItems; // selective items for name
												// Dropdown
	private String[] unitNames; // user selected name

	private boolean byPnvg; // User choose to search by plant community
	private boolean byMap; // User choose to search by map point-radius
	// new map layers
	private boolean byMapAlaska;
	private boolean byMapHawaii;

	public int getArea() {
		return area;
	}

	public void setArea(int newValue) {
		area = newValue;
	}

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

	// new stuff
	public boolean getByOtherArea() {
		return byOtherArea;
	}

	public void setByOtherArea(boolean newValue) {
		byOtherArea = newValue;
	}

	public int getOtherArea() {
		return otherArea;
	}

	public void setOtherArea(int newValue) {
		otherArea = newValue;
	}

	//

	// new
	public boolean getByNewMapLayers() {
		return byNewMapLayers;
	}

	public void setByNewMapLayers(boolean newValue) {
		byNewMapLayers = newValue;
	}

	public int getNewMapLayers() {
		return newMapLayers;
	}

	public void setNewMapLayers(int newValue) {
		newMapLayers = newValue;
	}

	//

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

	private ArrayList<SelectItem> stateItems; // selective items for state
												// Dropdown

	public ArrayList<SelectItem> getStateItems() {
		return stateItems;
	}

	public void setStateItems(ArrayList<SelectItem> newValue) {
		stateItems = newValue;
	}

	// --- admin unit
	public boolean getByAdminUnit() {
		return byAdminUnit;
	}

	public void setByAdminUnit(boolean newValue) {
		byAdminUnit = newValue;
	}

	public ArrayList<SelectItem> getAgencyItems() {
		return agencyItems;
	}

	public void setAgencyItems(ArrayList<SelectItem> newValue) {
		agencyItems = newValue;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String newValue) {
		agency = newValue;
	}

	public boolean getAgencyOn() {
		return agencyOn;
	}

	public void setAgencyOn(boolean newValue) {
		agencyOn = newValue;
	}

	public ArrayList<SelectItem> getRegionItems() {
		return regionItems;
	}

	public void setRegionItems(ArrayList<SelectItem> newValue) {
		regionItems = newValue;
	}

	public boolean getRegionOn() {
		return regionOn;
	}

	public void setRegionOn(boolean newValue) {
		regionOn = newValue;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String newValue) {
		region = newValue;
	}

	public ArrayList<SelectItem> getNameItems() {
		return nameItems;
	}

	public void setNameItems(ArrayList<SelectItem> newValue) {
		nameItems = newValue;
	}

	public String[] getUnitNames() {
		return unitNames;
	}

	public void setUnitNames(String[] newValue) {
		unitNames = newValue;
	}

	// ---

	public boolean getByPnvg() {
		return byPnvg;
	}

	public void setByPnvg(boolean newValue) {
		byPnvg = newValue;
	}

	public boolean getByMap() {
		return byMap;
	}

	public void setByMap(boolean newValue) {
		byMap = newValue;
	}

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

	// GeoArea top level checkbox valuechange event
	public void geoAreaChanged(ValueChangeEvent event) {

		this.byGeoArea = (Boolean) event.getNewValue();

		if (this.byGeoArea == false) {
			this.byState = false;
			this.byAdminUnit = false;
			this.byPnvg = false;
			this.byMap = false;
			// new map layers
			this.byMapAlaska = false;
			this.byMapHawaii = false;
			//
			setByGeoArea(false);
		} else {
			setInputName("");
			setInputFsName("");

		}

		FacesContext.getCurrentInstance().renderResponse();

	}

	// new stuff
	// available links, from review to firestudy
	public String doMagic(String sppFSID) {

		 
		  
		System.out.println("doMagic ACTIVATED " + sppFSID);
		System.out.println("doMagic ACTIVATED " + sppAcronym);
		// clears the list before any results are returned. Keeps it from
		// appending results when the "available" query is run a second time.
		if (fireStudyList != null) {
			fireStudyList.clear();
		}
		//

		this.byMagic = true;

		FireStudyQueryBuilder qb = new FireStudyQueryBuilder();
		String sqlString = qb.buildMagicString(sppFSID);
		this.fireStudyResultsSQL = sqlString;
		DbAccess DMan = new DbAccess(sqlString);
		if (this.fireStudyResultsSQL != null
				&& this.fireStudyResultsSQL.length() > 0) {
			System.out.println("Did I make it here?");
			ResultSet queryResults = DMan.ExecuteQuery();
			populateFireStudyList(queryResults);
		}
		this.reportDescription = qb.getDescription();
		this.displayReport = true;

		if (fireStudyList != null && !fireStudyList.isEmpty()) {
			this.hasResult = true;
			this.numSPPReturned = Integer.toString(fireStudyList.size());
			setInputFsName("");
			setInputAuthor("");
			setInputTitle("");
			setFsResultsTitleOne("Search Results");
		} else {
			this.hasResult = false;
			this.numSPPReturned = "0";
		}
		setReportErrorMsg(DMan.getErrMessage() + qb.getErrMsg());

		// if I add this, it appends the "available" results from the first pass
		// to the second. Check out this interesting behavior.
		// Without this line, the second pass never even works
		this.byMagic = false;

		return "fireStudyResults";
	}

	// available links from firestudy to review
	public String doMoreMagic(String sppAcronym) {
		System.out.println("doMoreMagic ACTIVATED");
		if (speciesList != null) {
			speciesList.clear();
		}

		this.byMoreMagic = true;

		ReviewsQueryBuilder qb = new ReviewsQueryBuilder();
		String sqlString = qb.buildMagicString(sppAcronym);

		// qb.setInputName(this.inputName);

		// this.reviewResultsSQL = qb.buildQuery();
		this.reviewResultsSQL = sqlString;

		// this.reportDescription = qb.getDescription();

		DbAccess DMan = new DbAccess(this.reviewResultsSQL);
		if (this.reviewResultsSQL != null && this.reviewResultsSQL.length() > 0) {
			ResultSet queryResults = DMan.ExecuteQuery();
			populateSpeciesList(queryResults);
		}
		this.reportDescription = qb.getDescription();
		this.displayReport = true;

		if (speciesList != null && !speciesList.isEmpty()) {
			this.hasResult = true;
			this.numSPPReturned = Integer.toString(speciesList.size());

		} else {
			this.hasResult = false;
			setReportErrorMsg(DMan.getErrMessage() + qb.getErrMsg());
		}

		this.byMoreMagic = false;

		return "reviewResults";
	}

	// new stuff
	public void otherAreaChanged(ValueChangeEvent event) {
		this.byOtherArea = (Boolean) event.getNewValue();

		if (this.byOtherArea == false) {
			this.byState = false;
			this.byAdminUnit = false;
			this.byPnvg = false;
			this.byMap = false;
			// new map layers
			this.byMapAlaska = false;
			this.byMapHawaii = false;
			//
			setByOtherArea(false);

		} else {
			setInputName("");
			setInputFsName("");

		}

		FacesContext.getCurrentInstance().renderResponse();
	}

	public void otherAreaSelectChanged(ValueChangeEvent event) {
		area = (Integer) event.getNewValue();
		// reset choices

		this.reportErrorMsg = "";
		this.reportDescription = "";
		setInputName("");
		setInputFsName("");

		// this.byState = false;
		this.byAdminUnit = false;
		this.byPnvg = false;
		this.byMap = false;
		this.byMapAlaska = false;
		this.byMapHawaii = false;

		if (area == 2) {
			// this.byState = true;

			this.byAdminUnit = true;
			this.agencyOn = false;

			this.agencyItems = populateAgencies();
			this.regionOn = false;

			if (agency != null) {
				if (agency.compareTo("0") != 0) {
					this.regionItems = populateRegions();
					this.regionOn = true;
				}
			}
			if (region != null) {
				if (region.compareTo("0") != 0) {
					this.nameItems = populateUnitNames();
				}
			}

			this.byPnvg = false;
			this.byMap = false;
			this.byMapAlaska = false;
			this.byMapHawaii = false;
		} else if (area == 3) {
			// this.byState = true;
			this.byAdminUnit = false;

			this.byPnvg = true;
			this.pnvgRegionItems = populatePnvgRegions();
			this.pnvgRegionOn = true;
			this.vegTypeOn = false;
			this.plantCommunityOn = false;
			if (pnvgRegion != null) {
				if (pnvgRegion.compareTo("0") != 0) {
					this.vegTypeItems = populateVegTypes();
					this.vegTypeOn = true;
				}
			}
			if (vegTypeDesc != null) {
				if (vegTypeDesc.compareTo("0") != 0) {
					this.pnvgItems = populatePnvgs();
					this.plantCommunityOn = true;

				}
			}

			this.byMap = false;
			this.byMapAlaska = false;
			this.byMapHawaii = false;
		} else if (area == 4) {
			// this.byState = true;
			this.byAdminUnit = false;
			this.byPnvg = false;
			this.byMap = true;
		} else if (area == 5) {
			// do alaska map
			this.byAdminUnit = false;
			this.byPnvg = false;
			this.byMapAlaska = true;
			// this.byMap = true;
		} else if (area == 6) {
			// do hawaii map
			this.byAdminUnit = false;
			this.byPnvg = false;
			// this.byMap = true;
			this.byMapHawaii = true;
		} else {
			// this.byState = false;
			this.byAdminUnit = false;
			this.byPnvg = false;
			this.byMap = false;
			this.byMapAlaska = false;
			this.byMapHawaii = false;
		}
		FacesContext.getCurrentInstance().renderResponse();
	}

	// end new stuff

	// geo area dropdown value change event
	public void geoAreaSelectChanged(ValueChangeEvent event) {
		// boolean area = true;//(Integer)event.getNewValue();
		// reset choices

		this.byNewStates = (Boolean) event.getNewValue();

		System.out.println("This is byNewStates: " + byNewStates);

		this.reportErrorMsg = "";
		this.reportDescription = "";
		setInputName("");
		setInputFsName("");

		// this.byState = true;
		// this.byNewStates = true;

		// 12-18-2013 Test, removing these to see if I can fix the error where
		// this stuff dissappears once a state is selected.
		// this.byAdminUnit = false;
		// this.byPnvg =false;
		// this.byMap = false;
		// end test

		// if (area == true)
		if (this.byNewStates == true) {
			this.byState = true;

			// 12-18-2013 Test, removing these to see if I can fix the error
			// where
			// this stuff dissappears once a state is selected.
			// this.byAdminUnit = false;
			// this.byPnvg =false;
			// this.byMap = false;
			// end test

			this.stateItems = populateStates();
		} else {
			this.byState = false;

			// clear states if byNewStates (state checkbox) not checked
			states = null;

			// 12-18-2013 Test, removing these to see if I can fix the error
			// where
			// this stuff dissappears once a state is selected.
			// this.byAdminUnit = false;
			// this.byPnvg =false;
			// this.byMap = false;
			// end test
		}
		FacesContext.getCurrentInstance().renderResponse();
	}

	// agency dropdown menu value change event
	public void agencySelectChanged(ValueChangeEvent event) {
		String selAgency = (String) event.getNewValue();

		if (selAgency.compareTo("0") != 0) {
			this.agency = selAgency;
			this.regionItems = populateRegions();
			this.regionOn = true;
			this.region = "0";
			this.nameItems = populateUnitNames();
		} else {
			this.agencyOn = false;
			this.regionOn = false;
		}
		FacesContext.getCurrentInstance().renderResponse();
	}

	// region dropdown menu value change event
	public void regionSelectChanged(ValueChangeEvent event) {
		String selRegion = (String) event.getNewValue();
		setReportErrorMsg("");
		if (selRegion.compareTo("0") != 0) {
			this.region = selRegion;
			this.nameItems = populateUnitNames();
			this.agencyOn = true;
			setReportErrorMsg("");
		} else {
			this.region = "0";
			this.agencyOn = false;
		}

		// region of 0 will populate Names based on agency
		// this.nameItems = populateNames();
		this.regionOn = true;

		FacesContext.getCurrentInstance().renderResponse();
	}

	private ArrayList<SelectItem> populateAgencies() {
		ArrayList<SelectItem> agencyList = new ArrayList<SelectItem>();

		SelectItem firstItem = new SelectItem("0", "Select an agency ", "",
				false, true);
		agencyList.add(firstItem);
		AdminUnitSearch auAgencies = new AdminUnitSearch();
		ResultSet results = auAgencies.getAgencies();

		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {
						String regionId = results.getString("AGENCY_ABB");
						SelectItem si = new SelectItem(regionId,
								results.getString("AGENCY"));
						agencyList.add(si);

					} catch (SQLException e) {

						e.printStackTrace();
					}
				}

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return agencyList;
	}

	private ArrayList<SelectItem> populateRegions() {
		String regionLabel = "Select a region ";
		ArrayList<SelectItem> regionList = new ArrayList<SelectItem>();

		AdminUnitSearch auRegions = new AdminUnitSearch();
		ResultSet results = auRegions.getRegions(this.agency);
		if (results != null) {
			SelectItem firstItem = new SelectItem("0", regionLabel, "", false,
					true);
			regionList.add(firstItem);

			try {
				results.beforeFirst();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						SelectItem si = new SelectItem(
								results.getString("REGION_ID"),
								results.getString("REGION_DESC"));
						regionList.add(si);

					} catch (SQLException e) {

						e.printStackTrace();
					}
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		} else {
			SelectItem firstItem = new SelectItem("0",
					"Selected Agency does not have regions", "", false, true);
			regionList.add(firstItem);
		}

		return regionList;
	}

	private ArrayList<SelectItem> populateUnitNames() {
		String nameLabel = "Select a unit name ";
		ArrayList<SelectItem> nameList = new ArrayList<SelectItem>();

		SelectItem firstItem = new SelectItem("0", nameLabel, "", false, true);
		nameList.add(firstItem);
		AdminUnitSearch auRegions = new AdminUnitSearch();
		ResultSet results = auRegions.getUnitNames(this.agency, this.region);

		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						SelectItem si = new SelectItem(
								results.getString("ADMIN_ID"),
								results.getString("NAME1"));
						nameList.add(si);

					} catch (SQLException e) {

						e.printStackTrace();
					}

				}

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return nameList;
	}

	// Plant Community Drop Down 1 - pnvg agency dropdown menu value change
	// event
	public void pnvgRegionSelectChanged(ValueChangeEvent event) {
		String selRegion = (String) event.getNewValue();
		if (selRegion != "0") {
			this.pnvgRegion = selRegion;

			this.vegTypeItems = populateVegTypes();
			if (this.vegTypeOn) {
				this.pnvgItems = populatePnvgs();
				this.plantCommunityOn = true;
			}
			setReportErrorMsg("Select region and formation.");

			this.pnvgRegionOn = true;
		} else {
			this.pnvgRegionOn = false;
			this.vegTypeOn = false;
			this.plantCommunityOn = false;
		}
		FacesContext.getCurrentInstance().renderResponse();
	}

	// Veg type dropdown menu value change event
	public void vegTypeSelectChanged(ValueChangeEvent event) {
		String selVegType = (String) event.getNewValue();

		if (selVegType != "0") {
			this.vegTypeDesc = selVegType;
			this.pnvgItems = populatePnvgs();
			this.vegTypeOn = true;
			this.plantCommunityOn = true;
		} else {
			this.vegTypeDesc = "0";
			this.plantCommunityOn = false;
		}
		FacesContext.getCurrentInstance().renderResponse();
	}

	private ArrayList<SelectItem> populatePnvgRegions() { // First dropdown for
															// Search by Plant
															// Communities
		ArrayList<SelectItem> agencyList = new ArrayList<SelectItem>();

		SelectItem firstItem = new SelectItem("0", "Select a region ", "",
				false, true);
		agencyList.add(firstItem);
		PNVGSearch pnvgRegions = new PNVGSearch();
		ResultSet results = pnvgRegions.getAllRegions();

		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {
						String regionId = results.getString("BPSREGION_ID");
						SelectItem si = new SelectItem(regionId,
								results.getString("BPSREGION"));
						agencyList.add(si);

					} catch (SQLException e) {

						e.printStackTrace();
					}

				}

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return agencyList;
	}

	private ArrayList<SelectItem> populateVegTypes() {
		// dropdown list 2(formation) for search by Plant Communities
		String listLabel = "Select a formation ";

		ArrayList<SelectItem> vegTypeList = new ArrayList<SelectItem>();

		SelectItem firstItem = new SelectItem("0", listLabel, "", false, true);
		vegTypeList.add(firstItem);
		PNVGSearch vegtypes = new PNVGSearch();
		ResultSet results = vegtypes.getAllVegType();
		int noItems = 1;
		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						// SelectItem si = new
						// SelectItem(results.getString("VEGTYPE_ID"),
						// results.getString("VEGTYPE"));
						// SelectItem si = new
						// SelectItem(results.getString("VEGTYPE"),
						// results.getString("VEGTYPE"));
						SelectItem si = new SelectItem(
								results.getString("FORMATION_ID"),
								results.getString("FORMATION"));
						vegTypeList.add(si);

					} catch (SQLException e) {

						e.printStackTrace();
					}
					noItems++;
				}

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return vegTypeList;
	}

	private ArrayList<SelectItem> populatePnvgs() {
		// drop down list 3 search by Plant Communities
		String nameLabel = "Select a plant community";

		ArrayList<SelectItem> pnvgList = new ArrayList<SelectItem>();
		items = new LinkedHashMap<String, String>();

		SelectItem firstItem = new SelectItem("0", nameLabel, "", false, true);
		pnvgList.add(firstItem);
		PNVGSearch pnvgData = new PNVGSearch();

		ResultSet results = pnvgData.getMatchingPNVG(this.pnvgRegion,
				this.vegTypeDesc);

		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						// SelectItem si = new
						// SelectItem(results.getString("PNVG_CODE"),
						// results.getString("PNVG_DESC"));
						SelectItem si = new SelectItem(
								results.getString("BPS_CODE"),
								results.getString("DISPLAY"));
						pnvgList.add(si);
						// items.put(results.getString("PNVG_CODE"),
						// results.getString("PNVG_DESC"));
						items.put(results.getString("BPS_CODE"),
								results.getString("DISPLAY"));
					} catch (SQLException e) {

						e.printStackTrace();
					}

				}

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return pnvgList;
	}

	// ----------------------- End Admin
	// units-----------------------------------------------

	// ------------------------ PNVG
	// --------------------------------------------------------
	private String pnvgRegion = ""; // User selected agency
	private ArrayList<SelectItem> pnvgRegionItems; // selective items for agency
	private boolean pnvgRegionOn; // flag to show if any agency selected
	private boolean plantCommunityOn;

	private ArrayList<SelectItem> vegTypeItems; // selective items for region
												// dropdown
	private int vegType; // User selected region
	private boolean vegTypeOn; // flag to show if any region selected
	private String vegTypeDesc; // needed to get pnvg selections

	private ArrayList<SelectItem> pnvgItems; // selective items for pnvg
												// Dropdown
	private String[] pnvgs; // user selected pnvg based on vegtype

	/* Adding this part in to search the map function using bps instead of pnvg */
	private String[] bps;
	private String fireRegimeResultsSQL;

	/* End */

	public ArrayList<SelectItem> getPnvgRegionItems() {
		return pnvgRegionItems;
	}

	public void setPnvgRegionItems(ArrayList<SelectItem> newValue) {
		pnvgRegionItems = newValue;
	}

	public String getPnvgRegion() {
		return pnvgRegion;
	}

	public void setPnvgRegion(String newValue) {
		pnvgRegion = newValue;
	}

	public boolean getPnvgRegionOn() {
		return pnvgRegionOn;
	}

	public void setpnvgRegionOn(boolean newValue) {
		pnvgRegionOn = newValue;
	}

	public boolean getPlantCommunityOn() {
		return plantCommunityOn;
	}

	public void setPlantCommunityOn(boolean newValue) {
		plantCommunityOn = newValue;
	}

	public ArrayList<SelectItem> getVegTypeItems() {
		return vegTypeItems;
	}

	public void setVegTypeItems(ArrayList<SelectItem> newValue) {
		vegTypeItems = newValue;
	}

	public boolean getVegTypeOn() {
		return vegTypeOn;
	}

	public void setVegTypeOn(boolean newValue) {
		vegTypeOn = newValue;
	}

	public int getVegType() {
		return vegType;
	}

	public void setVegType(int newValue) {
		vegType = newValue;
	}

	public String getVegTypeDesc() {
		return vegTypeDesc;
	}

	public void setVegTypeDesc(String newValue) {
		vegTypeDesc = newValue;
	}

	public ArrayList<SelectItem> getPnvgItems() {
		return pnvgItems;
	}

	public void setPnvgItems(ArrayList<SelectItem> newValue) {
		pnvgItems = newValue;
	}

	public String[] getPnvgs() {
		return pnvgs;
	}

	public void setPnvgs(String[] newValue) {
		pnvgs = newValue;
	}

	/* Adding this part in to search the map function using bps instead of pnvg */
	public String[] getBps() {
		return bps;
	}

	public void setBps(String[] newValue) {
		pnvgs = newValue;
	}

	// -------------------------END of PNVG
	// --------------------------------------------------

	private ArrayList<SelectItem> populateStates() {
		ArrayList<SelectItem> stateList = new ArrayList<SelectItem>();

		SelectItem firstItem = new SelectItem("0", "No selection ", "", false,
				true);
		stateList.add(firstItem);
		GeoData geoDataHelper = new GeoData();
		ResultSet results = geoDataHelper.getStates();

		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						SelectItem si = new SelectItem(
								results.getString("ABBR"),
								results.getString("STATE_NAME"));
						stateList.add(si);

					} catch (SQLException e) {

						e.printStackTrace();
					}
				}

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return stateList;
	}

	// ---------------- END GeoArea
	// -------------------------------------------------------

	public void exportTable(String id) {
		Exporter tableExport = new Exporter();
		if (this.reviewResultsSQL.length() > 0) {
			DbAccess DMan = new DbAccess(this.reviewResultsSQL);
			ResultSet queryResults = DMan.ExecuteQuery();
			tableExport.setDescription(this.reportDescription);
			StringBuffer sb = tableExport.populateReviewList(queryResults);
			tableExport.exportTable(sb, "reviews.csv");
		}
	}

	public void exportFSTable(String id) {
		Exporter tableExport = new Exporter();

		if (this.fireStudyResultsSQL.length() > 0) {
			DbAccess DMan = new DbAccess(this.fireStudyResultsSQL);
			ResultSet queryResults = DMan.ExecuteQuery();
			tableExport.setDescription(this.reportDescription);

			StringBuffer sb = tableExport.populateFireStudyList(queryResults);

			tableExport.exportTable(sb, "fire_studies.csv");
		}
	}

	public String gotoFireStudySearchAction() {
		clearSearch();
		return "searchByFireStudy";
	}

	public String gotoSearchAction() {

		clearSearch();
		if (this.byName)
			return "searchByName";
		else
			return "searchByOther";
	}

	public String gotoRevise() {
		this.byAdminUnit = false;
		getInputName();
		setInputName(inputName);
		// System.out.println("This is the inputName: " + inputName);

		getByLifeForm();
		// System.out.println("This is the lifeform: " + byLifeForm);

		getArea();
		System.out.println("This is the area: " + area);

		getByNativity();
		// System.out.println("This is the Nativity: " + nativity);

		getByInvasiveness();
		// System.out.println("This is the invasiveness: " + invasiveness);

		getFedStatus();
		// System.out.println("This is the fedstatus: " + fedStatus);

		getByAdminUnit();
		// System.out.println("This is the AdminUnit: " + byAdminUnit);

		getPlants();
		// System.out.println("This is the AdminUnit: " + byAdminUnit);

		getAgency();
		System.out.println("This is the agency: " + agency);

		getPnvgRegion();
		System.out.println("This is the pnvg: " + pnvgRegion);

		getByAdminUnit();
		System.out.println("This is the AdminUnit: " + byAdminUnit);

		getStates();
		System.out.println("This is the states: " + states);

		getInputLatitude();
		// System.out.println("This is the input latitude: " + inputLatitude);

		getGeoRadius();
		System.out.println("This is the geoRadius: " + geoRadius);

		getPnvgRegionOn();
		System.out.println("This is the PnvgRegionOn: " + pnvgRegionOn);

		if (plants != null) {
			this.byLifeForm = true;
		}
		if (nativity != 0) {
			this.byNativity = true;
		}
		if (invasiveness != 0) {
			this.byInvasiveness = true;
		}
		if (fedStatus != "") {
			this.byFedStatus = true;
		}
		if (states != null) {
			this.byState = true;
			this.byNewStates = true;
		}

		// The Hard Ones
		// if (agency != null && agency != "0")
		if (area == 2) {
			this.byAdminUnit = true;
		}

		// else if (pnvgRegion.length() > 0)
		if (area == 3) {
			this.byPnvg = true;
		}

		// forget about the map, its not worth it.
		if (area == 4) {
			this.byMap = true;
		}

		if (area == 5) {
			this.byMapAlaska = true;
		}

		if (area == 6) {
			this.byMapHawaii = true;
		}

		// return a page
		if (this.byName)
			return "searchByName";
		else
			return "searchByOther";
	}

	public String gotoReviseFS() {
		// not working atm...only works when there are no results....
		getInputFsName();
		getInputAuthor();
		getInputTitle();

		setInputFsName(inputFSName);
		setInputAuthor(inputAuthor);
		setInputTitle(inputTitle);

		getStates();
		getAgency();
		getPnvgRegionOn();

		getArea();
		System.out.println("This is the area: " + area);

		if (states != null) {
			this.byState = true;
			this.byNewStates = true;
		}

		// if (agency != null && agency != "0")
		if (area == 2) {
			this.byAdminUnit = true;
		}

		// else if (pnvgRegion.length() > 0)
		if (area == 3) {
			this.byPnvg = true;
		}

		if (area == 4) {
			this.byMap = true;
		}

		if (area == 5) {
			this.byMapAlaska = true;
		}

		if (area == 6) {
			this.byMapHawaii = true;
		}

		return "searchByFireStudy";
	}

	public String fireStudySearchAction() {
		// this.inputName = "";
		this.inputFSName = "";
		// New Stuff
		this.inputAuthor = "";
		this.inputTitle = "";
		// ////////////////////////////////////////////////////
		clearSearch();
		return "searchByFireStudy";
	}

	// Can I use this to make a "revise button"
	public String reviewSearchAction() {
		this.inputName = "";
		this.inputFSName = "";

		// New Stuff
		this.inputAuthor = "";
		this.inputTitle = "";
		// ////////////////////////////////////////////////////
		clearSearch();
		return "searchByOther";
	}

	// Action event handles search by name command button event on search by
	// name page
	@SuppressWarnings("unused")
	public String searchByName() {
		// check if name is empty or null
		this.numFSReturned = "";
		this.reportDescription = "";
		this.reviewResultsSQL = "";
		String queryInputName = "";
		this.numSPPReturned = "0";

		setReportErrorMsg("");
		this.byName = true;

		if (speciesList != null)
			speciesList.clear();

		if ("".equals(inputName) || inputName == null)
			return "";
		else {
			queryInputName = checkQueryString(inputName);
			String SQLStr = "SELECT DISTINCT "
					+ "\"REVIEWS\".\"REVIEW_ID\", "
					+ "\"SPECIES\".\"COMMONNAME\", "
					+ "\"SPECIES\".\"SCINAME\", "
					+ "\"SPECIES\".\"SPP_ID\", "
					+ "\"REVIEWS\".\"ACRONYM\", "
					+ "\"REVIEWS\".\"HTML_PATH\", "
					+ "\"REVIEWS\".\"YEAR_WRITTEN\", "
					+ "\"REVIEWS\".\"PDF_PATH\", "
					+ "\"SPECIES\".\"FEIS_SPP\" FROM \"NAMES\", \"REVIEWS\", \"SPECIES\" WHERE "
					+ "\"NAMES\".\"SPP_ID\" = \"SPECIES\".\"SPP_ID\" AND "
					+ "\"SPECIES\".\"REVIEW_ID\" = \"REVIEWS\".\"REVIEW_ID\" AND "
					+ "\"NAME\" ~* '"
					+ queryInputName
					+ "' ORDER BY  \"REVIEWS\".\"ACRONYM\",\"SPECIES\".\"SCINAME\";";
			this.reviewResultsSQL = SQLStr;
			DbAccess dbUtil = new DbAccess(SQLStr);
			ResultSet rs = dbUtil.ExecuteQuery();

			populateSpeciesList(rs);
			this.reportDescription = "Name = " + inputName;

			this.displayReport = true;
			if (speciesList != null && !speciesList.isEmpty()) {
				this.hasResult = true;
				this.numSPPReturned = Integer.toString(speciesList.size());
				setInputName("");
				setInputFsName("");
			} else {
				setReportErrorMsg(dbUtil.getErrMessage());
				this.hasResult = false;
			}

		}

		outCome = "reviewResults";
		return outCome;
	}

	public void clearSearch() {
		setInputName("");
		setInputFsName("");
		setFireRegimeName("");
		setFireRegimeTitle("");
		setInputAuthor("");
		setInputTitle("");

		this.byState = false;
		this.byNewStates = false;
		this.byAdminUnit = false;
		this.agency = "0";
		this.region = "0";
 
		area = 0;
  
		this.byPnvg = false;
		this.pnvgRegion = "";
		this.vegTypeDesc = "";
		if (this.states != null)
			this.states = null;

		this.byMap = false;
		this.byMapAlaska = false;
		this.byMapHawaii = false;
		setInvasiveness(0);
		// new stuff
		setFedStatus("");
		setNewStates("");
		setAdminJunk("");
		setPlantJunk("");
		setMapJunk("");
		setLocationJunk("");
		//
		this.byLifeForm = false;
		if (plants != null)
			plants = null;
		if (animals != null)
			animals = null;
		setPlantsAll(false);
		setAnimalsAll(false);
		setLichenAll(false);
		setFungusAll(false);
		setGeoArea(0);
		// new stuff
		setOtherArea(0);
		//
		if (this.unitNames != null)
			this.unitNames = null;
		if (this.pnvgs != null)
			this.pnvgs = null;

		this.byNativity = false;
		setNativity(0);
		this.byInvasiveness = false;
		// new stuff
		this.byFedStatus = false;
		this.byNewStates = false;
		//
		this.byGeoArea = false;
		this.geoArea = 0;
		// new stuff
		this.byOtherArea = false;
		this.otherArea = 0;
		//
		// new
		this.byNewMapLayers = false;
		this.newMapLayers = 0;
		// end
		this.byName = false;
		setReportErrorMsg("");
		this.reportDescription = "";
		this.reviewResultsSQL = "";
		this.fireStudyResultsSQL = "";
		this.magicString = "";

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context
				.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();

	}

	// clear everything on home search
	public void clearHomeSearch() {

		setInputAuthor("");
		setInputTitle("");
		// setInputName("");
		// setInputFsName("");
		this.byState = false;
		this.byNewStates = false;
		this.byAdminUnit = false;
		this.agency = "0";
		this.region = "0";

		// exp 12/31/13
		area = 0;
		// end exp

		this.byPnvg = false;
		this.pnvgRegion = "";
		this.vegTypeDesc = "";
		if (this.states != null)
			this.states = null;

		this.byMap = false;
		this.byMapAlaska = false;
		this.byMapHawaii = false;
		setInvasiveness(0);
		// new stuff
		setFedStatus("");
		setNewStates("");
		setAdminJunk("");
		setPlantJunk("");
		setMapJunk("");
		setLocationJunk("");
		//
		this.byLifeForm = false;
		if (plants != null)
			plants = null;
		if (animals != null)
			animals = null;
		setPlantsAll(false);
		setAnimalsAll(false);
		setLichenAll(false);
		setFungusAll(false);
		setGeoArea(0);
		// new stuff
		setOtherArea(0);
		//
		if (this.unitNames != null)
			this.unitNames = null;
		if (this.pnvgs != null)
			this.pnvgs = null;

		this.byNativity = false;
		setNativity(0);
		this.byInvasiveness = false;
		// new stuff
		this.byFedStatus = false;
		this.byNewStates = false;
		//
		this.byGeoArea = false;
		this.geoArea = 0;
		// new stuff
		this.byOtherArea = false;
		this.otherArea = 0;
		//
		// new
		this.byNewMapLayers = false;
		this.newMapLayers = 0;
		// end new map layers
		// this.byName = false;
		setReportErrorMsg("");
		this.reportDescription = "";
		this.reviewResultsSQL = "";
		this.fireStudyResultsSQL = "";
		this.magicString = "";

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context
				.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();

	}

	public String findAll() {
		inputName = "";
		return searchByOtherHome();
	}

	// Action event handles search by other command button event on search by
	// other page
	public String searchByOtherHome() {
		Logger.getLogger(getClass().getName())
				.info("BEGIN : searchByOtherHome");
		setSpeciesResultsTitleOne("I'm not Empty!");
		setSpeciesResultsTitleTwo("");

		// keeps these guys from showing up where they aren't supposed to
		clearHomeSearch();
		setAdminJunk("");
		setLocationJunk("");
		setMapJunk("");
		setPlantJunk("");
		//

		if (speciesList != null)
			speciesList.clear();

		this.byName = false;
		this.reviewResultsSQL = "";
		this.reportDescription = "";
		setReportErrorMsg("");
		this.displayReport = true; // description and error message
		this.numSPPReturned = "0";

		ReviewsQueryBuilder qb = new ReviewsQueryBuilder();
		qb.setInputName(this.inputName);

		this.reviewResultsSQL = qb.buildQuery();
		this.reportDescription = qb.getDescription();

		DbAccess DMan = new DbAccess(this.reviewResultsSQL);
		if (this.reviewResultsSQL != null && this.reviewResultsSQL.length() > 0) {
			ResultSet queryResults = DMan.ExecuteQuery();
			populateSpeciesList(queryResults);
		}
		if (speciesList != null && !speciesList.isEmpty()) {
			this.hasResult = true;
			this.numSPPReturned = Integer.toString(speciesList.size());

		} else {
			this.hasResult = false;
			setReportErrorMsg(DMan.getErrMessage() + qb.getErrMsg());
		}

		this.byLifeForm = false;
		this.byNativity = false;
		this.byInvasiveness = false;
		this.byFedStatus = false;
		this.byNewStates = false;
		this.byState = false;
		this.byAdminUnit = false;
		this.byMap = false;
		this.byMapAlaska = false;
		this.byMapHawaii = false;
		this.byPnvg = false;
		//

		return "reviewResults";
	}

	public String searchByOther() {
		setSpeciesResultsTitleOne("");
		setSpeciesResultsTitleTwo("I'm not Empty!");

		// keeps these guys from showing up where they aren't supposed to
		setAdminJunk("");
		setLocationJunk("");
		setMapJunk("");
		setPlantJunk("");
		//

		if (speciesList != null)
			speciesList.clear();

		this.byName = false;
		this.reviewResultsSQL = "";
		this.reportDescription = "";
		setReportErrorMsg("");
		this.displayReport = true; // description and error message
		this.numSPPReturned = "0";

		ReviewsQueryBuilder qb = new ReviewsQueryBuilder();
		// new stuff
		qb.setInputName(this.inputName);
		// set to true for the reviews advanced search?
		//

		// if (this.byNewStates)
		// {
		// qb.setByState(this.byState);
		// qb.setStates(this.states);
		// }

		qb.setByAdminUnit(this.byAdminUnit);
		if (this.byAdminUnit) {
			qb.setUnitNames(this.unitNames);
			qb.setAgency(this.agency);
			qb.setRegion(this.region);
			if (!validateAdminUnit()) {
				this.hasResult = false;

				return null;
			}
			// experimental
			setAdminJunk(ADMINISTRATIVE_UNIT_MSG);

		}
		qb.setByInvasiveness(this.byInvasiveness);
		// new stuff
		qb.setByFedStatus(this.byFedStatus);
		qb.setByNewStates(this.byNewStates);
		//
		qb.setByNativity(this.byNativity);
		qb.setNativity(nativity);
		qb.setInvasiveness(invasiveness);
		// new stuff
		qb.setFedStatus(fedStatus);
		qb.setNewStates(newStates);
		//

		qb.setByLifeForm(this.byLifeForm);
		if (plants != null)
			qb.setLifeformAnimals(Arrays.asList(plants));
		if (animals != null)
			qb.setLifeformPlants(Arrays.asList(animals));
		qb.setAnimalsAll(this.animalsAll);
		qb.setPlantsAll(this.plantsAll);
		qb.setFungusAll(this.fungusAll);
		qb.setLichenAll(this.lichenAll);

		qb.setByState(this.byState);
		qb.setStates(this.states);
		// experimental
		if (this.byState) {
			setLocationJunk("State, province, and country searches are based on species distribution data obtained mostly from the ");
		}
		// end experimental
		qb.setByPnvg(this.byPnvg);
		if (this.byPnvg) {
			if (!validatePlantCommunity()) {
				this.hasResult = false;
				return null;
			}

			qb.setPnvgRegion(this.pnvgRegion);
			qb.setVegTypeDesc(this.vegTypeDesc);
			String selected_codes = "";

			// determine if we need to retrieve all pnvgs
			PNVGSearch allPnvgs = new PNVGSearch();
			boolean allSelections = false;
			if (this.pnvgs == null || this.pnvgs.length == 0
					|| "".equals(this.pnvgs[0])) {
				selected_codes = allPnvgs.getCSVPnvgs(this.pnvgRegion,
						this.vegTypeDesc);
				allSelections = true;
			} else {
				selected_codes = pnvgsToCSVString(this.pnvgs);
			}

			if (selected_codes != null && (selected_codes.compareTo("0") == 0)
					|| selected_codes.compareTo("") == 0) {
				selected_codes = allPnvgs.getCSVPnvgs(this.pnvgRegion,
						this.vegTypeDesc);
				allSelections = true;
			}

			System.out
					.println("This is the selected codes from plant community : "
							+ selected_codes);
			if ("".equals(selected_codes.trim())) {

				setReportErrorMsg("There are no plant communities for the selected formation. Please try again.");
				this.hasResult = false;
				return null;
			}

			String bps_codes = getBpsCodeFinal(selected_codes);
			System.out
					.println("This is the new BPS codes (technically bps ID for queries) from plant community : "
							+ bps_codes);
			qb.setBps(bps_codes);
			// end new

			if (!allSelections) {
				String plantCommunitySelections = getSelectedPlantCommunities();
				qb.setPlantCommunities(plantCommunitySelections);
			} else
				qb.setPlantCommunities("All");
			// experimental
			setPlantJunk(PLANT_COMMUNITY_MSG);
		}
		qb.setByMap(this.byMap);
		// new map layer
		qb.setByMapAlaska(this.byMapAlaska);
		qb.setByMapHawaii(this.byMapHawaii);
		//
		qb.setInputLatitude(this.inputLatitude);
		qb.setInputLongitude(this.inputLongitude);
		qb.setGeoRadius(this.geoRadius);
		double radius = .7;
		if (this.byMap) {
			System.out.println("This is the area " + area);
			if (area == 4) {
				System.out.println("you made it to byMap!");
				System.out.println("this is the lat " + inputLatitude);
				System.out.println("this is the long " + inputLongitude);

				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}

				String bps_codes = getBpsCode(inputLatitude, inputLongitude,
						radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				System.out.println("This is the BPS 'pt' and 'radius': "
						+ bps_codes); // these are actually bps ID's being
										// returned, which is technically all we
										// need to build the final query and
										// return some results
				System.out.println("This is the BPS ID/s: " + bps_codes_final);

				qb.setBps(bps_codes_final);

				// experimental
				setMapJunk(MAP_SEARCHES_MSG);

				// test

			}
		}
		if (this.byMapAlaska) {
			System.out.println("you made it to byMapAlaska!");

			// debugging
			// so far lat and long are not being passed!
			System.out.println("this is the lat" + inputLatitude);
			System.out.println("this is the long" + inputLongitude);
			if (!validateLatLong(inputLatitude, inputLongitude)) {
				setReportErrorMsg("Select a map location.");
				return "";
			}

			String bps_codes = getBpsCodeAlaska(inputLatitude, inputLongitude,
					radius);
			String bps_codes_final = getBpsCodeFinal(bps_codes);
			/* End */
			// 1/14/2014
			System.out.println("This is the BPS 'pt' and 'radius': "
					+ bps_codes);
			System.out.println("This is the BPS code/s: " + bps_codes_final);
			// end 1/14/2014

			// qb.setPnvgs(veg_codes);

			qb.setBps(bps_codes_final);

			// experimental
			setMapJunk(MAP_SEARCHES_MSG);

		}
		if (this.byMapHawaii) {
			System.out.println("you made it to byMapHawaii! " + geoRadius);
			if (!validateLatLong(inputLatitude, inputLongitude)) {
				setReportErrorMsg("Select a map location.");
				return "";
			}
			String bps_codes = getBpsCodeHawaii(inputLatitude, inputLongitude,
					radius);
			String bps_codes_final = getBpsCodeFinal(bps_codes);
			/* End */
			// 1/14/2014
			System.out.println("This is the BPS 'pt' and 'radius': "
					+ bps_codes);
			System.out.println("This is the BPS code/s: " + bps_codes_final);
			// end 1/14/2014

			// qb.setPnvgs(veg_codes);

			qb.setBps(bps_codes_final);

			// experimental
			setMapJunk(MAP_SEARCHES_MSG);

		}
		this.reviewResultsSQL = qb.buildQuery();
		this.reportDescription = qb.getDescription();

		DbAccess DMan = new DbAccess(this.reviewResultsSQL);
		if (this.reviewResultsSQL != null && this.reviewResultsSQL.length() > 0) {
			ResultSet queryResults = DMan.ExecuteQuery();
			populateSpeciesList(queryResults);
		}
		if (speciesList != null && !speciesList.isEmpty()) {
			this.hasResult = true;
			this.numSPPReturned = Integer.toString(speciesList.size());

		} else {
			this.hasResult = false;
			setReportErrorMsg(DMan.getErrMessage() + qb.getErrMsg());
		}

		// This is new, should keep the simple searches from taking properties
		// from the advanced searches.
		// I should also put this in BEFORE the function starts in order to
		// remove specific output from the setXXXJunk functions in the results
		// page, but it would like screw up the revised search function.
		this.byLifeForm = false;
		this.byNativity = false;
		this.byInvasiveness = false;
		this.byFedStatus = false;
		this.byNewStates = false;
		this.byState = false;
		this.byAdminUnit = false;
		this.byMap = false;
		this.byMapAlaska = false;
		this.byMapHawaii = false;
		this.byPnvg = false;
		//

		return "reviewResults";
	}

	private boolean validateLatLong(String lat, String longi) {

		boolean ret = false;
		try {
			Double.parseDouble(lat);
			Double.parseDouble(longi);
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	private boolean isBlank(String str) {
		return str == null ? true : str.trim().length() == 0;
	}

	public String searchByFSAll() {
		inputFSName = "";
		return searchByFSHome();
	}

	
	public String searchFireStudiesByFireRegime(long frId) {
		logger.info("searchFireStudiesByFireRegime " + frId);
		clearSearch();
		FireStudyQueryBuilder qb = new FireStudyQueryBuilder();
		qb.setFireRegimeId(frId);
		fireStudyResultsSQL = qb.buildFireRegimeQuery();
		reportDescription = qb.getDescription();
		DbAccess DMan = new DbAccess(this.fireStudyResultsSQL);
		setReportErrorMsg(DMan.getErrMessage());
		ResultSet queryResults = DMan.ExecuteQuery();
		populateFireStudyList(queryResults);
		this.numSPPReturned = Integer.toString(fireStudyList.size());
		displayReport = true;
		hasResult = true; 
		return "FireStudyResults";
	}
	
	public String searchReviewsByFireRegime(long frId) {
		logger.info("searchReviewsByFireRegime " + frId);
		clearSearch();
		ReviewsQueryBuilder qb = new ReviewsQueryBuilder();
		qb.setFireRegimeId(frId);
		reviewResultsSQL = qb.buildFireRegimeQuery();
		reportDescription = qb.getDescription();
		DbAccess DMan = new DbAccess(reviewResultsSQL);
		setReportErrorMsg(DMan.getErrMessage());
		ResultSet queryResults = DMan.ExecuteQuery();
		populateSpeciesList(queryResults);

		if (speciesList != null && !speciesList.isEmpty()) {
			hasResult = true;
			numSPPReturned = Integer.toString(speciesList.size());
	
		} else {
			hasResult = false;
			setReportErrorMsg(DMan.getErrMessage() + qb.getErrMsg());
		}
		return "ReviewResults";
	}
	
	public String searchByFSHome() {
		// Testing alternate RESULTS page title
		setFsResultsTitleOne("I'm not Empty!");
		setFsResultsTitleTwo("");
		// End testing alternate RESULTS page title

		// keeps these guys from showing up where they aren't supposed to
		clearHomeSearch();

		setAdminJunk("");
		setLocationJunk("");
		setMapJunk("");
		setPlantJunk("");
		//

		if (fireStudyList != null) {
			fireStudyList.clear();
		}
		this.fireStudyResultsSQL = "";
		this.reportDescription = "";
		this.byName = false;
		this.numSPPReturned = "0";
		setReportErrorMsg("");

		FireStudyQueryBuilder qb = new FireStudyQueryBuilder();
		if (isBlank(this.inputFSName)) {
			qb.setByName(false);

			this.reportDescription = qb.getDescription();

			String sqlString = qb.buildGeoQuery();
			this.fireStudyResultsSQL = sqlString;
			DbAccess DMan = new DbAccess(sqlString);
			if (this.fireStudyResultsSQL != null
					&& this.fireStudyResultsSQL.length() > 0) {
				ResultSet queryResults = DMan.ExecuteQuery();
				populateFireStudyList(queryResults);
			}

			setReportErrorMsg(DMan.getErrMessage() + qb.getErrMsg());

			this.byState = false;
			this.byAdminUnit = false;
			this.byMap = false;
			this.byMapAlaska = false;
			this.byMapHawaii = false;
			this.byPnvg = false;
		} else {
			qb.setByName(true);
			qb.setInputFSName(this.inputFSName);

			this.fireStudyResultsSQL = qb.buildHomeNameQuery();

			DbAccess DMan = new DbAccess(this.fireStudyResultsSQL);
			setReportErrorMsg(DMan.getErrMessage());
			ResultSet queryResults = DMan.ExecuteQuery();
			populateFireStudyList(queryResults);
		}

		this.reportDescription = qb.getDescription();
		this.displayReport = true;

		if (fireStudyList != null && !fireStudyList.isEmpty()) {
			this.hasResult = true;
			this.numSPPReturned = Integer.toString(fireStudyList.size());
		} else {
			this.hasResult = false;
			this.numSPPReturned = "0";
		}

		System.out.println("This is the FS Results Page Title: "
				+ fsResultsTitleOne);

		return "fireStudyResults";
	}

	public String searchByFireStudy() {
		// Dynamically changing the title of the results page
		setFsResultsTitleOne("set");
		// end

		// keeps these guys from showing up where they aren't supposed to
		setAdminJunk("");
		setLocationJunk("");
		setMapJunk("");
		setPlantJunk("");
		//

		if (fireStudyList != null) {
			fireStudyList.clear();
		}
		this.fireStudyResultsSQL = "";
		this.reportDescription = "";
		this.byName = false;
		this.numSPPReturned = "0";
		setReportErrorMsg("");

		FireStudyQueryBuilder qb = new FireStudyQueryBuilder();
		qb.setInputTitle(fireRegimeTitle);
		if (isBlank(this.inputFSName) && isBlank(this.inputTitle)
				&& isBlank(this.inputAuthor)) {
			qb.setByName(false);

			if (this.byState) {
				qb.setByState(this.byState);
				qb.setStates(this.states);
				// experimental
				// setLocationJunk("State, province, and country searches are based on species distribution data obtained mostly from the ");
			}
			qb.setByAdminUnit(this.byAdminUnit);
			if (this.byAdminUnit) {
				qb.setUnitNames(this.unitNames);
				qb.setAgency(this.agency);
				qb.setRegion(this.region);
				if (!validateAdminUnit()) {
					this.hasResult = false;
					return null;
				}
				// experimental
				setAdminJunk(ADMINISTRATIVE_UNIT_MSG);
			}
			qb.setByPnvg(this.byPnvg);
			if (this.byPnvg) {
				if (!validatePlantCommunity()) {
					this.hasResult = false;
					return null;
				}

				String selected_codes = "";

				// determine if we need to retrieve all pnvgs
				PNVGSearch allPnvgs = new PNVGSearch();
				boolean allSelections = false;
				if (this.pnvgs == null || this.pnvgs.length == 0
						|| "".equals(this.pnvgs[0])) {
					selected_codes = allPnvgs.getCSVPnvgs(this.pnvgRegion,
							this.vegTypeDesc);
					allSelections = true;
				} else {
					selected_codes = pnvgsToCSVString(this.pnvgs);
				}

				if (selected_codes != null
						&& (selected_codes.compareTo("0") == 0)
						|| selected_codes.compareTo("") == 0) {
					selected_codes = allPnvgs.getCSVPnvgs(this.pnvgRegion,
							this.vegTypeDesc);
					allSelections = true;
				}

				// this is what I need to change to the get the goddamn BPS
				// codes
				// String crosswalk_codes = getVegsysCode(selected_codes);
				// qb.setPnvgs(crosswalk_codes);

				// new
				System.out
						.println("This is the selected codes from plant community : "
								+ selected_codes);
				if ("".equals(selected_codes.trim())) {

					setReportErrorMsg("There are no plant communities for the selected formation. Please try again.");
					this.hasResult = false;
					this.displayReport = true;
					return null;
				}

				// new
				System.out
						.println("This is the selected codes from plant community : "
								+ selected_codes);
				String bps_codes = getBpsCodeFinal(selected_codes);
				System.out
						.println("This is the new BPS codes (technically bps ID for queries) from plant community : "
								+ bps_codes);
				qb.setBps(bps_codes);
				// end new

				if (!allSelections) {
					String plantCommunitySelections = getSelectedPlantCommunities();
					qb.setPlantCommunities(plantCommunitySelections);
				} else
					qb.setPlantCommunities("All");
				// experimental
				setPlantJunk(PLANT_COMMUNITY_MSG);
			}

			qb.setByMap(this.byMap);
			qb.setByMapAlaska(this.byMapAlaska);
			qb.setByMapHawaii(this.byMapHawaii);

			qb.setInputLatitude(this.inputLatitude);
			qb.setInputLongitude(this.inputLongitude);
			qb.setGeoRadius(this.geoRadius);
			
			double radius = .7;
			if (this.byMap) {
				System.out
						.println("You are @ byMap on FireStudy advanced search");
				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}
				String bps_codes = getBpsCode(inputLatitude, inputLongitude,
						radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				String fs_id = getFSID(bps_codes_final);
				qb.setBps(fs_id);
				setMapJunk(MAP_SEARCHES_MSG);
			}
			if (this.byMapAlaska) {
				System.out.println("you made it to byMapAlaska!");

				System.out.println("this is the lat" + inputLatitude);
				System.out.println("this is the long" + inputLongitude);
				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}

				String bps_codes = getBpsCodeAlaska(inputLatitude,
						inputLongitude, radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				System.out.println("This is the BPS 'pt' and 'radius': "
						+ bps_codes);
				System.out
						.println("This is the BPS code/s: " + bps_codes_final);

				String fs_id = getFSID(bps_codes_final);
				qb.setBps(fs_id);

				setMapJunk(MAP_SEARCHES_MSG);

			}
			if (this.byMapHawaii) {
				System.out.println("you made it to byMapHawaii!");
				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}

				String bps_codes = getBpsCodeHawaii(inputLatitude,
						inputLongitude, radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				System.out.println("This is the BPS 'pt' and 'radius': "
						+ bps_codes);
				System.out
						.println("This is the BPS code/s: " + bps_codes_final);

				String fs_id = getFSID(bps_codes_final);
				qb.setBps(fs_id);
				setMapJunk(MAP_SEARCHES_MSG);

			}

			this.reportDescription = qb.getDescription();

			String sqlString = qb.buildGeoQuery();
			this.fireStudyResultsSQL = sqlString;
			DbAccess DMan = new DbAccess(sqlString);
			if (this.fireStudyResultsSQL != null
					&& this.fireStudyResultsSQL.length() > 0) {
				ResultSet queryResults = DMan.ExecuteQuery();
				populateFireStudyList(queryResults);
			}

			setReportErrorMsg(DMan.getErrMessage() + qb.getErrMsg());

			// This is new, should keep the simple searches from taking
			// properties (state, admin, pvng and map info) from the advanced
			// searches.
			this.byState = false;
			this.byAdminUnit = false;
			this.byMap = false;
			this.byMapAlaska = false;
			this.byMapHawaii = false;
			this.byPnvg = false;
		}
		// look at "buildGeoQuery()" and "buildNameQuery()" for "invalid column"
		// issues, the two need to be combined in order for this to work
		else {
			qb.setByName(true);
			qb.setInputFSName(this.inputFSName);
			qb.setInputAuthor(this.inputAuthor);
			qb.setInputTitle(this.inputTitle);

			if (this.byState) {
				qb.setByState(this.byState);
				qb.setStates(this.states);
				// experimental
				// setLocationJunk("State, province, and country searches are based on species distribution data obtained mostly from the ");
			}

			qb.setByAdminUnit(this.byAdminUnit);
			if (this.byAdminUnit) {
				qb.setUnitNames(this.unitNames);
				qb.setAgency(this.agency);
				qb.setRegion(this.region);
				if (!validateAdminUnit()) {
					this.hasResult = false;
					return null;
				}
				// experimental
				setAdminJunk(ADMINISTRATIVE_UNIT_MSG);
			}

			qb.setByPnvg(this.byPnvg);
			if (this.byPnvg) {
				if (!validatePlantCommunity()) {
					this.hasResult = false;
					return null;
				}
				String selected_codes = "";

				// determine if we need to retrieve all pnvgs
				PNVGSearch allPnvgs = new PNVGSearch();
				boolean allSelections = false;
				if (this.pnvgs == null || "".equals(this.pnvgs)
						|| this.pnvgs.length == 0) {
					selected_codes = allPnvgs.getCSVPnvgs(this.pnvgRegion,
							this.vegTypeDesc);
					allSelections = true;
				} else {
					selected_codes = pnvgsToCSVString(this.pnvgs);
				}
				if (selected_codes != null
						&& (selected_codes.compareTo("0") == 0)
						|| selected_codes.compareTo("") == 0) {
					selected_codes = allPnvgs.getCSVPnvgs(this.pnvgRegion,
							this.vegTypeDesc);
					allSelections = true;
				}
				if ("".equals(selected_codes.trim())) {

					setReportErrorMsg("There are no plant communities for the selected formation. Please try again.");
					this.hasResult = false;
					return null;
				}

				// new
				System.out
						.println("This is the selected codes from plant community : "
								+ selected_codes);
				String bps_codes = getBpsCodeFinal(selected_codes);
				System.out
						.println("This is the new BPS codes (technically bps ID for queries) from plant community : "
								+ bps_codes);
				qb.setBps(bps_codes);
				// end new

				// not needed anymore since BPS is in place
				// String crosswalk_codes = getVegsysCodeFS(selected_codes);
				// qb.setPnvgs(crosswalk_codes);

				if (!allSelections) {
					String plantCommunitySelections = getSelectedPlantCommunities();
					qb.setPlantCommunities(plantCommunitySelections);
				} else
					qb.setPlantCommunities("All");
				// experimental
				setPlantJunk(PLANT_COMMUNITY_MSG);
			}

			qb.setByMap(this.byMap);
			qb.setByMapAlaska(this.byMapAlaska);
			qb.setByMapHawaii(this.byMapHawaii);

			qb.setInputLatitude(this.inputLatitude);
			qb.setInputLongitude(this.inputLongitude);
			qb.setGeoRadius(this.geoRadius);
			double radius = 0.7;
			if (this.byMap) {
				System.out
						.println("You are @ byMap on FireStudy advanced search");

				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}
				String bps_codes = getBpsCode(inputLatitude, inputLongitude,
						radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				String fs_id = getFSID(bps_codes_final);
				qb.setBps(fs_id);
				setMapJunk(MAP_SEARCHES_MSG);
			}
			if (this.byMapAlaska) {
				System.out.println("you made it to byMapAlaska!");

				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}
				String bps_codes = getBpsCodeAlaska(inputLatitude,
						inputLongitude, radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				System.out.println("This is the BPS 'pt' and 'radius': "
						+ bps_codes);
				System.out
						.println("This is the BPS code/s: " + bps_codes_final);

				String fs_id = getFSID(bps_codes_final);
				qb.setBps(fs_id);

				setMapJunk(MAP_SEARCHES_MSG);

			}
			if (this.byMapHawaii) {
				System.out.println("you made it to byMapHawaii!");

				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}
				String bps_codes = getBpsCodeHawaii(inputLatitude,
						inputLongitude, radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				System.out.println("This is the BPS 'pt' and 'radius': "
						+ bps_codes);
				System.out
						.println("This is the BPS code/s: " + bps_codes_final);

				String fs_id = getFSID(bps_codes_final);
				qb.setBps(fs_id);
				setMapJunk(MAP_SEARCHES_MSG);

			}
			this.fireStudyResultsSQL = qb.buildNameQuery();

			DbAccess DMan = new DbAccess(this.fireStudyResultsSQL);
			setReportErrorMsg(DMan.getErrMessage());
			ResultSet queryResults = DMan.ExecuteQuery();
			populateFireStudyList(queryResults);
		}

		this.reportDescription = qb.getDescription();
		this.displayReport = true;

		if (fireStudyList != null && !fireStudyList.isEmpty()) {
			this.hasResult = true;
			this.numSPPReturned = Integer.toString(fireStudyList.size());
			 
		} else {
			this.hasResult = false;
			this.numSPPReturned = "0";
		}

		// This is new, should keep the simple searches from taking properties
		// (state, admin, pvng and map info) from the advanced searches.
		this.byState = false;
		this.byAdminUnit = false;
		this.byMap = false;
		this.byMapAlaska = false;
		this.byMapHawaii = false;
		this.byPnvg = false;
		// setInputAuthor("");
		// setInputTitle("");
		//

		return "fireStudyResults";
	}

	protected boolean validateAdminUnit() {
		// boolean isValidated = true;
		boolean isValidated = true;
		if (("".equals(agency) || agency == null || agency.compareTo("0") == 0)) {
			isValidated = false;

		}

		if (("".equals(region) || region == null || region.compareTo("0") == 0)) {
			isValidated = false;
		}

		if (nameItems == null || nameItems.size() == 0) {
			isValidated = false;
		}

		if (!isValidated) {
			setReportErrorMsg("Select agency and region.");
		}
		return isValidated;
	}

	protected boolean validatePlantCommunity() {
		boolean isValidated = true;
		if (("".equals(this.pnvgRegion) || this.pnvgRegion == null)
				|| this.pnvgRegion.compareTo("0") == 0)
			isValidated = false;
		if (("".equals(this.vegTypeDesc) || this.vegTypeDesc == null)
				|| this.vegTypeDesc.compareTo("0") == 0)
			isValidated = false;
		if (!isValidated)
			setReportErrorMsg("Select region and formation.");
		return isValidated;
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

	protected String generateGUID16() {
		String pt_id = "";
		UUID uniqueId = UUID.randomUUID();
		String pid = uniqueId.toString();

		pid = pid.replace("-", "");

		pt_id = pid.substring(0, 16);

		return pt_id;
	}

	protected String getPNVGCode(String pt_id, double radius) {

		String PNVGStr = "";
		GeoData dbAccessUtils = new GeoData();
		ResultSet results = dbAccessUtils.getPNVGCode(pt_id, radius);

		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						String pnvg_code = results.getString(1);
						PNVGStr = PNVGStr.trim() + pnvg_code + ",";
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (PNVGStr != null && PNVGStr.length() > 0)
					PNVGStr = PNVGStr.substring(0, PNVGStr.length() - 1);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return PNVGStr;
	}

	/* Needed for Bps map search */
	protected String getBpsCode(String lat, String longi, double radius) {
		System.out
				.println("This is the 'pt_id' and 'radius' being passed to getBpsCode: "
						+ lat + " : " + longi + "," + radius);
		String BpsStr = "";
		GeoData dbAccessUtils = new GeoData();
		ResultSet results = dbAccessUtils.getBpsCode(lat, longi, radius);

		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						String bps_code = results.getString(1);
						BpsStr = BpsStr.trim() + bps_code + ",";
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (BpsStr != null && BpsStr.length() > 0)
					BpsStr = BpsStr.substring(0, BpsStr.length() - 1);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return BpsStr;
	}

	protected String getBpsCodeAlaska(String lat, String longi, double radius) {
		System.out
				.println("This is the 'pt_id' and 'radius' being passed to getBpsCodeAlaska: "
						+ lat + " : " + longi + " , " + radius);
		String BpsStr = "";
		GeoData dbAccessUtils = new GeoData();
		ResultSet results = dbAccessUtils.getBpsCodeAlaska(lat, longi, radius);

		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						String bps_code = results.getString(1);
						BpsStr = BpsStr.trim() + bps_code + ",";
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (BpsStr != null && BpsStr.length() > 0)
					BpsStr = BpsStr.substring(0, BpsStr.length() - 1);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return BpsStr;
	}

	protected String getBpsCodeHawaii(String lat, String longi, double radius) {
		System.out
				.println("This is the 'lat long' and 'radius' being passed to getBpsCodeHawaii: "
						+ lat + " : " + longi + " ," + radius);
		String BpsStr = "";
		GeoData dbAccessUtils = new GeoData();
		ResultSet results = dbAccessUtils.getBpsCodeHawaii(lat, longi, radius);

		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						String bps_code = results.getString(1);
						BpsStr = BpsStr.trim() + bps_code + ",";
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (BpsStr != null && BpsStr.length() > 0)
					BpsStr = BpsStr.substring(0, BpsStr.length() - 1);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return BpsStr;
	}

	/* End */

	protected String pnvgsToCSVString(String[] pnvgCodes) {
		String formattedPnvgs = "";
		if ((pnvgCodes == null) || (pnvgCodes.length == 0))
			return "";
		for (int i = 0; i < pnvgCodes.length; i++)
			formattedPnvgs = formattedPnvgs + "" + pnvgCodes[i].trim() + ",";

		if (formattedPnvgs.length() > 0)
			formattedPnvgs = formattedPnvgs.substring(0,
					formattedPnvgs.length() - 1);

		return formattedPnvgs;
	}

	protected String getSelectedPlantCommunities() {
		String selectedPnvgs = "";
		if (byPnvg) {
			if ((pnvgs != null) && (pnvgs.length > 0)) {
				for (int i = 0; i < pnvgs.length; i++) {
					String selectedItem = items.get(pnvgs[i].trim());
					selectedPnvgs = selectedPnvgs + selectedItem + ",";
				}
				if (selectedPnvgs.length() > 0)
					selectedPnvgs = selectedPnvgs.substring(0,
							selectedPnvgs.length() - 1);
			}

		}
		return selectedPnvgs;
	}

	protected String getVegsysCode(String PNVG_Code) {
		String vegsys_code_2 = "";
		if ("".equals(PNVG_Code) || PNVG_Code == null)
			return "";
		// PNVG codes
		String[] PNVG = PNVG_Code.split(",");
		String PNVGStr = "";
		for (int i = 0; i < PNVG.length; i++)
			PNVGStr = PNVGStr.trim() + "'" + PNVG[i].trim() + "',";

		if (PNVGStr.length() > 0)
			PNVGStr = PNVGStr.substring(0, PNVGStr.length() - 1);

		GeoData dbAccessUtils = new GeoData();
		ResultSet results = dbAccessUtils.getVegSysCode(PNVGStr);
		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						String veg_code = results.getString(1);
						vegsys_code_2 = vegsys_code_2.trim() + veg_code + ",";

					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
				if ((vegsys_code_2 != null) && (vegsys_code_2.length() > 0))
					vegsys_code_2 = vegsys_code_2.substring(0,
							vegsys_code_2.length() - 1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return vegsys_code_2;
	}

	/*
	 * Adding this so I can search by BPS in the map functions on the review
	 * page!
	 */
	protected String getFSID(String Bps_Code_Final) {
		System.out
				.println("This is the FS_ID being passed to getFSID, which is actually the bps code from getBpsCodeFinal: "
						+ Bps_Code_Final);
		String fs_id = "";
		if ("".equals(Bps_Code_Final) || Bps_Code_Final == null)
			return Bps_Code_Final;
		String[] Bps = Bps_Code_Final.split(",");
		String BpsStr = "";
		for (int i = 0; i < Bps.length; i++)
			BpsStr = BpsStr.trim() + "'" + Bps[i].trim() + "',";

		if (BpsStr.length() > 0)
			BpsStr = BpsStr.substring(0, BpsStr.length() - 1);

		GeoData dbAccessUtils = new GeoData();
		ResultSet results = dbAccessUtils.getBpsFSID(BpsStr);
		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {
						String bps_code = results.getString(1);
						fs_id = fs_id.trim() + bps_code + ",";

					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
				if ((fs_id != null) && (fs_id.length() > 0)) {
					fs_id = fs_id.substring(0, fs_id.length() - 1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out
				.println("This is the fs_id for the corresponding bps codes: "
						+ fs_id);
		return fs_id;
	}
	
	protected String getFireRegimeIds(String Bps_Code_Final) {
		System.out
				.println("getFireRegimeIds: "
						+ Bps_Code_Final);
		String fr_ids = "";
		if ("".equals(Bps_Code_Final) || Bps_Code_Final == null)
			return Bps_Code_Final;
		String[] Bps = Bps_Code_Final.split(",");
		String BpsStr = "";
		for (int i = 0; i < Bps.length; i++)
			BpsStr = BpsStr.trim() + "'" + Bps[i].trim() + "',";

		if (BpsStr.length() > 0)
			BpsStr = BpsStr.substring(0, BpsStr.length() - 1);

		GeoData dbAccessUtils = new GeoData();
		ResultSet results = dbAccessUtils.getBpsFRID(BpsStr);
		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {
						String bps_code = results.getString(1);
						fr_ids = fr_ids.trim() + bps_code + ",";

					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
				if ((fr_ids != null) && (fr_ids.length() > 0)) {
					fr_ids = fr_ids.substring(0, fr_ids.length() - 1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out
				.println("This is the fr_id list for the corresponding bps codes: "
						+ fr_ids);
		return fr_ids;
	}


	protected String getBpsCodeFinal(String Bps_Code) {
		System.out
				.println("This is the Bps_Code being passed to BPSCodeFinal: "
						+ Bps_Code);
		String bps_code_2 = "";
		if ("".equals(Bps_Code) || Bps_Code == null)
			return Bps_Code;// return "";
		// PNVG codes
		String[] Bps = Bps_Code.split(",");
		String BpsStr = "";
		for (int i = 0; i < Bps.length; i++)
			BpsStr = BpsStr.trim() + "'" + Bps[i].trim() + "',";

		if (BpsStr.length() > 0)
			BpsStr = BpsStr.substring(0, BpsStr.length() - 1);

		GeoData dbAccessUtils = new GeoData();
		ResultSet results = dbAccessUtils.getBpsSysCode(BpsStr);
		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						String bps_code = results.getString(1);
						bps_code_2 = bps_code_2.trim() + bps_code + ",";

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if ((bps_code_2 != null) && (bps_code_2.length() > 0))
					bps_code_2 = bps_code_2.substring(0,
							bps_code_2.length() - 1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return bps_code_2;
	}

	// I added this so that the map would work again in Fire Studies Search
	// March 25th 2014
	protected String getVegsysCodeFS(String PNVG_Code) {
		String vegsys_code_2 = "";
		if ("".equals(PNVG_Code) || PNVG_Code == null)
			return "";
		// PNVG codes
		String[] PNVG = PNVG_Code.split(",");
		String PNVGStr = "";
		for (int i = 0; i < PNVG.length; i++)
			PNVGStr = PNVGStr.trim() + "'" + PNVG[i].trim() + "',";

		if (PNVGStr.length() > 0)
			PNVGStr = PNVGStr.substring(0, PNVGStr.length() - 1);

		GeoData dbAccessUtils = new GeoData();
		ResultSet results = dbAccessUtils.getVegSysCodeFS(PNVGStr);
		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				while (results.next()) {
					try {

						String veg_code = results.getString(1);
						vegsys_code_2 = vegsys_code_2.trim() + veg_code + ",";

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if ((vegsys_code_2 != null) && (vegsys_code_2.length() > 0))
					vegsys_code_2 = vegsys_code_2.substring(0,
							vegsys_code_2.length() - 1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return vegsys_code_2;
	}

	protected ResultSet populateSpeciesList(ResultSet results) {
		speciesList.clear();
		if (results != null) {

			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				while (results.next()) {
				 
					SPP sp = new SPP();
					String sppAcronym = results.getString("ACRONYM");
				 
					String sppFSID = results.getString("FS_ID");
					sp.setFSID(results.getString("FS_ID"));
				 
					String embeddedhtmlreviews = results
							.getString("FIRESTUDY_HTML_PATH");
					// new
					if (embeddedhtmlreviews != null) {
						sp.setFireStudyTitle("Available");
						this.hasLink = true;
					} else {
						sp.setFireStudyTitle2("Not Available");
						// this.hasLink = false;
					}
					//
					if (embeddedhtmlreviews != null
							&& embeddedhtmlreviews.length() > 0)
						sp.setFireStudyLink(embeddedhtmlreviews);
					else
						sp.setFireStudyLink("#");

					sp.setScientificName(results.getString("SCITITLE"));

					sp.setCommonName(results.getString("COMTITLE"));
					sp.setAcronym(results.getString("ACRONYM"));
					String htmlembedded = results.getString("HTML_PATH");
					if (htmlembedded != null && htmlembedded.length() > 0)
						sp.setLinkToSpeciesReview(htmlembedded);
					else
						sp.setLinkToSpeciesReview("#");
					sp.setReviewDate(results.getString("YEAR_WRITTEN"));
					sp.setSpeciesID(results.getInt("SPP_ID"));
					String pdfPath = results.getString("PDF_PATH");
					if (pdfPath != null && pdfPath.length() > 0)
						sp.setLinkToPDFFile(results.getString("PDF_PATH"));
					else
						sp.setLinkToPDFFile("#");

					long fireRegimeRefCountForSpecies = results
							.getLong("FR_REFERENCED_COUNT");
					sp.setSpeciesFireRegimeCount(fireRegimeRefCountForSpecies);
					if (fireRegimeRefCountForSpecies > 0) {
						sp.setSpeciesHasFireRegimes("Available");
					} else {
						sp.setSpeciesHasFireRegimes("Not Available");
					}
					 
					if (sppAcronym != null) {
						speciesList.add(sp);
					}

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return results;
	}

	protected ResultSet populateFireStudyList(ResultSet results) {
		fireStudyList.clear();
		if (results != null) {
			try {
				
				results.beforeFirst();
				while (results.next()) {
					FireStudy fs = new FireStudy();
					// changed this so that items with no review would show up
					// String sppAcronym = results.getString("ACRONYM");
					String sppAcronym = results.getString("FS_ID");

					// new stuff for not available items
					String sppAcronym2 = results.getString("ACRONYM");

					// List<String> arrList = new ArrayList<String>();
					// arrList.add(sppAcronym);
					// System.out.println(arrList);

					fs.setFSID(results.getString("FS_ID"));
					// System.out.println(sppFSID);

					// changed to make items show up as not available
					// if (sppAcronym != null)
					if (sppAcronym2 != null) {
						fs.setSpeciesTitle("Available");
					} else {
						fs.setSpeciesTitle2("Not Available");
					}

					fs.setAcronym(results.getString("ACRONYM"));

					fs.setAuthor(results.getString("FS_AUTHORS"));

					fs.setTitle(results.getString("TITLE"));

					fs.setState(results.getString("ABBR"));

					String embeddedhtml = results
							.getString("REVIEWS_HTML_PATH");

					if (embeddedhtml != null && embeddedhtml.length() > 0)
						fs.setLinkToSpeciesReview(embeddedhtml);
					else
						fs.setLinkToSpeciesReview("#");
					String htmlembedded = results.getString("HTML_PATH");

					if (htmlembedded != null && htmlembedded.length() > 0)
						fs.setLinkToFireStudy(htmlembedded);
					else
						fs.setLinkToFireStudy("#");

					long fireRegimeRefCountForSpecies = results
							.getLong("FR_REFERENCED_COUNT");
					fs.setSpeciesFireRegimeCount(fireRegimeRefCountForSpecies);
					if (fireRegimeRefCountForSpecies > 0) {
						fs.setSpeciesHasFireRegimes("Available");
					} else {
						fs.setSpeciesHasFireRegimes("Not Available");
					}

					if (sppAcronym != null) {
						fireStudyList.add(fs);
					}
				}
			} catch (SQLException a) {
				a.printStackTrace();
			}
		}
		fireStudyList = mergeById(fireStudyList);
 
		return results;
	}

	private ArrayList<FireStudy> mergeById(List<FireStudy> before) {

		Map<String, FireStudy> map = new LinkedHashMap<String, FireStudy>();

		for (FireStudy fireStudy : before) {
			FireStudy item = map.get(fireStudy.getFSID());
			if (item != null) {
				if(!item.getState().contains(fireStudy.getState())) { 
					item.setState(item.getState() + ", " + fireStudy.getState());
				}
			} else {
				map.put(fireStudy.getFSID(), fireStudy);
			}
		}
		return new ArrayList<FireStudy>(map.values());

	}

	public String goHome() {
		inputFSName = "";
		return "/index.xhtml?faces-redirect=true";
	}

	public String getInputFrName() {
		return inputFrName;
	}

	public void setInputFrName(String inputFrName) {
		this.inputFrName = inputFrName;
	}

	public String searchByFrHome() {
		return null;
	}
	
	public String searchFireRegimeAll() throws Exception {
		clearSearch();
		return searchByFireRegime();
	}

	public String searchFireRegimesForSpecies() throws Exception {
		
		logger.info("searchFireRegimesForSpecies :BEGIN : " + toString());
		FireRegimeQueryBuilder qb = new FireRegimeQueryBuilder();
		FacesContext context = FacesContext.getCurrentInstance();
		String specid = context.getExternalContext().getRequestParameterMap().get("speciesId");
		qb.setSpecies(Integer.parseInt(specid));
		
		this.fireRegimeResultsSQL = qb.buildSpeciesQuery();

		DbAccess DMan = new DbAccess(this.fireRegimeResultsSQL);
		setReportErrorMsg(DMan.getErrMessage());
		ResultSet queryResults = DMan.ExecuteQuery();
		fireRegimeList = toFireRegimeList(queryResults);
		fireRegimeResultCount = getDistinctCount(fireRegimeList);
		reportDescription = qb.getDescription();
		fireRegimeName = qb.getSpeciesName();
		return "FireRegimeResults";	
	}

	
	public String searchFireRegimesForFireStudy() throws Exception {
		
		logger.info("searchFireRegimesForFireStudy :BEGIN : " + toString());
		FireRegimeQueryBuilder qb = new FireRegimeQueryBuilder();
		FacesContext context = FacesContext.getCurrentInstance();
		String fsId = context.getExternalContext().getRequestParameterMap().get("fsId");
		qb.setFireStudyId(fsId);
		
		this.fireRegimeResultsSQL = qb.buildFireStudiesQuery();

		DbAccess DMan = new DbAccess(this.fireRegimeResultsSQL);
		setReportErrorMsg(DMan.getErrMessage());
		ResultSet queryResults = DMan.ExecuteQuery();
		fireRegimeList = toFireRegimeList(queryResults);
		fireRegimeResultCount = getDistinctCount(fireRegimeList);
		reportDescription = qb.getDescription();
		fireRegimeName = qb.getSpeciesName();
		return "FireRegimeResults";	
	}
	
	
	public String searchByFireRegime() throws Exception {
		
		logger.info("searchByFireRegime :BEGIN : " + toString());
		
		setFrResultsTitleOne("");
		setFrResultsTitleTwo("");
		setAdminJunk("");
		setLocationJunk("");
		setMapJunk("");
		setPlantJunk("");


		if (fireRegimeList != null) {
			fireRegimeList.clear();
		}
		this.fireRegimeResultsSQL = "";
		this.reportDescription = "";
		this.byName = false;
		this.numSPPReturned = "0";
		setReportErrorMsg("");

		FireRegimeQueryBuilder qb = new FireRegimeQueryBuilder();
		qb.setInputTitle(fireRegimeTitle);
		if (isBlank(this.fireRegimeName) && isBlank(fireRegimeTitle) ) {
			qb.setByName(false);

			if (this.byState) {
				qb.setByState(this.byState);
				qb.setStates(this.states);
 			}
			qb.setByAdminUnit(this.byAdminUnit);
			if (this.byAdminUnit) {
				qb.setUnitNames(this.unitNames);
				qb.setAgency(this.agency);
				qb.setRegion(this.region);
				if (!validateAdminUnit()) {
					this.hasResult = false;
					return null;
				} 
				setAdminJunk(ADMINISTRATIVE_UNIT_MSG);
			}
			qb.setByPnvg(this.byPnvg);
			if (this.byPnvg) {
				if (!validatePlantCommunity()) {
					this.hasResult = false;
					return null;
				}

				String selected_codes = "";

				// determine if we need to retrieve all pnvgs
				PNVGSearch allPnvgs = new PNVGSearch();
				boolean allSelections = false;
				if (this.pnvgs == null || this.pnvgs.length == 0
						|| "".equals(this.pnvgs[0])) {
					selected_codes = allPnvgs.getCSVPnvgs(this.pnvgRegion,
							this.vegTypeDesc);
					allSelections = true;
				} else {
					selected_codes = pnvgsToCSVString(this.pnvgs);
				}

				if (selected_codes != null
						&& (selected_codes.compareTo("0") == 0)
						|| selected_codes.compareTo("") == 0) {
					selected_codes = allPnvgs.getCSVPnvgs(this.pnvgRegion,
							this.vegTypeDesc);
					allSelections = true;
				}

				System.out
						.println("This is the selected codes from plant community : "
								+ selected_codes);
				if ("".equals(selected_codes.trim())) {

					setReportErrorMsg("There are no plant communities for the selected formation. Please try again.");
					this.hasResult = false;
					this.displayReport = true;
					return null;
				}

				System.out
						.println("This is the selected codes from plant community : "
								+ selected_codes);
				String bps_codes = getBpsCodeFinal(selected_codes);
				System.out
						.println("This is the new BPS codes (technically bps ID for queries) from plant community : "
								+ bps_codes);
				qb.setBps(bps_codes);
			 
				if (!allSelections) {
					String plantCommunitySelections = getSelectedPlantCommunities();
					qb.setPlantCommunities(plantCommunitySelections);
				} else
					qb.setPlantCommunities("All");
				// experimental
				setPlantJunk(PLANT_COMMUNITY_MSG);
			}

			qb.setByMap(this.byMap);
			qb.setByMapAlaska(this.byMapAlaska);
			qb.setByMapHawaii(this.byMapHawaii);

			qb.setInputLatitude(this.inputLatitude);
			qb.setInputLongitude(this.inputLongitude);
			qb.setGeoRadius(this.geoRadius);
			double radius = .7;
			if (this.byMap) {
				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}
				String bps_codes = getBpsCode(inputLatitude, inputLongitude,
						radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				qb.setBps( getFireRegimeIds(bps_codes_final));
				setMapJunk(MAP_SEARCHES_MSG);
			}
			if (this.byMapAlaska) {
				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}

				String bps_codes = getBpsCodeAlaska(inputLatitude,
						inputLongitude, radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				qb.setBps( getFireRegimeIds(bps_codes_final));

				setMapJunk(MAP_SEARCHES_MSG);

			}
			if (this.byMapHawaii) {
				System.out.println("you made it to byMapHawaii!");
				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}

				String bps_codes = getBpsCodeHawaii(inputLatitude,
						inputLongitude, radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				System.out.println("This is the BPS 'pt' and 'radius': "
						+ bps_codes);
				System.out
						.println("This is the BPS code/s: " + bps_codes_final);

				qb.setBps(getFireRegimeIds(bps_codes_final));
				setMapJunk(MAP_SEARCHES_MSG);

			}

			this.reportDescription = qb.getDescription();

			String sqlString = qb.buildNameQuery();
			//String sqlString = qb.buildGeoQuery();
			this.fireRegimeResultsSQL = sqlString;
			DbAccess DMan = new DbAccess(sqlString);
			if (this.fireRegimeResultsSQL != null
					&& this.fireRegimeResultsSQL.length() > 0) {
				ResultSet queryResults = DMan.ExecuteQuery();
				fireRegimeList = toFireRegimeList(queryResults);
				
				fireRegimeResultCount = getDistinctCount(fireRegimeList);
				
			}

			setReportErrorMsg(DMan.getErrMessage() + qb.getErrMsg());

			this.byState = false;
			this.byAdminUnit = false;
			this.byMap = false;
			this.byMapAlaska = false;
			this.byMapHawaii = false;
			this.byPnvg = false;
		} 
		else {
			qb.setByName(true);
			qb.setFireRegimeName(fireRegimeName);
			 

			if (this.byState) {
				qb.setByState(this.byState);
				qb.setStates(this.states);
			}

			qb.setByAdminUnit(this.byAdminUnit);
			if (this.byAdminUnit) {
				qb.setUnitNames(this.unitNames);
				qb.setAgency(this.agency);
				qb.setRegion(this.region);
				if (!validateAdminUnit()) {
					this.hasResult = false;
					return null;
				}
				setAdminJunk(ADMINISTRATIVE_UNIT_MSG);
			}

			qb.setByPnvg(this.byPnvg);
			if (this.byPnvg) {
				if (!validatePlantCommunity()) {
					this.hasResult = false;
					return null;
				}
				String selected_codes = "";

				PNVGSearch allPnvgs = new PNVGSearch();
				boolean allSelections = false;
				if (this.pnvgs == null || "".equals(this.pnvgs)
						|| this.pnvgs.length == 0) {
					selected_codes = allPnvgs.getCSVPnvgs(this.pnvgRegion,
							this.vegTypeDesc);
					allSelections = true;
				} else {
					selected_codes = pnvgsToCSVString(this.pnvgs);
				}
				if (selected_codes != null
						&& (selected_codes.compareTo("0") == 0)
						|| selected_codes.compareTo("") == 0) {
					selected_codes = allPnvgs.getCSVPnvgs(this.pnvgRegion,
							this.vegTypeDesc);
					allSelections = true;
				}
				if ("".equals(selected_codes.trim())) {

					setReportErrorMsg("There are no plant communities for the selected formation. Please try again.");
					this.hasResult = false;
					return null;
				}

		 
				String bps_codes = getBpsCodeFinal(selected_codes);
		 
				qb.setBps(bps_codes);

				if (!allSelections) {
					String plantCommunitySelections = getSelectedPlantCommunities();
					qb.setPlantCommunities(plantCommunitySelections);
				} else
					qb.setPlantCommunities("All");
				// experimental
				setPlantJunk(PLANT_COMMUNITY_MSG);
			}

			qb.setByMap(this.byMap);
			qb.setByMapAlaska(this.byMapAlaska);
			qb.setByMapHawaii(this.byMapHawaii);

			qb.setInputLatitude(this.inputLatitude);
			qb.setInputLongitude(this.inputLongitude);
			qb.setGeoRadius(this.geoRadius);
			double radius = 0.7;
			if (this.byMap) {
				System.out
						.println("You are @ byMap on FireStudy advanced search");

				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}
				String bps_codes = getBpsCode(inputLatitude, inputLongitude,
						radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				String fs_id = getFSID(bps_codes_final);
				qb.setBps(fs_id);
				setMapJunk(MAP_SEARCHES_MSG);
			}
			if (this.byMapAlaska) {
				System.out.println("you made it to byMapAlaska!");

				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}
				String bps_codes = getBpsCodeAlaska(inputLatitude,
						inputLongitude, radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				System.out.println("This is the BPS 'pt' and 'radius': "
						+ bps_codes);
				System.out
						.println("This is the BPS code/s: " + bps_codes_final);

				String fs_id = getFSID(bps_codes_final);
				qb.setBps(fs_id);

				setMapJunk(MAP_SEARCHES_MSG);

			}
			if (this.byMapHawaii) {
				System.out.println("you made it to byMapHawaii!");

				if (!validateLatLong(inputLatitude, inputLongitude)) {
					setReportErrorMsg("Select a map location.");
					return "";
				}
				String bps_codes = getBpsCodeHawaii(inputLatitude,
						inputLongitude, radius);
				String bps_codes_final = getBpsCodeFinal(bps_codes);
				System.out.println("This is the BPS 'pt' and 'radius': "
						+ bps_codes);
				System.out
						.println("This is the BPS code/s: " + bps_codes_final);

				String fs_id = getFSID(bps_codes_final);
				qb.setBps(fs_id);
				setMapJunk(MAP_SEARCHES_MSG);

			}
			this.fireRegimeResultsSQL = qb.buildNameQuery();

			DbAccess DMan = new DbAccess(this.fireRegimeResultsSQL);
			setReportErrorMsg(DMan.getErrMessage());
			ResultSet queryResults = DMan.ExecuteQuery();
			fireRegimeList = toFireRegimeList(queryResults);
			fireRegimeResultCount = getDistinctCount(fireRegimeList);
		}

		this.reportDescription = qb.getDescription();
		this.displayReport = true;

		if (fireStudyList != null && !fireStudyList.isEmpty()) {
			this.hasResult = true;
 
		} else {
			this.hasResult = false;
			this.numSPPReturned = "0";
		}

		this.byState = false;
		this.byAdminUnit = false;
		this.byMap = false;
		this.byMapAlaska = false;
		this.byMapHawaii = false;
		this.byPnvg = false;
		// setInputAuthor("");
		// setInputTitle("");
		//
		
		return "FireRegimeResults";	
	}
	
	private int getDistinctCount(List<FireRegime> fireRegimeList2) {
		HashSet<Long> set = new HashSet<Long>();
		for (FireRegime fireRegime : fireRegimeList2) {
			set.add(fireRegime.getFrId());
		}
		return set.size();
	}

	protected List<FireRegime> toFireRegimeList(ResultSet rs) throws Exception
	{
		List<FireRegime> results = new ArrayList<FireRegime>();
		Map<Long, FireRegime> deduplicator = new LinkedHashMap<Long, FireRegime>();
        while (rs.next()) {
        	
            long id = rs.getLong("FR_ID");
            FireRegime bean = deduplicator.get(id); 
            String rg = rs.getString("BPSREGION");
			if(bean == null) { 
            	bean = new FireRegime();
            	bean.setFrId(id);
            	bean.setTitle(rs.getString("TITLE"));
            	bean.setBpsName(rs.getString("BIOPHYSICAL_SETTING_NAME"));
            	bean.setMonthWritten(rs.getString("MONTH_WRITTEN"));
            	bean.setYearWritten(rs.getString("YEAR_WRITTEN"));
            	bean.setQuality(rs.getInt("QUALITY"));
            	bean.setFeisTaxaCount(rs.getInt("NUM_FEIS_TAXA"));
            	bean.setNonFeisTaxaCount(rs.getInt("NUM_NON_FEIS_TAXA"));
            	bean.setPageCount(rs.getInt("NUM_PAGES"));
            	bean.setCitationCount(rs.getInt("NUM_CITATIONS"));
            	bean.setHtmlPath(rs.getString("HTML_PATH"));
            	bean.setNotes(rs.getString("NOTES"));
            	bean.setEntryComplete(rs.getBoolean("ENTRY_COMPLETE"));
            	bean.setFullTitle(rs.getString("FULL_TITLE"));
            	bean.setBpsMinInterval(rs.getDouble("BPS_MININTERVAL"));
            	bean.setBpsMaxInterval(rs.getDouble("BPS_MAXINTERVAL"));
            	bean.setMinReplace(rs.getDouble("MIN_REPLACE"));
            	bean.setMaxReplace(rs.getDouble("MAX_REPLACE"));
            	bean.setMinMixed(rs.getDouble("MIN_MIXED"));
            	bean.setMaxMixed(rs.getDouble("MAX_MIXED"));
            	bean.setMinLow(rs.getDouble("MIN_LOW"));
            	bean.setMaxLow(rs.getDouble("MAX_LOW"));
            	bean.setFrg1Count(rs.getInt("NUM_FRG-I"));
            	bean.setFrg2Count(rs.getInt("NUM_FRG-II"));
            	bean.setFrg3Count(rs.getInt("NUM_FRG-III"));
            	bean.setFrg4Count(rs.getInt("NUM_FRG-IV"));
            	bean.setFrg5Count(rs.getInt("NUM_FRG-V"));
            	bean.setFrgNACount(rs.getInt("NUM_FRG-NA"));
            	bean.setHtmlPathAppendixA(rs.getString("HTML_PATH_APPENDIXA"));
            	Object minI = rs.getObject("FEIS_MININTERVAL");
				bean.setFeisMinInterval((Integer)minI);
				Object maxI = rs.getObject("FEIS_MAXINTERVAL");
            	bean.setFeisMaxInterval((Integer)maxI);
            	bean.setBpsRegion(rg); 
            	bean.setFireStudyCount(rs.getLong("FIRE_STUDY_COUNT"));
            	bean.setReviewCount(rs.getLong("REVIEW_COUNT")); 
            	results.add(bean);
            	deduplicator.put(id, bean);
            }
            else
            {
            	if(bean.getBpsRegion().indexOf(rg) == -1) {
            		bean.setBpsRegion(bean.getBpsRegion() + ", " + rg);
            	}
            }
        }
        results = new ArrayList<FireRegime>(deduplicator.values());
        return results;
	}

	public List<FireRegime> getFireRegimeList() {
		return fireRegimeList;
	}

	public void setFireRegimeList(List<FireRegime> fireRegimeList) {
		this.fireRegimeList = fireRegimeList;
	}

	public String getFireRegimeName() {
		return fireRegimeName;
	}

	public void setFireRegimeName(String fireRegimeName) {
		this.fireRegimeName = fireRegimeName;
	}

	public String getFrResultsTitleOne() {
		return frResultsTitleOne;
	}

	public void setFrResultsTitleOne(String frResultsTitleOne) {
		this.frResultsTitleOne = frResultsTitleOne;
	}

	public String getFrResultsTitleTwo() {
		return frResultsTitleTwo;
	}

	public void setFrResultsTitleTwo(String frResultsTitleTwo) {
		this.frResultsTitleTwo = frResultsTitleTwo;
	}
	
	public void exportFireRegimeTable() throws Exception {
		
		Exporter tableExport = new Exporter();
		DbAccess DMan = new DbAccess(fireRegimeResultsSQL);
		ResultSet queryResults = DMan.ExecuteQuery();
		tableExport.setDescription(this.reportDescription);
		List<FireRegime> frlist = toFireRegimeList(queryResults);
		StringBuffer sb = tableExport.populateFireRegimeList(frlist);
		tableExport.exportTable(sb, "fire_regimes.csv");

	}
	
	public String gotoFireRegimeSearchAction() {
		clearSearch();
		return "FireRegimeSearch";
	}
	public String gotoReviseFireRegimeSearch() {
		if (states != null) {
			this.byState = true;
			this.byNewStates = true;
		}
		if (area == 2) {
			this.byAdminUnit = true;
		}

		// else if (pnvgRegion.length() > 0)
		if (area == 3) {
			this.byPnvg = true;
		}

		if (area == 4) {
			this.byMap = true;
		}

		if (area == 5) {
			this.byMapAlaska = true;
		}

		if (area == 6) {
			this.byMapHawaii = true;
		}

		return "FireRegimeSearch";
	}
	
	public String fireRegimeSearchAction() {
		clearSearch();
		clearHomeSearch();
		
		return "FireRegimeSearch";
	}

	public String getFireRegimeTitle() {
		return fireRegimeTitle;
	}

	public void setFireRegimeTitle(String fireRegimeTitle) {
		this.fireRegimeTitle = fireRegimeTitle;
	}

	public int getFireRegimeResultCount() {
		return fireRegimeResultCount;
	}

	public void setFireRegimeResultCount(int fireRegimeResultCount) {
		this.fireRegimeResultCount = fireRegimeResultCount;
	}

	
	 
	public int getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(int speciesId) {
		this.speciesId = speciesId;
	}

	@Override
	public String toString() {
		return "FEISSearch [sppAcronym=" + sppAcronym + ", sppFSID=" + sppFSID
				+ ", inputFrName=" + inputFrName + ", byMagic=" + byMagic
				+ ", speciesId=" + speciesId + ", byMoreMagic=" + byMoreMagic
				+ ", inputName=" + inputName + ", byName=" + byName
				+ ", inputFSName=" + inputFSName + ", byFSName=" + byFSName
				+ ", inputAuthor=" + inputAuthor + ", inputTitle=" + inputTitle
				+ ", byLifeForm=" + byLifeForm + ", plants="
				+ Arrays.toString(plants) + ", animals="
				+ Arrays.toString(animals) + ", items=" + items
				+ ", lichenAll=" + lichenAll + ", fungusAll=" + fungusAll
				+ ", plantsAll=" + plantsAll + ", animalsAll=" + animalsAll
				+ ", byNativity=" + byNativity + ", nativity=" + nativity
				+ ", byInvasiveness=" + byInvasiveness + ", invasiveness="
				+ invasiveness + ", byFedStatus=" + byFedStatus
				+ ", fedStatus=" + fedStatus + ", byNewStates=" + byNewStates
				+ ", newStates=" + newStates + ", outCome=" + outCome
				+ ", displayReport=" + displayReport + ", hasResult="
				+ hasResult + ", hasLink=" + hasLink + ", reportDescription="
				+ reportDescription + ", reviewResultsSQL=" + reviewResultsSQL
				+ ", fireStudyResultsSQL=" + fireStudyResultsSQL
				+ ", byMagicString=" + byMagicString + ", magicString="
				+ magicString + ", byMoreMagicString=" + byMoreMagicString
				+ ", moreMagicString=" + moreMagicString + ", reportErrorMsg="
				+ reportErrorMsg + ", numSPPReturned=" + numSPPReturned
				+ ", numFSReturned=" + numFSReturned + ", adminJunk="
				+ adminJunk + ", mapJunk=" + mapJunk + ", locationJunk="
				+ locationJunk + ", plantJunk=" + plantJunk
				+ ", fsResultsTitleOne=" + fsResultsTitleOne
				+ ", fsResultsTitleTwo=" + fsResultsTitleTwo
				+ ", speciesResultsTitleOne=" + speciesResultsTitleOne
				+ ", speciesResultsTitleTwo=" + speciesResultsTitleTwo
				 
				+ ", inputLongitude=" + inputLongitude + ", inputLatitude="
				+ inputLatitude + ", geoRadius=" + geoRadius
				+ ", fireRegimeName=" + fireRegimeName + ", fireRegimeTitle="
				+ fireRegimeTitle + ", frResultsTitleOne=" + frResultsTitleOne
				+ ", frResultsTitleTwo=" + frResultsTitleTwo
				+ ", fireRegimeResultCount=" + fireRegimeResultCount
				+ ", byGeoArea=" + byGeoArea + ", geoArea=" + geoArea
				+ ", byOtherArea=" + byOtherArea + ", otherArea=" + otherArea
				+ ", byNewMapLayers=" + byNewMapLayers + ", newMapLayers="
				+ newMapLayers + ", byState=" + byState + ", states="
				+ Arrays.toString(states) + ", byAdminUnit=" + byAdminUnit
				+ ", area=" + area + ", agency=" + agency + ", agencyItems="
				+ agencyItems + ", agencyOn=" + agencyOn + ", regionItems="
				+ regionItems + ", region=" + region + ", regionOn=" + regionOn
				+ ", nameItems=" + nameItems + ", unitNames="
				+ Arrays.toString(unitNames) + ", byPnvg=" + byPnvg
				+ ", byMap=" + byMap + ", byMapAlaska=" + byMapAlaska
				+ ", byMapHawaii=" + byMapHawaii + ", stateItems=" + stateItems
				+ ", pnvgRegion=" + pnvgRegion + ", pnvgRegionItems="
				+ pnvgRegionItems + ", pnvgRegionOn=" + pnvgRegionOn
				+ ", plantCommunityOn=" + plantCommunityOn + ", vegTypeItems="
				+ vegTypeItems + ", vegType=" + vegType + ", vegTypeOn="
				+ vegTypeOn + ", vegTypeDesc=" + vegTypeDesc + ", pnvgItems="
				+ pnvgItems + ", pnvgs=" + Arrays.toString(pnvgs) + ", bps="
				+ Arrays.toString(bps) + ", fireRegimeResultsSQL="
				+ fireRegimeResultsSQL + "]";
	}

	public String getSppAcronym() {
		return sppAcronym;
	}

	public void setSppAcronym(String sppAcronym) {
		this.sppAcronym = sppAcronym;
	}

}
