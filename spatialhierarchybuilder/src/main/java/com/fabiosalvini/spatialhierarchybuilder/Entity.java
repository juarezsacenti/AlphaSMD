package com.fabiosalvini.spatialhierarchybuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.Hierarchy;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;

public class Entity {
	
	private Integer id;
	private String label;
	private Double latitude;
	private Double longitude;
	Set<Resource> resources;
	Hierarchy hierarchy;

	public Entity(Integer id, String label) {
		if(id == null || label == null) {
			throw new IllegalArgumentException("Error creating Entity: parameters cannot be empty!");
		}
		this.id = id;
		this.label = label;
		resources = new HashSet<Resource>();
		hierarchy = new Hierarchy();
	}
	

	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public Integer getId() {
		return id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void addResource(Resource r) {
		resources.add(r);
	}
	
	public void addResources(Collection<Resource> r) {
		resources.addAll(r);
	}

	public Set<Resource> getResources() {
		return resources;
	}
	
	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}
	
	public Hierarchy getHierarchy() {
		return hierarchy;
	}

}
