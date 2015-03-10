package com.fabiosalvini.spatialhierarchybuilder.resources;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fabiosalvini.spatialhierarchybuilder.Utils;
import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GeonamesDataset;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevelInstance;
import com.fabiosalvini.spatialhierarchybuilder.json.GeonamesHierarchyJSON;
import com.google.gson.Gson;


public class GeonamesResource extends Resource {
	
	private static final Logger LOG = Logger.getLogger(GeonamesResource.class.getName());
	private static Pattern idPattern = Pattern.compile("geonames.org/([^/]*)/");

	public GeonamesResource(String url) {
		super(url);
	}

	@Override
	public Dataset getDataset() {
		return GeonamesDataset.getSingleton();
	}
	
	@Override
	public boolean retrieveHierarchy() {
		String geonameId = GeonamesResource.getGeonameId(url);
		if(geonameId == null || geonameId.equals("")) {
			return false;
		}
		String hierarchyUrl = "http://api.geonames.org/hierarchyJSON?geonameId="+geonameId+"&username=fsalvini";
        String json;
		try {
			json = Utils.readUrl(hierarchyUrl);
		} catch (Exception e) {
			LOG.warning(e.fillInStackTrace().toString());
			return false;
		}
        Gson gson = new Gson();        
        GeonamesHierarchyJSON hierarchy = gson.fromJson(json, GeonamesHierarchyJSON.class);
        lvlInstance = hierarchy.getLowestLevel().getParent(); //Discard the level representing the resource itself
		return true;
	}
	
	private static String getGeonameId(String url) {
		Matcher m = idPattern.matcher(url);
		if (m.find()) {
		    return m.group(1);
		} else {
			return null;
		}
	}

}
