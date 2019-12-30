package com.FEIS;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

public class FireRegimeQueryBuilderTest {
	
	
	static { 
        try {
			// Create initial context
			System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
			    "org.apache.naming.java.javaURLContextFactory");
			System.setProperty(Context.URL_PKG_PREFIXES, 
			    "org.apache.naming");            
			InitialContext ic = new InitialContext();

			ic.createSubcontext("java:");
			ic.createSubcontext("java:/comp");
			ic.createSubcontext("java:/comp/env");
			ic.createSubcontext("java:/comp/env/jdbc");

			// Construct DataSource
			PGSimpleDataSource ds = new PGSimpleDataSource();
      
			ds.setDatabaseName("XXXX");

			ds.setServerName("XXX.XX.XXX.XX");

			ds.setUser("XXXX"); //<-- self explanatory
			ds.setPassword("XXXX"); //<-- self explanatory

			ic.bind("java:/comp/env/jdbc/postgres", ds); //<--insert name of binding here
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test 
	public void testbuildHomeNameQuery() throws Exception { 
		
		FireRegimeQueryBuilder frqb = new FireRegimeQueryBuilder();
		frqb.setFireRegimeName("acacia");
		frqb.setByName(true);
		String q = frqb.buildHomeNameQuery();
		System.out.println(q);
 		DbAccess dba = new DbAccess(q);
 		ResultSet qo = dba.ExecuteQuery();
 		dumpResults(qo);
 		int rows = 1; 
		while(qo.next()) { 
			rows++;
		}
	
		assertEquals(8, rows);

	}
	
	@Test 
	public void testbuildNameQuery() throws Exception {
		FireRegimeQueryBuilder frqb = new FireRegimeQueryBuilder();
		frqb.setFireRegimeName("acacia,elm");
		frqb.setByName(true);
		String q = frqb.buildNameQuery();
		System.out.println(q);
 		DbAccess dba = new DbAccess(q);
 		ResultSet qo = dba.ExecuteQuery();
 		dumpResults(qo);
 		int rows = 1; 
		while(qo.next()) { 
			rows++;
		}
	
		assertEquals(46, rows);
		
		
		frqb = new FireRegimeQueryBuilder();
		frqb.setFireRegimeName("acacia,elm");
		frqb.setInputTitle("i");
		frqb.setByName(true);
		q = frqb.buildNameQuery();
		System.out.println(q);
 		  dba = new DbAccess(q);
 		  qo = dba.ExecuteQuery();
 		dumpResults(qo);
 		  rows = 1; 
		while(qo.next()) { 
			rows++;
		}
	
		assertEquals(14, rows);
		
		
		frqb = new FireRegimeQueryBuilder();
		frqb.setFireRegimeName("acacia,elm");
		frqb.setInputTitle("i");
		frqb.setByName(true);
		frqb.setByState(true);
		String[] states = new String[] {  "TX" };
		frqb.setStates(states);
		q = frqb.buildNameQuery();
		System.out.println(q);
 		  dba = new DbAccess(q);
 		  qo = dba.ExecuteQuery();
 		dumpResults(qo);
 		  rows = 1; 
		while(qo.next()) { 
			rows++;
		}
	
		assertEquals(8, rows);
		
		
		
		frqb = new FireRegimeQueryBuilder();
		frqb.setFireRegimeName("acacia,elm");
		frqb.setInputTitle("i");
		frqb.setByName(true);
		frqb.setByState(true);
		states = new String[] {  "TX" };
		frqb.setStates(states);
		
		frqb.setByPnvg(true);
		
		frqb.setPnvgs("AB");
		
		
		q = frqb.buildNameQuery();
		System.out.println(q);
 		  dba = new DbAccess(q);
 		  qo = dba.ExecuteQuery();
 		dumpResults(qo);
 		  rows = 1; 
		while(qo.next()) { 
			rows++;
		}
	
		assertEquals(8, rows);
		
		
		
		
	}

	@Test
	public void testBuildGeoQueryByState() throws Exception  {
	
		FireRegimeQueryBuilder frqb = new FireRegimeQueryBuilder();
//		frqb.setByState(true);
//		frqb.setStates(new String[] { "WA", "CA" });
 	String q = frqb.buildGeoQuery();
//		System.out.println(q);
 		DbAccess dba = new DbAccess(q);
 		ResultSet qo = dba.ExecuteQuery();
		int rows = 0; 
//		dumpResults(qo);
//		while(qo.next()) { 
//			rows++;
//			String state = qo.getString("STATE");
//			assertTrue("WA".equals(state) || "CA".equals(state));
//		}
//		
//		assertEquals(49, rows);
//		
//		frqb = new FireRegimeQueryBuilder();
//		
//		frqb.setByState(true);
//		frqb.setStates(new String[] { "WA", "CA" });
//		frqb.setByPnvg(true);
//		
//		frqb.setBps("357");
//		frqb.setPlantCommunities("7617320");
//		
//		q = frqb.buildGeoQuery();
//		System.out.println(q);
//		dba = new DbAccess(q);
//		qo = dba.ExecuteQuery();
//		rows = 0; 
//		dumpResults(qo);
//		while(qo.next()) { 
//			rows++;
//			String state = qo.getString("STATE");
//			assertTrue("WA".equals(state) || "CA".equals(state));
//		}
//		
//		assertEquals(47, rows);
//		
//		
//		
		frqb = new FireRegimeQueryBuilder();
		
		frqb.setByState(true);
		frqb.setStates(new String[] { "WA", "CA" });
		frqb.setByPnvg(true);
		
		frqb.setBps("357");
		frqb.setPlantCommunities("7617320");
		
		frqb.setByMap(true);
		
		frqb.setInputLatitude("lat");
		frqb.setInputLongitude("long");
		
		
		q = frqb.buildGeoQuery();
		System.out.println(q);
		dba = new DbAccess(q);
		qo = dba.ExecuteQuery();
		rows = 0; 
		dumpResults(qo);
		while(qo.next()) { 
			rows++;
			String state = qo.getString("STATE");
			assertTrue("WA".equals(state) || "CA".equals(state));
		}
		
		assertEquals(47, rows);
		
		
		
	}

	private void dumpResults(ResultSet qo)  throws Exception{
		ResultSetMetaData metaData = qo.getMetaData();
		
		for(int i = 1; i <= metaData.getColumnCount(); i++) { 
			System.out.print(metaData.getColumnName(i) + " \t|\t ");
		}
		System.out.println("\n");
		while(qo.next()) {
			for(int i = 1; i <= metaData.getColumnCount(); i++) { 
				System.out.print(qo.getObject(metaData.getColumnName(i)) + " \t|\t ");
			}
			System.out.println("\n");
		}
		qo.absolute(1);
	}
	
	@Test public void testJustBPS()  throws Exception {
		
		FireRegimeQueryBuilder frqb = new FireRegimeQueryBuilder();
		
	 
		frqb.setByPnvg(true);
		frqb.setBps("357");
		frqb.setPlantCommunities("7617320");
		DbAccess dba = new DbAccess(frqb.buildNameQuery());
		ResultSet qo = dba.ExecuteQuery();
		int rows = 0; 
		dumpResults(qo);
		
	}
	
	
	@Test public void testJustAgency()  throws Exception {
		
		FireRegimeQueryBuilder frqb = new FireRegimeQueryBuilder();
		
		String[] unitNames = { "827","837","843" };
		 
		frqb.setByAdminUnit(true);
		frqb.setUnitNames(unitNames);
		frqb.setPlantCommunities("7617320");
		DbAccess dba = new DbAccess(frqb.buildNameQuery());
		ResultSet qo = dba.ExecuteQuery();
		int rows = 0; 
		dumpResults(qo);
		
	}
	
	
}
