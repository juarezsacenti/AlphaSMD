package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevel;

public class GADMDataset implements Dataset {
	
	public static final String DOMAIN = "gadm.geovocab.org";
	//public static final String SPARQL_ENDPOINT = "";
	private static Set<String> SAMEAS_PROPERTIES;
	private static Set<Dataset> LINKEDBY_DATASETS;
	private static Set<HierarchyLevel> LEVELS;
	
	private static GADMDataset singleton = new GADMDataset();

	private GADMDataset() {
		SAMEAS_PROPERTIES = new HashSet<String>();
		LINKEDBY_DATASETS = new HashSet<Dataset>();
		LINKEDBY_DATASETS.add(LinkedGeoDataDataset.getSingleton());
		LEVELS = new HashSet<HierarchyLevel>();
		LEVELS.add(HierarchyLevel.COUNTRY);
		LEVELS.add(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_1);
		LEVELS.add(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_2);
		LEVELS.add(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_3);
		LEVELS.add(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_4);
		LEVELS.add(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_5);
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
	public Set<HierarchyLevel> getHierarchyLevels() {
		return LEVELS;
	}
	
	@Override
	public HierarchyLevel getHierarchyLevelFromName(String name) {
		switch(name) {
			case "Country":
				return HierarchyLevel.COUNTRY;
			case "Level1":
				return HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_1;
			case "Level2":
				return HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_2;
			case "Level3":
				return HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_3;
			case "Level4":
				return HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_4;
			case "Level5":
				return HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_5;
			default:
				return null;
		}
	}

}
