package com.fabiosalvini.spatialhierarchybuilder.resources;

import com.fabiosalvini.spatialhierarchybuilder.datasets.DbpediaDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GADMDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GeonamesDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.LinkedGeoDataDataset;

public class ResourceFactory {
	
	public static Resource getResource(Integer idresource, String url) {
		if(url.contains(DbpediaDataset.DOMAIN)) {
			return new DbpediaResource(idresource, url);
		}
		if(url.contains(GeonamesDataset.DOMAIN)) {
			return new GeonamesResource(idresource, url);
		}
		if(url.contains(LinkedGeoDataDataset.DOMAIN)) {
			return new LinkedGeoDataResource(idresource, url);
		}
		if(url.contains(GADMDataset.DOMAIN)) {
			return new GADMResource(idresource, url);
		}
		return null;
	}
}
