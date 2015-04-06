package com.fabiosalvini.spatialhierarchybuilder.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBAccess {

	private static final Logger LOG = Logger.getLogger(DBAccess.class.getName());
	private static final String DBCLASSNAME = "org.postgresql.Driver";
	private static final String CONNECTION = "jdbc:postgresql://localhost/result2";

	/**
	 * Get the connection to the database
	 * @return the connection or null if errors occured
	 */
	public static Connection getConnection() {
		try {
			Class.forName(DBCLASSNAME);
		} catch (ClassNotFoundException e) {
			LOG.warning(e.fillInStackTrace().toString());
			return null;
		}

		Connection con;
		try	{
			con = DriverManager.getConnection(CONNECTION, "postgres", "#_postgres_&");
		} catch (SQLException e) {
			LOG.warning(e.fillInStackTrace().toString());
			return null;
		} 

		return con;
	}


}
