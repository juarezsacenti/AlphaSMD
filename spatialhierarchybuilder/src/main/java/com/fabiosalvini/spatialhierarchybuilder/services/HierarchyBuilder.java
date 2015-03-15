package com.fabiosalvini.spatialhierarchybuilder.services;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fabiosalvini.spatialhierarchybuilder.Entity;
import com.fabiosalvini.spatialhierarchybuilder.Utils;
import com.fabiosalvini.spatialhierarchybuilder.database.HierarchyDAO;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GADMDataset;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.Hierarchy;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyElement;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevel;
import com.fabiosalvini.spatialhierarchybuilder.json.GeonamesHierarchyJSON;
import com.fabiosalvini.spatialhierarchybuilder.resources.GADMResource;
import com.fabiosalvini.spatialhierarchybuilder.resources.GeonamesResource;
import com.fabiosalvini.spatialhierarchybuilder.resources.LinkedGeoDataResource;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;
import com.google.gson.Gson;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class HierarchyBuilder {
	
	private static final Logger LOG = Logger.getLogger(HierarchyBuilder.class.getName());

	public HierarchyBuilder() {
	}
	
	public static void retrieveHierarchy(Entity e) {
		for(Resource r : e.getResources()) {
			if(canFindLevels(e.getHierarchy(), r)) {
				retrieveHierarchy(e.getHierarchy(), r);
			}
		}
		HierarchyDAO hDAO = new HierarchyDAO();
		try {
			hDAO.saveHierarchy(e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private static boolean canFindLevels(Hierarchy h, Resource r) {
		Collection<HierarchyLevel> missingLevels = h.getMissingLevels();
		missingLevels.removeAll(r.getDataset().getHierarchyLevels());
		return missingLevels.size() > 0;
	}
	
	private static void retrieveHierarchy(Hierarchy hierarchy, Resource r) {
		if(r instanceof GeonamesResource) {
			retrieveHierarchy(hierarchy, (GeonamesResource) r);
		} else if(r instanceof GADMResource) {
			retrieveHierarchy(hierarchy, (GADMResource) r);
		} else if(r instanceof LinkedGeoDataResource) {
			retrieveHierarchy(hierarchy, (LinkedGeoDataResource) r);
		}
	}
	
	
	private static void retrieveHierarchy(Hierarchy hierarchy, GeonamesResource gr) {
		String geonameId = gr.getGeonameId();
		if(geonameId == null || geonameId.equals("")) {
			return;
		}
		String hierarchyUrl = "http://api.geonames.org/hierarchyJSON?geonameId="+geonameId+"&username=fsalvini";
        String json;
		try {
			json = Utils.readUrl(hierarchyUrl);
		} catch (Exception e) {
			LOG.warning(e.fillInStackTrace().toString());
			return;
		}
        Gson gson = new Gson();        
        GeonamesHierarchyJSON gh = gson.fromJson(json, GeonamesHierarchyJSON.class);
        hierarchy.addHierarchyElements(gh.getHierarchyElements());
	}
	
	private static void retrieveHierarchy(Hierarchy hierarchy, GADMResource gadmr) {
		Model model = ModelFactory.createDefaultModel();
        model.read(gadmr.getUrl());
        Property p = model.getProperty("http://gadm.geovocab.org/spatial#PP");
        Iterator<RDFNode> spatialIter = model.listObjectsOfProperty(p);
        while(spatialIter.hasNext()) {
        	HierarchyElement elem = getGADMElement(spatialIter.next().toString() + ".rdf");
        	if(elem != null) {
        		hierarchy.addHierarchyElement(elem);
        	}
        }
	}
	
	private static void retrieveHierarchy(Hierarchy hierarchy, LinkedGeoDataResource lgdr) {
		Model model = ModelFactory.createDefaultModel();
		try {
			model.read(lgdr.getUrl());
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
        Property pCity = model.getProperty("http://linkedgeodata.org/ontology/addr%3Acity");
        Property pAddress = model.getProperty("http://linkedgeodata.org/ontology/addr%3Astreet");
        
        Iterator<RDFNode> cityIter = model.listObjectsOfProperty(pCity);
        Iterator<RDFNode> addressIter = model.listObjectsOfProperty(pAddress);
        String city = (cityIter.hasNext()) ? cityIter.next().toString() : null;
        String address = (addressIter.hasNext()) ? addressIter.next().toString() : null;
        
        if(city != null) {
        	HierarchyElement elem = new HierarchyElement(city, HierarchyLevel.CITY);
        	hierarchy.addHierarchyElement(elem);
        }
        if(address != null) {
        	HierarchyElement elem = new HierarchyElement(address, HierarchyLevel.ADDRESS);
        	hierarchy.addHierarchyElement(elem);
        }
	}
		
	
	public static HierarchyElement getGADMElement(String url) {
		Model model = ModelFactory.createDefaultModel();
		try {
			model.read(url);
		} catch(Exception e) {
			return null;
		}
        Property pLabel = model.getProperty("http://www.w3.org/2000/01/rdf-schema#label");
        Property pTypes = model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        Iterator<RDFNode> labelIter = model.listObjectsOfProperty(pLabel);
        Iterator<RDFNode> typesIter = model.listObjectsOfProperty(pTypes);
        String label = (labelIter.hasNext()) ? labelIter.next().toString() : null;
        String levelName = null;
        while(levelName == null && typesIter.hasNext()) {
        	String type = typesIter.next().toString();
        	if(type.startsWith("http://gadm.geovocab.org/ontology#Level")) {
        		levelName = type.substring(34);
        	} else if(type.startsWith("http://gadm.geovocab.org/ontology#Country")) {
        		levelName = type.substring(34);
        	}
        }
        HierarchyLevel level = GADMDataset.getSingleton().getHierarchyLevelFromName(levelName);
        if(label == null || level == null) {
        	return null;
        }
        return new HierarchyElement(label, level);
	}

}
