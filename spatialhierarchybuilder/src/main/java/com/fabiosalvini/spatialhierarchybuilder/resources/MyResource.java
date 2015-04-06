package com.fabiosalvini.spatialhierarchybuilder.resources;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.hp.hpl.jena.rdf.model.Resource;

public abstract interface MyResource extends Resource {
	public abstract Dataset getDataset();
}
