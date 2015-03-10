package com.fabiosalvini.spatialhierarchybuilder.resources;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.DbpediaDataset;


public class DbpediaResource extends Resource {

	public DbpediaResource(String url) {
		super(url);
	}

	@Override
	public Dataset getDataset() {
		return DbpediaDataset.getSingleton();
	}

}
