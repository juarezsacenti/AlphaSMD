package com.fabiosalvini.spatialhierarchybuilder.json;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevel;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevelInstance;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyManager;

public class GeonamesHierarchyJSON {
	
	private Entry[] geonames;

	public Entry[] getGeonames() {
		return geonames;
	}

	@Override
	public String toString() {
		String str = "GeonamesHierarchyJSON = ";
		for(Entry e: geonames) {
			str += e.toString() + ";";
		}
		return str;
	}
	
	public HierarchyLevelInstance getLowestLevel() {
		HierarchyLevelInstance parent = null;
		for(int i = 0; i < geonames.length; i++) {
			Entry e = geonames[i];
			HierarchyLevel level;
			if(parent == null) {
				level = new HierarchyLevel(e.getFcode(), null);
			} else {
				level = new HierarchyLevel(e.getFcode(), parent.getLevel());
			}
			parent = new HierarchyLevelInstance(e.getName(), level, parent);
		}
		return parent;
	}

}

class Entry {
	
	private String fcode;
	private String name;
	private Integer geonameId;
	
	public String getFcode() {
		return fcode;
	}
	public String getName() {
		return name;
	}
	public Integer getGeonameId() {
		return geonameId;
	}
	
	@Override
	public String toString() {
		return "Entry [fcode=" + fcode + ", name=" + name + ", geonameId="
				+ geonameId + "]";
	} 
}