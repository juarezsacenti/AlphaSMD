package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.CatHierarchyLevel;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.ObjHierarchyLevel;

public class GADMDataset implements Dataset {
	
	public static final String DOMAIN = "gadm.geovocab.org";
	//public static final String SPARQL_ENDPOINT = "";
	private static Set<String> SAMEAS_PROPERTIES;
	private static Set<Dataset> LINKEDBY_DATASETS;
	private static Set<ObjHierarchyLevel> OBJ_LEVELS;
	
	private static GADMDataset singleton = new GADMDataset();

	private GADMDataset() {
		SAMEAS_PROPERTIES = new HashSet<String>();
		LINKEDBY_DATASETS = new HashSet<Dataset>();
		LINKEDBY_DATASETS.add(LinkedGeoDataDataset.getSingleton());
		OBJ_LEVELS = new HashSet<ObjHierarchyLevel>();
		OBJ_LEVELS.add(ObjHierarchyLevel.COUNTRY);
		OBJ_LEVELS.add(ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_1);
		OBJ_LEVELS.add(ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_2);
		OBJ_LEVELS.add(ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_3);
		OBJ_LEVELS.add(ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_4);
		OBJ_LEVELS.add(ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_5);
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
	
	@Override
	public Set<ObjHierarchyLevel> getObjHierarchyLevels() {
		return OBJ_LEVELS;
	}
	
	@Override
	public ObjHierarchyLevel getObjHierarchyLevelFromName(String name) {
		switch(name) {
			case "Country":
				return ObjHierarchyLevel.COUNTRY;
			case "Level1":
				return ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_1;
			case "Level2":
				return ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_2;
			case "Level3":
				return ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_3;
			case "Level4":
				return ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_4;
			case "Level5":
				return ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_5;
			default:
				return null;
		}
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
