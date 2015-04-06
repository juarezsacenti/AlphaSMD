package com.fabiosalvini.spatialhierarchybuilder.steps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.fabiosalvini.spatialhierarchybuilder.Entity;
import com.fabiosalvini.spatialhierarchybuilder.Utils;
import com.fabiosalvini.spatialhierarchybuilder.database.HierarchyDAO;
import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GADMDataset;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.Hierarchy;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyElement;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.ObjHierarchyLevel;
import com.fabiosalvini.spatialhierarchybuilder.json.GeonamesHierarchyJSON;
import com.fabiosalvini.spatialhierarchybuilder.resources.GADMResource;
import com.fabiosalvini.spatialhierarchybuilder.resources.GeonamesResource;
import com.fabiosalvini.spatialhierarchybuilder.resources.LinkedGeoDataResource;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;
import com.fabiosalvini.spatialhierarchybuilder.resources.ResourceFactory;
import com.google.gson.Gson;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class HierarchyExtraction {
	
	private static final Logger LOG = Logger.getLogger(HierarchyExtraction.class.getName());
	private static HashMap<String,Integer> numberOfHits = new HashMap<String,Integer>();

	public static void retrieveObjHierarchy(Entity e) {
		for(Resource r : e.getResources()) {
			if(canFindObjLevels(e.getObjHierarchy(), r)) {
				retrieveObjHierarchy(e.getObjHierarchy(), r);
			}
		}
		HierarchyDAO hDAO = new HierarchyDAO();
		try {
			hDAO.saveObjHierarchy(e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private static boolean canFindObjLevels(Hierarchy h, Resource r) {
		Collection<ObjHierarchyLevel> missingLevels = h.getMissingLevels();
		missingLevels.removeAll(r.getDataset().getObjHierarchyLevels());
		return missingLevels.size() > 0;
	}
	
	private static void retrieveObjHierarchy(Hierarchy hierarchy, Resource r) {
		if(r instanceof GeonamesResource) {
			retrieveObjHierarchy(hierarchy, (GeonamesResource) r);
		} else if(r instanceof GADMResource) {
			retrieveObjHierarchy(hierarchy, (GADMResource) r);
		} else if(r instanceof LinkedGeoDataResource) {
			retrieveObjHierarchy(hierarchy, (LinkedGeoDataResource) r);
		}
	}
	
	private static void retrieveObjHierarchy(Hierarchy hierarchy, GeonamesResource gr) {
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
	
	private static void retrieveObjHierarchy(Hierarchy hierarchy, GADMResource gadmr) {
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
	
	private static void retrieveObjHierarchy(Hierarchy hierarchy, LinkedGeoDataResource lgdr) {
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
        	HierarchyElement elem = new HierarchyElement(city, ObjHierarchyLevel.CITY);
        	hierarchy.addHierarchyElement(elem);
        }
        if(address != null) {
        	HierarchyElement elem = new HierarchyElement(address, ObjHierarchyLevel.ADDRESS);
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
        ObjHierarchyLevel level = GADMDataset.getSingleton().getObjHierarchyLevelFromName(levelName);
        if(label == null || level == null) {
        	return null;
        }
        return new HierarchyElement(label, level);
	}

	public static void retrieveCatHierarchy(Entity e) {
		HashSet<List<String>> hierarchies = new HashSet<List<String>>();
		
		List<String> h;
		for(Resource r : e.getResources()) {
			h = retrieveCatHierarchy(r);
			hierarchies.add(h);
		}
		
		List<String> chosenHierarchy = chooseHierarchy(hierarchies);
		
		String id = e.getIdentifier();
		if(!chosenHierarchy.get(0).equals(id)) {
			chosenHierarchy.remove(0);
			chosenHierarchy.add(0, id);
		}
		e.setCatHierarchy(chosenHierarchy);
		
		String thing = "http://www.w3.org/2002/07/owl#Thing";
		int last = chosenHierarchy.size()-1;
		if(!chosenHierarchy.get(last).equals(thing)) {
			chosenHierarchy.add(thing);
		}
		e.setCatHierarchy(chosenHierarchy);
		
		saveCatHierarchy(e);
	}

	private static List<String> retrieveCatHierarchy(Resource r) {
		if(r instanceof GeonamesResource) {
			return retrieveCatHierarchy((GeonamesResource) r);
		} else if(r instanceof GADMResource) {
			return new ArrayList<String>();
		} else {
			com.hp.hpl.jena.rdf.model.Resource jenaR = Facet.getSingleton().openGraph(r.getDataset().getDomain()).getResource(r.getUrl());
			return retrieveCatHierarchy(jenaR, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		}
	}
	
	private static List<String> retrieveCatHierarchy(GeonamesResource r) {
        List<String> hierarchy = new ArrayList<String>();
		Model datasetModel = Facet.getSingleton().openGraph("geonames.org");
		com.hp.hpl.jena.rdf.model.Resource jenaR = datasetModel.getResource(r.getUrl());
		
		HashSet<Property> listP = new HashSet<Property>();
		StmtIterator itS = datasetModel.listStatements();
        while(itS.hasNext()) {
        	listP.add(itS.next().getPredicate());
        }

        boolean found = false;
        Iterator<Property> itP = listP.iterator();
        Property p = null;
        while(itP.hasNext() && !found) {
        	p = itP.next();
        	found = datasetModel.contains(null, p, jenaR);
        }
        if(p != null) {
        	if(p.toString().equals("http://www.geonames.org/ontology#featureClass")) {
        		hierarchy.add(jenaR.getURI());
        		hierarchy.add(getLevelValue(jenaR, "http://www.geonames.org/ontology#featureClass", datasetModel));
        		hierarchy.add("http://www.geonames.org/ontology#Feature");
            } else if (p.toString().equals("http://www.geonames.org/ontology#featureCode")) {
        		hierarchy.add(jenaR.getURI());
        		hierarchy.add(getLevelValue(jenaR, "http://www.geonames.org/ontology#featureCode", datasetModel));
            	hierarchy.add(getLevelValue(jenaR, "http://www.geonames.org/ontology#featureClass", datasetModel));
        		hierarchy.add("http://www.geonames.org/ontology#Feature");
            }
        } else {
    		hierarchy.add(jenaR.getURI());
        	hierarchy.add(getLevelValue(jenaR, "http://www.geonames.org/ontology#featureCode", datasetModel));
        	hierarchy.add(getLevelValue(jenaR, "http://www.geonames.org/ontology#featureClass", datasetModel));
    		hierarchy.add("http://www.geonames.org/ontology#Feature");
        }
		return hierarchy;
	}
	
	private static String getLevelValue(com.hp.hpl.jena.rdf.model.Resource r, String pURI, Model datasetModel) {
		String uri;
		
		Property p = datasetModel.getProperty(pURI);
    	com.hp.hpl.jena.rdf.model.Resource resource = null;
    	RDFNode obj;
        NodeIterator itN = datasetModel.listObjectsOfProperty(r, p);
    	while(itN.hasNext()) {
        	obj = itN.next();
        	if(obj.isResource()) {
        		 resource = obj.asResource();
        	}
        }
        if(resource != null) {
        	uri = resource.getURI();
        } else {
        	uri = "http://semovdim.org/rdf#missingNode";
        }
        return uri;
	}

	private static List<String> retrieveCatHierarchy(
			com.hp.hpl.jena.rdf.model.Resource r, String pURI) {
		List<String> chosenHierarchy;
		
		Model model = r.getModel();
		Property p = model.getProperty(pURI);
		HashSet<List<String>> hierarchies = new HashSet<List<String>>();

		RDFNode father;
		Iterator<RDFNode> objIter;
		Set<RDFNode> fathers = model.listObjectsOfProperty(r, p).toSet();
		if(fathers.size() <= 0) {
			chosenHierarchy = new ArrayList<String>();
			chosenHierarchy.add(r.getURI());
		} else {
			objIter = fathers.iterator();
			while(objIter.hasNext()) {
	        	father = objIter.next();
	        	if(father.isResource()) {
	        		hierarchies.add(retrieveCatHierarchy(father.asResource(), "http://www.w3.org/2000/01/rdf-schema#subClassOf"));
	        	}
			}
			chosenHierarchy = chooseHierarchy(hierarchies);
			chosenHierarchy.add(0, r.getURI());
		}
		return chosenHierarchy;
	}

	private static List<String> chooseHierarchy(HashSet<List<String>> hierarchies) {
		int n, biggest = 0;
		List<String> biggestHierarchy = null;
		for(List<String> h : hierarchies) {
			n = h.size();
			if(n >= biggest){
				biggest = n;
				biggestHierarchy = h;
			}
		}		return biggestHierarchy;
	}

	public static void removeAntecipatedAssociations(Dataset singleton,
			String property1, String property2) {
		Model model = Facet.getSingleton().openGraph(singleton.getDomain());
		Property p1 = model.getProperty(property1);
		Property p2 = model.getProperty(property2);
		
		Iterator<RDFNode> itO;
		RDFNode obj;
		HashSet<com.hp.hpl.jena.rdf.model.Resource> fathers;
		com.hp.hpl.jena.rdf.model.Resource sub, father;
		Iterator<com.hp.hpl.jena.rdf.model.Resource> itF, itR = model.listResourcesWithProperty(p1).toList().iterator();
        while(itR.hasNext()) {
            sub = itR.next();
       		fathers = new HashSet<com.hp.hpl.jena.rdf.model.Resource>();
    		itO = model.listObjectsOfProperty(sub, p1);
            while(itO.hasNext()) {
    			obj = itO.next();
    			if(obj.isResource()) {
    				fathers.add(obj.asResource());
    			}
    		}
    		itF = fathers.iterator();
    		while(itF.hasNext()) {
    			father = itF.next();
    			if(isAnticipatedAssociation(father, fathers, p2, singleton)) {
    				model.remove(model.listStatements(sub, p1, father.asResource()));
    			}
    		}
        }
	}

	private static boolean isAnticipatedAssociation(
			com.hp.hpl.jena.rdf.model.Resource father,
			HashSet<com.hp.hpl.jena.rdf.model.Resource> fathers,
			Property p, Dataset singleton) {
		boolean isAnticipated = false;
		
		com.hp.hpl.jena.rdf.model.Resource otherFather;
		HashSet<com.hp.hpl.jena.rdf.model.Resource> ofConcepts;
		Iterator<com.hp.hpl.jena.rdf.model.Resource> itR = fathers.iterator();
		while(itR.hasNext() && !isAnticipated) {
			otherFather = itR.next();
			if(!father.equals(otherFather)) {
				ofConcepts = getConcepts(otherFather, p, singleton);
				if(ofConcepts.contains(father)) {
					isAnticipated = true;
				}
			}
		}
		return isAnticipated;
	}

	private static HashSet<com.hp.hpl.jena.rdf.model.Resource> getConcepts(
			com.hp.hpl.jena.rdf.model.Resource r, Property p, Dataset singleton) {
		HashSet<com.hp.hpl.jena.rdf.model.Resource> lstR = new HashSet<com.hp.hpl.jena.rdf.model.Resource>();
		Model model = Facet.getSingleton().openGraph(singleton.getDomain());
		RDFNode obj;
		com.hp.hpl.jena.rdf.model.Resource father;
		
        Iterator<RDFNode> itO = model.listObjectsOfProperty(r, p);
        while(itO.hasNext()) {
        	obj = itO.next();
        	if(obj.isResource()) {
        		father = obj.asResource();
        		if(!lstR.contains(father)) {
        			lstR.add(father);
        			lstR.addAll(getConcepts(father, p, singleton));
        		}
        	}
        }
		return lstR;
	}
	
	private static void saveCatHierarchy(Entity e) {
		List<String> hierarchy = e.getCatHierarchy();
		Model model = Facet.getSingleton().openGraph("semovdim.org");
		Property p = model.getProperty("http://semovdim.org/rdf#categoryHierarchy");
		
		String last = null;
		for(String uri : hierarchy) {
			if(last != null) {
				model.add(model.createResource(last), p, model.createResource(uri));
			}
			last = uri;
		}
	}
	
	public static void saveNumberOfHits(Entity e) {
		List<String> hierarchy = e.getCatHierarchy();
		Model model = Facet.getSingleton().openGraph("semovdim.org");
		
		for(String uri : hierarchy) {
			if(!numberOfHits.containsKey(uri)) {
				numberOfHits.put(uri, 1);
			} else {
				numberOfHits.put(uri, numberOfHits.get(uri) + 1);
			}
		}
	}
	
	public static void saveNumberOfHits() {
		Model model = Facet.getSingleton().openGraph("semovdim.org");
		Property p = model.getProperty("http://semovdim.org/rdf#numberOfHits");
		
		for(String uri : numberOfHits.keySet()) {
			model.addLiteral(model.getResource(uri), p, (int) numberOfHits.get(uri));
		}
	}
}
