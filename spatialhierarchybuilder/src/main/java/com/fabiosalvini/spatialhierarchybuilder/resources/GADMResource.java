package com.fabiosalvini.spatialhierarchybuilder.resources;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GADMDataset;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevel;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevelInstance;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyManager;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;


public class GADMResource extends Resource {

	public GADMResource(String url) {
		super(url);
	}

	@Override
	public Dataset getDataset() {
		return GADMDataset.getSingleton();
	}
	
	@Override
	public boolean retrieveHierarchy() {
		Model model = ModelFactory.createDefaultModel();
        model.read(url);
        Property p = model.getProperty("http://gadm.geovocab.org/spatial#PP");
        Iterator<RDFNode> spatialIter = model.listObjectsOfProperty(p);
        List<Level> levels = new LinkedList<Level>();
        while(spatialIter.hasNext()) {
        	Level l = getLevel(spatialIter.next().toString() + ".rdf");
        	if(l != null) {
        		levels.add(l);
        	}
        }
        if(levels.size() == 0) {
        	return false;
        }
        Collections.sort(levels);
        HierarchyLevel hl = null;
        for(Level l : levels) {
        	hl = new HierarchyLevel(l.level, hl);
        	lvlInstance = new HierarchyLevelInstance(l.label, hl, lvlInstance);
        }
		return true;
	}
	
	public Level getLevel(String url) {
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
        String level = null;
        while(level == null && typesIter.hasNext()) {
        	String type = typesIter.next().toString();
        	if(type.startsWith("http://gadm.geovocab.org/ontology#Level")) {
        		level = type.substring(34);
        	} else if(type.startsWith("http://gadm.geovocab.org/ontology#Country")) {
        		level = type.substring(34);
        	}
        }
        
        if(label == null || level == null) {
        	return null;
        }
        return new Level(label, level);
	}
	
	private class Level implements Comparable<Level>{
		String label;
		String level;
		
		public Level(String label, String level) {
			super();
			this.label = label;
			this.level = level;
		}

		public int compareTo(Level arg0) {
			Level other = ((Level)arg0);
			if(level.equals(other.level)) {
				return 0;
			}
			if(level.equals("Country")) {
				return -1;
			} else if(other.level.equals("Country")){
				return 1;
		    }if(level.length() < other.level.length()) {
				return 1;
			}
			return level.compareTo(other.level);
		}
		
	}
}
