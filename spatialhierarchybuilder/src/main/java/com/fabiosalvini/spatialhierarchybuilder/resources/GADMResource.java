package com.fabiosalvini.spatialhierarchybuilder.resources;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GADMDataset;


public class GADMResource extends Resource {

	public GADMResource(String url) {
		super(url);
	}

	@Override
	public Dataset getDataset() {
		return GADMDataset.getSingleton();
	}
}
