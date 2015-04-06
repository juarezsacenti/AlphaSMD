package com.fabiosalvini.spatialhierarchybuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.Hierarchy;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;

public class Entity {
	
	private Integer id;
	private String label;
	private Double latitude;
	private Double longitude;
	Set<Resource> resources;
	Hierarchy objHierarchy;
	List<String> catHierarchy;
	String identifier;

	public Entity(Integer id, String label) {
		if(id == null || label == null) {
			throw new IllegalArgumentException("Error creating Entity: parameters cannot be empty!");
		}
		this.id = id;
		this.label = label;
		resources = new HashSet<Resource>();
		objHierarchy = new Hierarchy();
		catHierarchy = new ArrayList<String>();
		this.identifier = null;
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
	
	public void setObjHierarchy(Hierarchy hierarchy) {
		this.objHierarchy = hierarchy;
	}
	
	public Hierarchy getObjHierarchy() {
		return objHierarchy;
	}

	public void setCatHierarchy(List<String> hierarchy) {
		this.catHierarchy = hierarchy;
	}
	
	public List<String> getCatHierarchy() {
		return catHierarchy;
	}
	
	public Resource getResource(Dataset d) {
		Iterator<Resource> it = resources.iterator();
		Resource r;
		
		while(it.hasNext()) {
			r = it.next();
			if(System.identityHashCode(d) == System.identityHashCode(r.getDataset())) {
				return r;
			}
		}
		return null;
	}

	public void setIdentifier(String URI) {
		this.identifier = URI;
	}
	
	public String getIdentifier() {
		return identifier;
	}
}
