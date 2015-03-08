package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.HashSet;
import java.util.Set;

public class DbpediaDataset implements Dataset {
	
	public static final String DOMAIN = "dbpedia.org";
	public static final String SPARQL_ENDPOINT = "http://dbpedia.org/sparql";
	private static Set<String> SAMEAS_PROPERTIES;
	private static Set<Dataset> LINKEDBY_DATASETS;
	
	private static DbpediaDataset singleton = new DbpediaDataset();

	private DbpediaDataset() {
		SAMEAS_PROPERTIES = new HashSet<String>();
		SAMEAS_PROPERTIES.add("http://www.w3.org/2002/07/owl#sameAs");
		LINKEDBY_DATASETS = new HashSet<Dataset>();
		LINKEDBY_DATASETS.add(LinkedGeoDataDataset.getSingleton());
	}

	public static DbpediaDataset getSingleton() {
		return singleton;
	}
	
	public String getDomain() {
		return DOMAIN;
	}
	
	public String getSparqlEndpoint() {
		return SPARQL_ENDPOINT;
	}
	
	public Set<String> getSameAsProperties() {
		return SAMEAS_PROPERTIES;
	}

	public Set<Dataset> getLinkedByDatasets() {
		return LINKEDBY_DATASETS;
	}

}