package com.FEIS.bps;

import com.FEIS.DbAccess;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ManagedBean(name = "bpsBean")
@SessionScoped
public class BpsJsonBean implements Serializable {
	
	String bpsTable = null;
	int bpsId = 0; 
	String bpsJson = null;
	
	List<BPS> bps = new ArrayList<BPS>();
	
	private static final String[] tables = {
		"bps_ak",
		"bps_hi", 
		"bps_us" 
	};
	
	public String lookupBpsJson() throws Exception {
		return null;
	}
	
	
	
	public String getBpsTable() {
		return bpsTable;
	}



	public void setBpsTable(String bpsTable) {
		this.bpsTable = bpsTable;
		updateBpsList(); 
	}



	private void updateBpsList() {
		
		 
		if(Arrays.binarySearch(tables, bpsTable) < 0) {
			return; 
		}
		String query = "select id, bps_name from " + bpsTable + " order by 2";
		
		DbAccess dba = new DbAccess(query);
		
		ResultSet rs = dba.ExecuteQuery();
		
		bps.clear();
		
		try {
			while(rs.next()) {
				bps.add(new BPS(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}



	public int getBpsId() {
		return bpsId;
	}



	public void setBpsId(int bpsId) {
		this.bpsId = bpsId;
		updateBpsJson();
	}



	private void updateBpsJson() {
		String query = "select ST_AsGeoJSON(\"the_geom\"::geometry, 9, 8) from " + bpsTable  +
				" where \"id\" = ?";

		DbAccess dba = new DbAccess(query);
		
		ResultSet rs = dba.ExecuteQuery(new Object[] { bpsId } );
	
		try {
			while(rs.next()) { 
				bpsJson = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}



	public String getBpsJson() {
		return bpsJson;
	}



	public void setBpsJson(String bpsJson) {
		this.bpsJson = bpsJson;
	}



	public List<BPS> getBps() {
		return bps;
	}



	public void setBps(List<BPS> bps) {
		this.bps = bps;
	}
}
