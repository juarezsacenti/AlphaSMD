package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.HashSet;
import java.util.Set;

public class GADMDataset implements Dataset {
	
	public static final String DOMAIN = "gadm.geovocab.org";
	//public static final String SPARQL_ENDPOINT = "";
	private static Set<String> SAMEAS_PROPERTIES;
	private static Set<Dataset> LINKEDBY_DATASETS;
	
	private static GADMDataset singleton = new GADMDataset();

	private GADMDataset() {
		SAMEAS_PROPERTIES = new HashSet<String>();
		LINKEDBY_DATASETS = new HashSet<Dataset>();
		LINKEDBY_DATASETS.add(LinkedGeoDataDataset.getSingleton());
	}

	public static GADMDataset getSingleton() {
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
