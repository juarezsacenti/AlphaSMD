package com.fabiosalvini.spatialhierarchybuilder.hierarchies;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Hierarchy {
	
	private Set<HierarchyElement> elements;

	public Hierarchy() {
		elements = new HashSet<HierarchyElement>();
	}
	
	public boolean isEmpty() {
		return elements.size() == 0;
	}
	
	public void addHierarchyElement(HierarchyElement element) {
		if(element != null && getMissingLevels().contains(element.getLevel())) {
			elements.add(element);
		}
	}
	
	public void addHierarchyElements(Collection<HierarchyElement> elements) {
		if(elements != null) {
			for(HierarchyElement elem : elements) {
				addHierarchyElement(elem);
			}
		}
	}
	
	public Collection<ObjHierarchyLevel> getMissingLevels() {
		Collection<ObjHierarchyLevel> levels = new HashSet<ObjHierarchyLevel>(Arrays.asList(ObjHierarchyLevel.values()));
		for(HierarchyElement he : elements) {
			ObjHierarchyLevel l = he.getLevel();
			levels.remove(l);
		}
		return levels;
	}
	
	public HierarchyElement getElementAtLevel(ObjHierarchyLevel level) {
		for(HierarchyElement elem : elements) {
			if(elem.getLevel() == level) {
				return elem;
			}
		}
		return null;
	}

}
