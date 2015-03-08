package com.fabiosalvini.spatialhierarchybuilder.resources;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;


public abstract class Resource {
	
	private Integer idresource;
	protected String url;
	
	public Resource(Integer idresource, String url) {
		if(url == null || url.equals("")) {
			throw new IllegalArgumentException("Error creating Resource: url cannot be empty!");
		}
		this.idresource = idresource;
		this.url = url;
	}

	public Integer getIdresource() {
		return idresource;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
