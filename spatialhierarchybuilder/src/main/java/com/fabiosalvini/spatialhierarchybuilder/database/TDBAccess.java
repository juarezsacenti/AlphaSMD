package com.fabiosalvini.spatialhierarchybuilder.database;

import java.util.logging.Logger;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.tdb.TDBException;
import com.hp.hpl.jena.tdb.TDBFactory;

public class TDBAccess {

	private static final Logger LOG = Logger.getLogger(TDBAccess.class.getName());
	private static final String TDBCLASSNAME = "com.hp.hpl.jena.tdb.TDBFactory";
	private static final String TDBDIRECTORY = "TDB/";

	public static Dataset getConnection() {
		return getConnection("Default");
	}
	/**
	 * Get the connection to the database
	 * @return the connection or null if errors occured
	 */
	public static Dataset getConnection(String repo) {
		Dataset dataset;

		try {
			Class.forName(TDBCLASSNAME);
		} catch (ClassNotFoundException e) {
			LOG.warning(e.fillInStackTrace().toString());
			return null;
		}
		
		try	{
			dataset = TDBFactory.createDataset(TDBDIRECTORY + repo);
		} catch (TDBException e) {
			LOG.warning(e.fillInStackTrace().toString());
			return null;
		} 

		return dataset;
	}

	public static String getTDBDirectory() {
		return TDBDIRECTORY;
	}

}
