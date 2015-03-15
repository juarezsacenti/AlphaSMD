package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevel;

public class LinkedGeoDataDataset implements Dataset {
	
	public static final String DOMAIN = "linkedgeodata.org";
	public static final String SPARQL_ENDPOINT = "http://linkedgeodata.org/sparql";
	private static Set<String> SAMEAS_PROPERTIES;
	private static Set<Dataset> LINKEDBY_DATASETS;
	
	private static LinkedGeoDataDataset singleton = new LinkedGeoDataDataset();

	private LinkedGeoDataDataset() {
		SAMEAS_PROPERTIES = new HashSet<String>();
		SAMEAS_PROPERTIES.add("http://www.w3.org/2002/07/owl#sameAs");
		SAMEAS_PROPERTIES.add("http://linkedgeodata.org/ontology/gadmSameAs");
		LINKEDBY_DATASETS = new HashSet<Dataset>();
		LINKEDBY_DATASETS.add(DbpediaDataset.getSingleton());
	}

	public static LinkedGeoDataDataset getSingleton() {
		return singleton;
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

	public String getDomain() {
		return DOMAIN;
	}

	@Override
	public Set<HierarchyLevel> getHierarchyLevels() {
		return new HashSet<HierarchyLevel>();
	}
	
	@Override
	public HierarchyLevel getHierarchyLevelFromName(String name) {
		return null;
	}

}
