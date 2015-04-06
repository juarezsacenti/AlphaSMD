package com.fabiosalvini.spatialhierarchybuilder.steps;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.fabiosalvini.spatialhierarchybuilder.Entity;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

public class DimensionTailoring {
	
	private static final Logger LOG = Logger.getLogger(DimensionTailoring.class.getName());
	private static HashMap<String,Integer> numberOfHits = new HashMap<String,Integer>();

	public static void loadNumberOfHits(Entity e) {
		List<String> hierarchy = e.getCatHierarchy();
		
		for(String uri : hierarchy) {
			if(!numberOfHits.containsKey(uri)) {
				numberOfHits.put(uri, 1);
			} else {
				numberOfHits.put(uri, numberOfHits.get(uri) + 1);
			}
		}
	}

	public static Model tailorDimension(Model model, Property p, int i) {
		
		return null;
	}

	public static void analyze(Model dim) {
		System.out.println("idresource: ");
		int count = 1;
		System.out.println("level "+count);
	}
}
