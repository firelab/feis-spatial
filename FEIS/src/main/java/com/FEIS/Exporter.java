package com.FEIS;

import com.FEIS.types.FireRegime;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * Export table data to csv 
 */
@ManagedBean(name = "exporterBean")
@SessionScoped
public class Exporter implements Serializable {

	private static final String FIREREGIME_EXPLANATION =
			"Summary information on historical fire regimes. If a link is available in Column 3, you can use it to obtain a synthesis of information on the historical fire regime and current changes in fuels and fire regimes. Columns 4 and 5, Min and Max MFRI, are the shortest and longest Mean Fire Return Intervals (years) from the literature if a synthesis is available, from LANDFIRE succession modeling if not. The next 8 columns are derived from LANDFIRE succession models of Biophysical Settings (BpSs), which are based on literature, local data, and/or expert estimates. If a synthesis is not yet available, Columns 4 and 5 are identical to Columns 6 and 7. Columns  8 through 13  are the least and greatest percentages of historical fires in 3 fire severity classes. The remaining columns are the number of BpSs in each LANDFIRE Fire Regime Group. See the FEIS glossary (http://www.fs.fed.us/database/feis/glossary2.html) for definitions.";
	
	public static final long serial = 1L;

	private String componentID;
	private String description;

	public Exporter() {
	}

	public String getComponentID() {
		return componentID;
	}

