package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.HashSet;
import java.util.Set;

public class GeonamesDataset implements Dataset {
	
	public static final String DOMAIN = "geonames.org";
	//public static final String SPARQL_ENDPOINT = "http://www.lotico.com:3030/lotico/sparql";
	private static Set<String> SAMEAS_PROPERTIES;
	private static Set<Dataset> LINKEDBY_DATASETS;
	
	private static GeonamesDataset singleton = new GeonamesDataset();

	private GeonamesDataset() {
		SAMEAS_PROPERTIES = new HashSet<String>();
		LINKEDBY_DATASETS = new HashSet<Dataset>();
		LINKEDBY_DATASETS.add(DbpediaDataset.getSingleton());
		LINKEDBY_DATASETS.add(LinkedGeoDataDataset.getSingleton());
	}

	public static GeonamesDataset getSingleton() {
		return singleton;
	}
	
	public String getSparqlEndpoint() {
		return null;
	}

	public Set<String> getSameAsProperties() {
		return SAMEAS_PROPERTIES;
	}

	public Set<Dataset> getLinkedByDatasets() {
		return LINKEDBY_DATASETS;
	}

	public String getDomain() {
		return DOMAIN;
	}

}
