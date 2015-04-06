package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.CatHierarchyLevel;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.ObjHierarchyLevel;

public class GeonamesDataset implements Dataset {
	
	public static final String DOMAIN = "geonames.org";
	//public static final String SPARQL_ENDPOINT = "http://www.lotico.com:3030/lotico/sparql";
	private static Set<String> SAMEAS_PROPERTIES;
	private static Set<Dataset> LINKEDBY_DATASETS;
	private static Set<ObjHierarchyLevel> OBJ_LEVELS;
	private static Set<CatHierarchyLevel> CAT_LEVELS;

	private static GeonamesDataset singleton = new GeonamesDataset();

	private GeonamesDataset() {
		SAMEAS_PROPERTIES = new HashSet<String>();
		LINKEDBY_DATASETS = new HashSet<Dataset>();
		LINKEDBY_DATASETS.add(DbpediaDataset.getSingleton());
		LINKEDBY_DATASETS.add(LinkedGeoDataDataset.getSingleton());
		OBJ_LEVELS = new HashSet<ObjHierarchyLevel>();
		OBJ_LEVELS.add(ObjHierarchyLevel.CONTINENT);
		OBJ_LEVELS.add(ObjHierarchyLevel.COUNTRY);
		OBJ_LEVELS.add(ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_1);
		OBJ_LEVELS.add(ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_2);
		OBJ_LEVELS.add(ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_3);
		OBJ_LEVELS.add(ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_4);
		OBJ_LEVELS.add(ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_5);
		CAT_LEVELS = new HashSet<CatHierarchyLevel>();
		CAT_LEVELS.add(CatHierarchyLevel.NON_LEAF);
		CAT_LEVELS.add(CatHierarchyLevel.LEAF);
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
	
	@Override
	public Set<ObjHierarchyLevel> getObjHierarchyLevels() {
		return OBJ_LEVELS;
	}
	
	@Override
	public ObjHierarchyLevel getObjHierarchyLevelFromName(String name) {
		switch(name) {
			case "CONT":
				return ObjHierarchyLevel.CONTINENT;
			case "PCLI":
				return ObjHierarchyLevel.COUNTRY;
			case "ADM1":
				return ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_1;
			case "ADM2":
				return ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_2;
			case "ADM3":
				return ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_3;
			case "ADM4":
				return ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_4;
			case "ADM5":
				return ObjHierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_5;
			default:
				return null;
		}
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
