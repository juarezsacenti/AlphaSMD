package com.fabiosalvini.spatialhierarchybuilder.hierarchies;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Temporary not used.
 *
 */
public class HierarchyManager {
	
	private static final Logger LOG = Logger.getLogger(HierarchyManager.class.getName());
	private static HierarchyManager singleton = new HierarchyManager();
	private Set<HierarchyLevel> levels;
	private Set<HierarchyLevelInstance> levelInstances;

	private HierarchyManager() {
		levels = new HashSet<HierarchyLevel>();
		levelInstances = new HashSet<HierarchyLevelInstance>();
	}

	public static HierarchyManager getSingleton() {
		return singleton;
	}
	
	public HierarchyLevel getLevel(String name, HierarchyLevel parent) {
		for(HierarchyLevel l : levels) {
			if(l == null) {
				continue;
			}
			if(l.getName() == null || l.getName().equals(name) && (l.getParent() == null || l.getParent().equals(parent))) {
				return l;
			}
		}
		HierarchyLevel l = new HierarchyLevel(name, parent);
		levels.add(l);
		return l;	
	}
	
	public HierarchyLevelInstance getLevelInstance(String name, HierarchyLevel level, HierarchyLevelInstance parent) {
		for(HierarchyLevelInstance l : levelInstances) {
			if(l == null) {
				continue;
			}
			if(l.getName().equals(name) && l.getLevel().equals(level) && (l.getParent() == parent || l.getParent().equals(parent))) {
				return l;
			}
		}
		HierarchyLevelInstance l = new HierarchyLevelInstance(name, level, parent);
		levelInstances.add(l);
		return l;	
	}
	
	public boolean updateDB() {
		return false;
	}
}
