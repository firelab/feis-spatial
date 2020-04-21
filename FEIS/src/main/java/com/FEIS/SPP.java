/**
 * SPP class represent species.
 */
package com.FEIS;

/**
 * @author limei.piao
 *
 */
import java.io.Serializable;
import java.util.ArrayList;

public class SPP implements Serializable {
	private String fsID = "";
	private String speciesHasFireRegimes = null;
	private Long speciesFireRegimeCount = 0l;
	public Long getSpeciesFireRegimeCount() {
		return speciesFireRegimeCount;
	}

	public void setSpeciesFireRegimeCount(Long speciesFireRegimeCount) {
		this.speciesFireRegimeCount = speciesFireRegimeCount;
	}

	private String acronym = "";
	private int speciesID = -1;
	private String scientificName = "";
	private String commonName = "";
	private String reviewDate = "";
	private String linkToSpeciesReview = "";
	private String linkNameToSpeciesReview = "";
	private String linkToPDFFile = "";
	private ArrayList<FireStudy> fireStudyList;

	// new stuff
	private String fireStudyTitle = "";
	private String fireStudyTitle2 = "";
	private String fireStudyLink = "";
	//

	// experimental
	private String allFS;
	private String allFSTitle;
	private String allFSLink;

	public String getAllFS() {
		return allFS;
	}

	public void setAllFS(String newValue) {
		allFS = newValue;
	}

	public String getAllFSTitle() {
		return allFSTitle;
	}

	public void setAllFSTitle(String newValue) {
		allFSTitle = newValue;
	}

	public String getAllFSLink() {
		return allFSLink;
	}

	public void setAllFSLink(String newValue) {
		allFSLink = newValue;
	}

	//

	public SPP() {
	}

	public String getFireStudyTitle2() {
		return fireStudyTitle2;
	}

	public void setFireStudyTitle2(String newValue) {
		fireStudyTitle2 = newValue;
	}

	public String getFireStudyTitle() {
		return fireStudyTitle;
	}

	public void setFireStudyTitle(String newValue) {
		fireStudyTitle = newValue;
	}

	public String getFireStudyLink() {
		return fireStudyLink;
	}

	public void setFireStudyLink(String newValue) {
		fireStudyLink = newValue;
	}

	//

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String newValue) {
		acronym = newValue;
	}

	public String getFSID() {
		return fsID;
	}

	public void setFSID(String newValue) {
		fsID = newValue;
	}

	public int getSpeciesID() {
		return speciesID;
	}

	public void setSpeciesID(int newValue) {
		speciesID = newValue;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String newValue) {
		scientificName = newValue;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String newValue) {
		commonName = newValue;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(String newValue) {
		reviewDate = newValue;
	}

	public String getLinkToSpeciesReview() {
		return linkToSpeciesReview;
	}

	public void setLinkToSpeciesReview(String newValue) {
		linkToSpeciesReview = newValue;
	}

	public String getNameToSpeciesReview() {
		return linkNameToSpeciesReview;
	}

	public void setNameToSpeciesReview(String newValue) {
		linkNameToSpeciesReview = newValue;
	}

	public String getLinkToPDFFile() {
		return linkToPDFFile;
	}

	public void setLinkToPDFFile(String newValue) {
		linkToPDFFile = newValue;
	}

	public ArrayList<FireStudy> getFireStudyList() {
		return fireStudyList;
	}

	public void setFireStudyList(ArrayList<FireStudy> newFSList) {
		fireStudyList = newFSList;
	}

	public String getSpeciesHasFireRegimes() {
		return speciesHasFireRegimes;
	}

	public void setSpeciesHasFireRegimes(String speciesHasFireRegimes) {
		this.speciesHasFireRegimes = speciesHasFireRegimes;
	}

}
