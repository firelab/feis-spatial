package com.FEIS.types;

import java.io.Serializable;

public class FireRegime implements Serializable {

	private static final long serialVersionUID = 3616835418081252692L;

	long frId;
	String bpsRegion;
	String bpsName;
	long bpsRegionId;
	String title;
	String htmlPath;
	String htmlPathAppendixA;
	String monthWritten;
	String yearWritten;
	long quality;
	long feisTaxaCount;
	long nonFeisTaxaCount;
	long pageCount;
	long citationCount;
	String notes;
	boolean entryComplete;
	String fullTitle;

	double bpsMinInterval;
	double bpsMaxInterval;

	Integer feisMinInterval;
	Integer feisMaxInterval;

	double minReplace;
	double maxReplace;

	double minMixed;
	double maxMixed;

	double minLow;
	double maxLow;

	long frg1Count;
	long frg2Count;
	long frg3Count;
	long frg4Count;
	long frg5Count;
	
	long fireStudyCount; 
	long reviewCount; 
	
	
	public long getFrg5Count() {
		return frg5Count;
	}
	public void setFrg5Count(long frg5Count) {
		this.frg5Count = frg5Count;
	}
	long frgNACount;
	
	public long getFrId() {
		return frId;
	}
	public void setFrId(long frId) {
		this.frId = frId;
	}
	public String getBpsRegion() {
		return bpsRegion;
	}
	public void setBpsRegion(String bpsRegion) {
		this.bpsRegion = bpsRegion;
	}
	public long getBpsRegionId() {
		return bpsRegionId;
	}
	public void setBpsRegionId(long bpsRegionId) {
		this.bpsRegionId = bpsRegionId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHtmlPath() {
		return htmlPath;
	}
	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}
	public String getHtmlPathAppendixA() {
		return htmlPathAppendixA;
	}
	public void setHtmlPathAppendixA(String htmlPathAppendix) {
		this.htmlPathAppendixA = htmlPathAppendix;
	}
	public String getMonthWritten() {
		return monthWritten;
	}
	public void setMonthWritten(String monthWritten) {
		this.monthWritten = monthWritten;
	}
	public String getYearWritten() {
		return yearWritten;
	}
	public void setYearWritten(String yearWritten) {
		this.yearWritten = yearWritten;
	}
	public long getQuality() {
		return quality;
	}
	public void setQuality(long quality) {
		this.quality = quality;
	}
	public long getFeisTaxaCount() {
		return feisTaxaCount;
	}
	public void setFeisTaxaCount(long feisTaxaCount) {
		this.feisTaxaCount = feisTaxaCount;
	}
	public long getNonFeisTaxaCount() {
		return nonFeisTaxaCount;
	}
	public void setNonFeisTaxaCount(long nonFeisTaxaCount) {
		this.nonFeisTaxaCount = nonFeisTaxaCount;
	}
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	public long getCitationCount() {
		return citationCount;
	}
	public void setCitationCount(long citationCount) {
		this.citationCount = citationCount;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public boolean isEntryComplete() {
		return entryComplete;
	}
	public void setEntryComplete(boolean entryComplete) {
		this.entryComplete = entryComplete;
	}
	public String getFullTitle() {
		return fullTitle;
	}
	public void setFullTitle(String fullTitle) {
		this.fullTitle = fullTitle;
	}
	public double getBpsMinInterval() {
		return bpsMinInterval;
	}
	public void setBpsMinInterval(double bpsMinInterval) {
		this.bpsMinInterval = bpsMinInterval;
	}
	public double getBpsMaxInterval() {
		return bpsMaxInterval;
	}
	public void setBpsMaxInterval(double bpsMaxInterval) {
		this.bpsMaxInterval = bpsMaxInterval;
	}
	public Integer getFeisMinInterval() {
		return feisMinInterval;
	}
	public void setFeisMinInterval(Integer feisMinInterval) {
		this.feisMinInterval = feisMinInterval;
	}
	public Integer getFeisMaxInterval() {
		return feisMaxInterval;
	}
	public void setFeisMaxInterval(Integer feisMaxInterval) {
		this.feisMaxInterval = feisMaxInterval;
	}
	public double getMinReplace() {
		return minReplace;
	}
	public void setMinReplace(double minReplace) {
		this.minReplace = minReplace;
	}
	public double getMaxReplace() {
		return maxReplace;
	}
	public void setMaxReplace(double maxReplace) {
		this.maxReplace = maxReplace;
	}
	public double getMinMixed() {
		return minMixed;
	}
	public void setMinMixed(double minMixed) {
		this.minMixed = minMixed;
	}
	public double getMaxMixed() {
		return maxMixed;
	}
	public void setMaxMixed(double maxMixed) {
		this.maxMixed = maxMixed;
	}
	public double getMinLow() {
		return minLow;
	}
	public void setMinLow(double minLow) {
		this.minLow = minLow;
	}
	public double getMaxLow() {
		return maxLow;
	}
	public void setMaxLow(double maxLow) {
		this.maxLow = maxLow;
	}
	public long getFrg1Count() {
		return frg1Count;
	}
	public void setFrg1Count(long frg1Count) {
		this.frg1Count = frg1Count;
	}
	public long getFrg2Count() {
		return frg2Count;
	}
	public void setFrg2Count(long frg2Count) {
		this.frg2Count = frg2Count;
	}
	public long getFrg3Count() {
		return frg3Count;
	}
	public void setFrg3Count(long frg3Count) {
		this.frg3Count = frg3Count;
	}
	public long getFrg4Count() {
		return frg4Count;
	}
	public void setFrg4Count(long frg4Count) {
		this.frg4Count = frg4Count;
	}
	public long getFrgNACount() {
		return frgNACount;
	}
	public void setFrgNACount(long frgNACount) {
		this.frgNACount = frgNACount;
	}
	public long getFireStudyCount() {
		return fireStudyCount;
	}
	public void setFireStudyCount(long fireStudyCount) {
		this.fireStudyCount = fireStudyCount;
	}
	public long getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(long reviewCount) {
		this.reviewCount = reviewCount;
	}
	public String getBpsName() {
		return bpsName;
	}
	public void setBpsName(String bpsName) {
		this.bpsName = bpsName;
	}
	 
	 

}
