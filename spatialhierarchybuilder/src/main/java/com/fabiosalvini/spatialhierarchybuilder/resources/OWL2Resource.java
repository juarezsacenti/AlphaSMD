package com.fabiosalvini.spatialhierarchybuilder.resources;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.OWL2Dataset;

public class OWL2Resource extends Resource {

	public OWL2Resource(String url) {
		super(url);
	}

	@Override
	public Dataset getDataset() {
		return OWL2Dataset.getSingleton();
	}
}

