package com.fabiosalvini.spatialhierarchybuilder.resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GeonamesDataset;


public class GeonamesResource extends Resource {
	
	private static Pattern idPattern = Pattern.compile("geonames.org/([^/]*)/");

	public GeonamesResource(String url) {
		super(url);
	}

	@Override
	public Dataset getDataset() {
		return GeonamesDataset.getSingleton();
	}
	
	public String getGeonameId() {
		Matcher m = idPattern.matcher(url);
		if (m.find()) {
		    return m.group(1);
		} else {
			return null;
		}
	}

}
