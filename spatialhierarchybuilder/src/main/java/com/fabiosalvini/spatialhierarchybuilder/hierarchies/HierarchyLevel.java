package com.fabiosalvini.spatialhierarchybuilder.hierarchies;

public final class HierarchyLevel {
	
	private String name;
	private HierarchyLevel parent;
	
	public HierarchyLevel(String name, HierarchyLevel parent) {
		this.name = name;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public HierarchyLevel getParent() {
		return parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HierarchyLevel other = (HierarchyLevel) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}

}
