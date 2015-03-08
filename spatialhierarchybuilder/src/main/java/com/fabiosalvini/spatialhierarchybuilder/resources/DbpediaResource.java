package com.fabiosalvini.spatialhierarchybuilder.resources;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.DbpediaDataset;


public class DbpediaResource extends Resource {

	public DbpediaResource(Integer idresource, String url) {
		super(idresource, url);
	}

	@Override
	public Dataset getDataset() {
		return DbpediaDataset.getSingleton();
	}

}
