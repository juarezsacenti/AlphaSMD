package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevel;

public class GeonamesDataset implements Dataset {
	
	public static final String DOMAIN = "geonames.org";
	//public static final String SPARQL_ENDPOINT = "http://www.lotico.com:3030/lotico/sparql";
	private static Set<String> SAMEAS_PROPERTIES;
	private static Set<Dataset> LINKEDBY_DATASETS;
	private static Set<HierarchyLevel> LEVELS;
	
	private static GeonamesDataset singleton = new GeonamesDataset();

	private GeonamesDataset() {
		SAMEAS_PROPERTIES = new HashSet<String>();
		LINKEDBY_DATASETS = new HashSet<Dataset>();
		LINKEDBY_DATASETS.add(DbpediaDataset.getSingleton());
		LINKEDBY_DATASETS.add(LinkedGeoDataDataset.getSingleton());
		LEVELS = new HashSet<HierarchyLevel>();
		LEVELS.add(HierarchyLevel.CONTINENT);
		LEVELS.add(HierarchyLevel.COUNTRY);
		LEVELS.add(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_1);
		LEVELS.add(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_2);
		LEVELS.add(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_3);
		LEVELS.add(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_4);
		LEVELS.add(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_5);
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
	public Set<HierarchyLevel> getHierarchyLevels() {
		return LEVELS;
	}
	
	@Override
	public HierarchyLevel getHierarchyLevelFromName(String name) {
		switch(name) {
			case "CONT":
				return HierarchyLevel.CONTINENT;
			case "PCLI":
				return HierarchyLevel.COUNTRY;
			case "ADM1":
				return HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_1;
			case "ADM2":
				return HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_2;
			case "ADM3":
				return HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_3;
			case "ADM4":
				return HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_4;
			case "ADM5":
				return HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_5;
			default:
				return null;
		}
	}

}
