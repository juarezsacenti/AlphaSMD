package com.fabiosalvini.spatialhierarchybuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;

public class Entity {
	
	private static final Logger LOG = Logger.getLogger(App.class.getName());
	
	private String label;
	Set<Resource> resources;

	public Entity(String label) {
		this.label = label;
		resources = new HashSet<Resource>();
	}

	public String getLabel() {
		return label;
	}
	
	public void addResource(Resource r) {
		resources.add(r);
	}
	
	public int findResources() {
		int oldResSize = resources.size();
		findResources(resources);
		return resources.size() - oldResSize;
	}
	
	private void findResources(Set<Resource> resSet) {
		Set<Resource> newResources = new HashSet<Resource>();
		for(Resource r: resSet) {
			Set<Resource> searchRes = Searcher.findSameAs(r);
			for(Resource foundR : searchRes) {
				if(!resources.contains(foundR)) {
					newResources.add(foundR);
				}
			}
		}
		resources.addAll(newResources);
		if(newResources.size() > 0) {
			findResources(newResources);
		}
	}

}
