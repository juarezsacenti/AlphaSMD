package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.CatHierarchyLevel;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.ObjHierarchyLevel;

public class OWL2Dataset implements Dataset {
	
	public static final String DOMAIN = "www.w3.org";
	//public static final String SPARQL_ENDPOINT = "";
	private static Set<String> SAMEAS_PROPERTIES;
	private static Set<Dataset> LINKEDBY_DATASETS;
	
	private static OWL2Dataset singleton = new OWL2Dataset();

	private OWL2Dataset() {
		SAMEAS_PROPERTIES = new HashSet<String>();
		SAMEAS_PROPERTIES.add("http://www.w3.org/2002/07/owl#equivalentClass");
		LINKEDBY_DATASETS = new HashSet<Dataset>();
	}

	public static OWL2Dataset getSingleton() {
		return singleton;
	}
	
	public String getDomain() {
		return DOMAIN;
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
		return new HashSet<CatHierarchyLevel>();
	}

	@Override
	public CatHierarchyLevel getCatHierarchyLevelFromName(String name) {
		return null;
	}

}
