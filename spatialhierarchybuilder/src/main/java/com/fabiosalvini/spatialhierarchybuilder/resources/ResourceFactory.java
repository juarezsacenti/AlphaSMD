package com.fabiosalvini.spatialhierarchybuilder.resources;

import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.datasets.DbpediaDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GADMDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GeonamesDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.LinkedGeoDataDataset;

public class ResourceFactory {
	
	private static Set<Resource> resources = new HashSet<Resource>();
	
	public static Resource getResource(String url) {
		for(Resource r: resources) {
			if(r.getUrl().equals(url)) {
				return r;
			}
		}
		Resource r = null;
		if(url.contains(DbpediaDataset.DOMAIN)) {
			if(url.startsWith("http://dbpedia.org")) {
				//Temporary discard all dbpedia resources that starts with a different url (like it.dbpedia.org)
				r = new DbpediaResource(url);
			}
		}
		if(url.contains(GeonamesDataset.DOMAIN)) {
			r =  new GeonamesResource(url);
		}
		if(url.contains(LinkedGeoDataDataset.DOMAIN)) {
			r =  new LinkedGeoDataResource(url);
		}
		if(url.contains(GADMDataset.DOMAIN)) {
			r = new GADMResource(url);
		}
		if(r != null) {
			resources.add(r);
		}
		return r;
	}
	
	public static Resource getResource(double latitude, double longitude) {
		String url = "http://gadm.geovocab.org/services/withinRegion?lat="+latitude+"&long="+longitude+"#point";
		return getResource(url);
	}
}