	public void setComponentID(String componentID) {
		this.componentID = componentID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * export table string buffer to csv file
	 */
	public void exportTable(StringBuffer reviewResults, String fileName) {
		try {

			byte[] csvData = reviewResults.toString().getBytes("windows-1252");
			// byte[] csvData = reviewResults.toString().getBytes();
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			HttpServletResponse response = (HttpServletResponse) externalContext
					.getResponse();
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileName);
			response.setContentLength(reviewResults.length());
			response.setContentType("application/CSV");

			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(csvData);
			outputStream.flush();
			outputStream.close();
			context.responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public StringBuffer populateReviewList(ResultSet results) {
		StringBuffer sb = new StringBuffer();
		String descFormatted = "";

		if (!("".equals(this.description) || this.description == null)) {
			sb.append("Results for: ");
			descFormatted = checkString(this.description);
			sb.append(descFormatted);
			sb.append("\n");
		}
		sb.append("Acronym");
		sb.append(",");
		sb.append("Link");
		sb.append(",");
		sb.append("Scientific Name");
		sb.append(",");
		sb.append("Common Name");
		sb.append(",");
		sb.append("Review Date");
		sb.append(",");
		sb.append("Fire Study Availability");
		sb.append(",");
		sb.append("Fire Regime Availability");

		sb.append("\n");

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
						// Acronym
						String acronym = results.getString("ACRONYM");
						if (acronym != null) {
							sb.append(acronym);
							sb.append(",");
						} else {
							continue;
						}
						// Link
						String link = results.getString("HTML_PATH");
						if (link != null) {
							sb.append(link);
							sb.append(",");
						} else {
							sb.append("");
							sb.append(",");
						}
						// Scientific Name
						String sciname = results.getString("SCITITLE");
						if (sciname != null) {
							sb.append(commaProtect(sciname));
							sb.append(",");
						} else {
							sb.append("");
							sb.append(",");
						}
						// Common Name
						String commonName = results.getString("COMTITLE");
						if (commonName != null) {
							sb.append(commaProtect(commonName));

							sb.append(",");
						} else {
							sb.append("");
							sb.append(",");
						}
						// Year Written
						String yearWritten = results.getString("YEAR_WRITTEN");
						String availableFS = results
								.getString("FIRESTUDY_HTML_PATH");
						if (yearWritten != null) {
							sb.append(yearWritten);
							sb.append(",");
							if (availableFS != null) {
								sb.append("Available");
								sb.append(",");
							} else {
								sb.append("Not Available");
								sb.append(",");
							}
						} else {
							sb.append("");
							sb.append(",");
						}

						long fireRegimeRefCountForSpecies = results
								.getLong("FR_REFERENCED_COUNT");
						if (fireRegimeRefCountForSpecies > 0) {
							sb.append("Available");
						} else {
							sb.append("Not Available");
						}

						sb.append("\n");

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb;
	}

	private String commaProtect(String sciname) {
		return sciname.replaceAll(",", ";");
	}

	public StringBuffer populateFireRegimeList(List<FireRegime> frList) {
		StringBuffer sb = new StringBuffer();
		String descFormatted = "";

		if (!("".equals(this.description) || this.description == null)) {
			sb.append("Results for: ");
			descFormatted = checkString(this.description);
			sb.append(descFormatted);
			sb.append("\n");
		}
		sb.append("Region");
		sb.append(",");
		 
		sb.append("Fire regime title");
		sb.append(",");
		sb.append("Link");
		sb.append(",");
		sb.append("Date");
		sb.append(",");
		sb.append("Shortest MFRI (yr)");
		sb.append(",");
		sb.append("Longest MFRI (yr)");
		sb.append(",");
		sb.append("From BpS model: shortest MFRI (yr)");
		sb.append(",");
		sb.append("From BpS model: longest MFRI (yr)");
		sb.append(",");
		sb.append("From BpS model: min % replacement");
		sb.append(",");
		sb.append("From BpS model: max % replacement");
		sb.append(",");
		sb.append("From BpS model: min % mixed");
		sb.append(",");
		sb.append("From BpS model: max % mixed");
		sb.append(",");

		sb.append("From BpS model: min % low");
		sb.append(",");
		sb.append("From BpS model: max % low");
		sb.append(",");
		sb.append("BpSs in fire regime group I");
		sb.append(",");
		sb.append("BpSs in fire regime group II");
		sb.append(",");
		sb.append("BpSs in fire regime group III");
		sb.append(",");
		sb.append("BpSs in fire regime group IV");
		sb.append(",");
		sb.append("BpSs in fire regime group V");
		sb.append(",");
		sb.append("BpS models with no fire included");
		sb.append(",");
		sb.append("BpS models & data");
		sb.append("\n");

		for (FireRegime fr : frList) {
			
		
				
				sb.append(get(fr.getBpsRegion())+",");
				sb.append(get(fr.getTitle())+",");
				sb.append(get(fr.getHtmlPath())+",");
				sb.append(get(fr.getYearWritten())+",");
				sb.append(getNone(fr.getFeisMinInterval())+",");
				sb.append(getNone(fr.getFeisMaxInterval())+",");
				sb.append(get(fr.getBpsMinInterval())+",");
				sb.append(get(fr.getBpsMaxInterval())+",");
				sb.append(get(fr.getMinReplace())+",");
				sb.append(get(fr.getMaxReplace())+",");
				sb.append(get(fr.getMinMixed())+",");
				sb.append(get(fr.getMaxMixed())+",");
				sb.append(get(fr.getMinLow())+",");
				sb.append(get(fr.getMaxLow())+",");
				sb.append(get(fr.getFrg1Count())+",");
				sb.append(get(fr.getFrg2Count())+",");
				sb.append(get(fr.getFrg3Count())+",");
				sb.append(get(fr.getFrg4Count())+",");
				sb.append(get(fr.getFrg5Count())+",");
				sb.append(get(fr.getFrgNACount())+",");
				sb.append(get(fr.getHtmlPathAppendixA())+"");
				sb.append("\n");

		}
		sb.append("\n");
		sb.append("\n");
		sb.append("\"" + FIREREGIME_EXPLANATION + "\"");
		return sb;
	}

	private String get(Object o) {
		return o != null ? o.toString().replace(",", " ") : "";
	}
	private String getNone(Object o) {
		return o != null ? o.toString().replace(",", " ") : "none";
	}

	private Object getString(String name, ResultSet results, boolean last)
			throws SQLException {
		String string = results.getString(name);
		String ret = "";
		if (string != null) {
			ret += string;
		}
		if (!last) {
			ret += ", ";
		}
		return ret;
	}

	private String getLong(String name, ResultSet results, boolean last)
			throws SQLException {
		long ret = results.getLong(name);

		if (!last) {
			return ret + ", ";
		}
		return ret + "";
	}

	private String getDouble(String name, ResultSet results, boolean last)
			throws SQLException {
		double ret = results.getDouble(name);

		if (!last) {
			return ret + ", ";
		}
		return ret + "";
	}

	public StringBuffer populateFireStudyList(ResultSet results) {
		StringBuffer sb = new StringBuffer();
		String descFormatted = "";

		if (!("".equals(this.description) || this.description == null)) {
			sb.append("Results for: ");
			descFormatted = checkString(this.description);
			sb.append(descFormatted);
			sb.append("\n");
		}
		sb.append("Location");
		sb.append(",");
		sb.append("Summarizes research by...");
		sb.append(",");
		sb.append("Link");
		sb.append(",");
		sb.append("Title");
		sb.append(",");
		sb.append("Species Review Availability");
		sb.append(",");
		sb.append("Fire Regime Availability");
		 
		sb.append("\n");

		if (results != null) {
			try {
				results.beforeFirst();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Map<String, Map<String, String>> rows = new LinkedHashMap<String, Map<String, String>>();
				while (results.next()) {
					try {
						String fsId = results.getString("FS_ID");
						Map<String, String> row = rows.get(fsId);
						if (row == null) {
							row = new HashMap<String, String>();
						}

						// Location
						String location = results.getString("ABBR");
						String abbr = (String) row.get("ABBR");
						if (abbr != null && !abbr.toString().contains(location)) {
							abbr += " " + location;
						} else {
							abbr = location;
						}
						row.put("ABBR", abbr);
						row.put("FS_AUTHORS", results.getString("FS_AUTHORS"));
						row.put("TITLE", results.getString("TITLE"));
						row.put("HTML_PATH", results.getString("HTML_PATH"));
						row.put("ACRONYM", results.getString("ACRONYM"));
						row.put("YEAR_WRITTEN",
								results.getString("YEAR_WRITTEN"));
					//	row.put("TYPE", results.getString("TYPE"));
						row.put("FR_REFERENCED_COUNT",
								results.getLong("FR_REFERENCED_COUNT") + "");
						String[] blankCheck = new String[] { location,
								row.get("AUTHOR"), row.get("TITLE"),
								row.get("HTML_PATH"), row.get("ACRONYM"),
								row.get("YEAR_WRITTEN") };
						if (allAreBlank(blankCheck)) {
							continue;
						} else {
							rows.put(fsId, row);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				for (Map<String, String> row : rows.values()) {

					appendCsv(sb, row.get("ABBR"));
					appendCsv(sb, row.get("FS_AUTHORS"));
					appendCsv(sb, row.get("HTML_PATH"));
					appendCsv(sb, row.get("TITLE"));
					if (row.get("ACRONYM") != null) {
						sb.append("Available");
						sb.append(",");
					} else {
						sb.append("");
						sb.append(",");
					}
					String rc = row
							.get("FR_REFERENCED_COUNT");
					long fireRegimeRefCountForSpecies = 0; 
					if(rc != null) { 
						fireRegimeRefCountForSpecies = new Long(rc);
					}
					if (fireRegimeRefCountForSpecies > 0) {
						sb.append("Available,");
					} else {
						sb.append(",");
					}
					sb.append("\n");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb;
	}

	private void appendCsv(StringBuffer sb, String string) {
		// TODO Auto-generated method stub
		if (string != null) {
			string = string.replace(",", "");
			sb.append(string);
			sb.append(",");
		} else {
			sb.append("");
			sb.append(",");
		}
	}

	private boolean allAreBlank(String[] strings) {
		for (String string : strings) {
			if (string != null && string.trim().length() > 0) {
				return false;
			}
		}
		return true;
	}

	protected String checkString(String descString) {
		// add escape for apostrophe's
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				descString);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == ',') {
				result.append(" ");
			} else
				result.append(character);
			character = iterator.next();
		}
		return result.toString();
	}

		
	
}
