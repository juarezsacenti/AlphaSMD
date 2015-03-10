package com.fabiosalvini.spatialhierarchybuilder.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.fabiosalvini.spatialhierarchybuilder.Entity;
import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;
import com.fabiosalvini.spatialhierarchybuilder.resources.ResourceFactory;
import com.fabiosalvini.spatialhierarchybuilder.statistics.Statistics;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class ResourceSearcher {
	
	private static final Logger LOG = Logger.getLogger(ResourceSearcher.class.getName());
	
	public static Set<Resource> findSameAs(Resource r) {
		Set<Resource> resources = findSameAs(r, r.getDataset(), false);
		for(Dataset d : r.getDataset().getLinkedByDatasets()) {
			resources.addAll(findSameAs(r, d, true));
		}
		return resources;
	}
	
	public static Set<Resource> findSameAs(Resource r, Dataset d, boolean reversed) {
		Set<Resource> resources = new HashSet<Resource>();
		if(r == null || d == null) {
			return resources;
		}
		Set<String> sameAsProps = d.getSameAsProperties();
		String url = r.getUrl();
		if(sameAsProps != null) {
			
			Iterator<String> sameAsIter = sameAsProps.iterator();
			if(!sameAsIter.hasNext()) {
				return resources;
			}
			String subject;
			String object;
			if(reversed) {
				subject = "?r";
				object = "<"+url+">";
			} else {
				subject = "<"+url+">";
				object = "?r";
			}
			String queryString = "SELECT ?r WHERE { " +
	        		"{ "+subject+" <"+sameAsIter.next()+"> "+object+" }";
			while(sameAsIter.hasNext()) {
				queryString += " UNION { "+subject+" <"+sameAsIter.next()+"> "+object+" }";
			}
	        queryString += " }";
	
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(d.getSparqlEndpoint(), query);
	
			try {
			    ResultSet results = qexec.execSelect();
			    while(results.hasNext()) {
			    	QuerySolution row = results.next();
			    	RDFNode node = row.get("r");
			    	Resource foundRes = ResourceFactory.getResource(node.toString());
			    	if(foundRes != null) {
			    		//Update Statistics
			    		if(reversed) {
			    			Statistics.getSingleton().addSameAsMatch(foundRes, r);
			    		} else {
			    			Statistics.getSingleton().addSameAsMatch(r, foundRes);
			    		}
			    		//Add the resources to the result
			    		resources.add(foundRes);
			    	}
			    }
			} catch(Exception e) {
				LOG.warning("Error during query execution and the endpoint: " + d.getSparqlEndpoint() + " for resource: " + r.getUrl());
				LOG.warning("Error message: " + e.fillInStackTrace());
			}
			finally {
			   qexec.close();
			}
		}
		return resources;
	}
	
	public static int findResources(Entity e) {
		Set<Resource> resources = e.getResources();
		int oldResSize = resources.size();
		findResources(resources, resources);
		return resources.size() - oldResSize;
	}
	
	
	private static void findResources(Set<Resource> resources, Set<Resource> resSet) {
		Set<Resource> newResources = new HashSet<Resource>();
		for(Resource r: resSet) {
			Set<Resource> searchRes = ResourceSearcher.findSameAs(r);
			for(Resource foundR : searchRes) {
				if(!resources.contains(foundR)) {
					newResources.add(foundR);
				}
			}
		}
		resources.addAll(newResources);
		if(newResources.size() > 0) {
			findResources(resources, newResources);
		}
	}
}
