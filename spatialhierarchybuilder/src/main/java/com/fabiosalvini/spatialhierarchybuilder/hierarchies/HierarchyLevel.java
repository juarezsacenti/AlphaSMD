package com.fabiosalvini.spatialhierarchybuilder.hierarchies;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public enum HierarchyLevel {
	CONTINENT, 
	COUNTRY, 
	ADMINISTRATIVE_AREA_LEVEL_1, 
	ADMINISTRATIVE_AREA_LEVEL_2, 
	ADMINISTRATIVE_AREA_LEVEL_3, 
	ADMINISTRATIVE_AREA_LEVEL_4, 
	ADMINISTRATIVE_AREA_LEVEL_5,
	CITY,
	ADDRESS;
	
	public static Collection<HierarchyLevel> getAllLevels() {
		Set<HierarchyLevel> levels = new HashSet<HierarchyLevel>();
		levels.add(CONTINENT);
		levels.add(COUNTRY);
		levels.add(ADMINISTRATIVE_AREA_LEVEL_1);
		levels.add(ADMINISTRATIVE_AREA_LEVEL_2);
		levels.add(ADMINISTRATIVE_AREA_LEVEL_3);
		levels.add(ADMINISTRATIVE_AREA_LEVEL_4);
		levels.add(ADMINISTRATIVE_AREA_LEVEL_5);
		levels.add(CITY);
		levels.add(ADDRESS);
		return levels;
	}
}
