package com.FEIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

public class DbAccess {

	private String SQLString = "";
	private String errMessage = "";
	private final Logger log = Logger.getLogger(this.getClass().getName());

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String newValue) {
		errMessage = newValue;
	}

	public DbAccess(String SQLStr) {
		SQLString = SQLStr;
		log.info("CTOR : " + SQLStr);

	}

	public boolean ExecuteUpdate() {
		boolean resultOK = false;

		try {
			InitialContext cxt = new InitialContext();

			DataSource ds = (DataSource) cxt
					.lookup("java:/comp/env/jdbc/postgres");
			if (ds == null) {
				log.info("Data Source Not Found:java:/comp/env/jdbc/postgres");
				throw new Exception("Data source not found!");
			}
			Connection conn = ds.getConnection();
			try {
				Statement stmt = conn.createStatement();
				int result = stmt.executeUpdate(SQLString);
				if (result == 1)
					resultOK = true;

			} finally {
				conn.close();
			}

		} catch (Exception e) {
			log.info("Execute Update: " + e.getMessage());
			e.printStackTrace();

		}
		return resultOK;

	}

	public ResultSet ExecuteQuery() {
		CachedRowSet crs = null;

		try {
			InitialContext cxt = new InitialContext();
			DataSource ds = (DataSource) cxt
					.lookup("java:/comp/env/jdbc/postgres");
			if (ds == null) {
				log.info("Data Source Not Found - java:/comp/env/jdbc/postgres");
				errMessage = ("Data Source Not Found - java:/comp/env/jdbc/postgres");
				throw new Exception("Data source not found!");
			}
			Connection conn = ds.getConnection();
			try {

				Statement stmt = conn.createStatement();

				log.info("BEGIN : Query: " + SQLString);
				long before = System.currentTimeMillis();
				ResultSet result = stmt.executeQuery(SQLString);
				long duration = System.currentTimeMillis() - before;
				log.info("END : Query: " + SQLString + " : took " + duration
						+ " ms");
				crs = new com.sun.rowset.CachedRowSetImpl();
				crs.populate(result);

				return crs;

			} catch (SQLException e) {

				log.info("Execute Query 1: " + e.getMessage());
				log.log(Level.INFO, "error caught in query", e);
				errMessage = e.getMessage();
				System.out.println(errMessage);
			} catch (Exception e) {
				log.info("Execute Query 2: " + e.getMessage());
				log.info(e.getMessage());
				errMessage = e.getMessage();

			} finally {
				conn.close();
			}

		} catch (Exception e) {
			log.info("Execute Query: " + e.getMessage());
			log.info(e.getMessage());
			errMessage = e.getMessage();

		}
		return crs;

	}

	public ResultSet ExecuteQuery(Object[] params) {
		CachedRowSet crs = null;

		try {
			InitialContext cxt = new InitialContext();
			DataSource ds = (DataSource) cxt
					.lookup("java:/comp/env/jdbc/postgres");
			if (ds == null) {
				log.info("Data Source Not Found - java:/comp/env/jdbc/postgres");
				errMessage = ("Data Source Not Found - java:/comp/env/jdbc/postgres");
				throw new Exception("Data source not found!");
			}
			Connection conn = ds.getConnection();
			try {
				PreparedStatement ps = conn.prepareStatement(SQLString);
				int i = 1;
				for (Object object : params) {
					ps.setObject(i++, object);
				}

				log.info("BEGIN : Query: " + SQLString);
				long before = System.currentTimeMillis();
				ResultSet result = ps.executeQuery();
				long duration = System.currentTimeMillis() - before;
				log.info("END : Query: " + SQLString + " : took " + duration
						+ " ms");
				crs = new com.sun.rowset.CachedRowSetImpl();
				crs.populate(result);

				return crs;

			}

			catch (SQLException e) {
				log.info("Execute Query 1: " + e.getMessage());
				log.log(Level.INFO, "error caught in query", e);
				errMessage = e.getMessage();
				System.out.println(errMessage);
			} catch (Exception e) {
				log.info("Execute Query 2: " + e.getMessage());
				log.info(e.getMessage());
				errMessage = e.getMessage();

			} finally {
				conn.close();
			}

		} catch (Exception e) {
			log.info("Execute Query: " + e.getMessage());
			log.info(e.getMessage());
			errMessage = e.getMessage();

		}
		return crs;

	}

}
