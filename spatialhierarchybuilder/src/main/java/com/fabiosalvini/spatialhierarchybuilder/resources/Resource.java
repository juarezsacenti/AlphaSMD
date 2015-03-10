package com.fabiosalvini.spatialhierarchybuilder.resources;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevelInstance;


public abstract class Resource {
	
	protected String url;
	protected HierarchyLevelInstance lvlInstance;
	
	public Resource(String url) {
		if(url == null || url.equals("")) {
			throw new IllegalArgumentException("Error creating Resource: url cannot be empty!");
		}
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public HierarchyLevelInstance getHierarchyLevelInstance() {
		return lvlInstance;
	}
	
	public void setHierarchyLevelInstance(HierarchyLevelInstance lvlInstance) {
		this.lvlInstance = lvlInstance;
	}
	
	public boolean hasHierarchy() {
		return lvlInstance != null;
	}
	
	public abstract Dataset getDataset();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Resource other = (Resource) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
