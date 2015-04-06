package com.fabiosalvini.spatialhierarchybuilder.resources;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.OntDesPatDataset;

public class OntDesPatResource extends Resource {

	public OntDesPatResource(String url) {
		super(url);
	}

	@Override
	public Dataset getDataset() {
		return OntDesPatDataset.getSingleton();
	}
}
