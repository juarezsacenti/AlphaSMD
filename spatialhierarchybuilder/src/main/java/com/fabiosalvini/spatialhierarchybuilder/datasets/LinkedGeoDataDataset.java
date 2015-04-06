package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.CatHierarchyLevel;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.ObjHierarchyLevel;

public class LinkedGeoDataDataset implements Dataset {
	
	public static final String DOMAIN = "linkedgeodata.org";
	public static final String SPARQL_ENDPOINT = "http://linkedgeodata.org/sparql";
	private static Set<String> SAMEAS_PROPERTIES;
	private static Set<Dataset> LINKEDBY_DATASETS;
	private static Set<CatHierarchyLevel> CAT_LEVELS;

	private static LinkedGeoDataDataset singleton = new LinkedGeoDataDataset();

	private LinkedGeoDataDataset() {
		SAMEAS_PROPERTIES = new HashSet<String>();
		SAMEAS_PROPERTIES.add("http://www.w3.org/2002/07/owl#sameAs");
		SAMEAS_PROPERTIES.add("http://linkedgeodata.org/ontology/gadmSameAs");
		LINKEDBY_DATASETS = new HashSet<Dataset>();
		LINKEDBY_DATASETS.add(DbpediaDataset.getSingleton());
		CAT_LEVELS = new HashSet<CatHierarchyLevel>();
		CAT_LEVELS.add(CatHierarchyLevel.NON_LEAF);
		CAT_LEVELS.add(CatHierarchyLevel.LEAF);
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
	public Set<ObjHierarchyLevel> getObjHierarchyLevels() {
		return new HashSet<ObjHierarchyLevel>();
	}
	
	@Override
	public ObjHierarchyLevel getObjHierarchyLevelFromName(String name) {
		return null;
	}
	
	@Override
	public Set<CatHierarchyLevel> getCatHierarchyLevels() {
		return CAT_LEVELS;
	}

	@Override
	public CatHierarchyLevel getCatHierarchyLevelFromName(String name) {
		switch(name) {
		case "Non_Leaf":
			return CatHierarchyLevel.NON_LEAF;
		case "Leaf":
			return CatHierarchyLevel.LEAF;
		default:
			return null;
		}
	}
}
