package com.fabiosalvini.spatialhierarchybuilder.json;

import java.util.Collection;
import java.util.HashSet;

import com.fabiosalvini.spatialhierarchybuilder.datasets.GeonamesDataset;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyElement;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.ObjHierarchyLevel;

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
	
	public Collection<HierarchyElement> getHierarchyElements() {
		Collection<HierarchyElement> elements = new HashSet<HierarchyElement>();
		for(int i = 0; i < geonames.length; i++) {
			Entry e = geonames[i];
			ObjHierarchyLevel level = GeonamesDataset.getSingleton().getObjHierarchyLevelFromName(e.getFcode());
			if(level != null) {
				HierarchyElement elem = new HierarchyElement(e.getName(), level);
				elements.add(elem);
			}
		}
		return elements;
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