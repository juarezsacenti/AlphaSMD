package com.fabiosalvini.spatialhierarchybuilder.hierarchies;

public class HierarchyLevelInstance {
	
	private String name;
	private HierarchyLevel level;
	private HierarchyLevelInstance parent;

	public HierarchyLevelInstance(String name, HierarchyLevel level, HierarchyLevelInstance parent) {
		this.name = name;
		this.level = level;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HierarchyLevel getLevel() {
		return level;
	}

	public void setLevel(HierarchyLevel level) {
		this.level = level;
	}

	public HierarchyLevelInstance getParent() {
		return parent;
	}

	public void setParent(HierarchyLevelInstance parent) {
		this.parent = parent;
	}
}
