/**
 * Class FireStudy Review Results 
 */
package com.FEIS;

/**
 * @author limei.piao
 * Update: Axiom IT Solutions
 *
 */
import java.io.Serializable;

public class FireStudy implements Serializable {

	private String speciesHasFireRegimes = null;
	private Long speciesFireRegimeCount = 0l;

	private String fsID = "";
	private String title = "";
	private String studyDate = "";
	private String linkToFireStudy = "";
	private String linkToPDFFireStudy = "";
	private String reportType = "";
	private String speciesTitle = "";
	private String speciesTitle2 = "";
	private String author = "";
	private String state = "";
	private String linkToSpeciesReview = "";
	private String acronym = "";

	private int speciesId; 
	
	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String newValue) {
		acronym = newValue;
	}

	public FireStudy() {
		//
		// TODO: Add constructor logic here
		//
	}

	// new stuff
	public String getFSID() {
		return fsID;
	}

	public void setFSID(String newValue) {
		fsID = newValue;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String newValue) {
		author = newValue;
	}

	public String getState() {
		return state;
	}

	public void setState(String newValue) {
		state = newValue;
	}

	public String getLinkToSpeciesReview() {
		return linkToSpeciesReview;
	}

	public void setLinkToSpeciesReview(String newValue) {
		linkToSpeciesReview = newValue;
	}

	public String getSpeciesTitle() {
		return speciesTitle;
	}

	public void setSpeciesTitle(String newValue) {
		speciesTitle = newValue;
	}

	public String getSpeciesTitle2() {
		return speciesTitle2;
	}

	public void setSpeciesTitle2(String newValue) {
		speciesTitle2 = newValue;
	}

	// end new stuff

	public String getTitle() {
		return title;
	}

	public void setTitle(String newValue) {
		title = newValue;
	}

	public String getStudyDate() {
		return studyDate;
	}

	public void setStudyDate(String newValue) {
		studyDate = newValue;
	}

	public String getLinkToFireStudy() {
		return linkToFireStudy;
	}

	public void setLinkToFireStudy(String newValue) {
		linkToFireStudy = newValue;
	}

	public String getLinkToPDFStudy() {
		return linkToPDFFireStudy;
	}

	public void setLinkToPDFStudy(String newValue) {
		linkToPDFFireStudy = newValue;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String newValue) {
		reportType = newValue;
	}

	public String getSpeciesHasFireRegimes() {
		return speciesHasFireRegimes;
	}

	public void setSpeciesHasFireRegimes(String speciesHasFireRegimes) {
		this.speciesHasFireRegimes = speciesHasFireRegimes;
	}

	public Long getSpeciesFireRegimeCount() {
		return speciesFireRegimeCount;
	}

	public void setSpeciesFireRegimeCount(Long speciesFireRegimeCount) {
		this.speciesFireRegimeCount = speciesFireRegimeCount;
	}

	public int getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(int speciesId) {
		this.speciesId = speciesId;
	}

}
