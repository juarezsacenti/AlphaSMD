package com.fabiosalvini.spatialhierarchybuilder.steps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.fabiosalvini.spatialhierarchybuilder.Entity;
import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.DbpediaDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.LinkedGeoDataDataset;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;
import com.fabiosalvini.spatialhierarchybuilder.resources.ResourceFactory;
import com.fabiosalvini.spatialhierarchybuilder.statistics.Statistics;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;

public class SemanticEnrichment {
	
	private static final Logger LOG = Logger.getLogger(SemanticEnrichment.class.getName());
	public static HashMap<String,Integer> numberOfHits = new HashMap<String,Integer>();
	public static HashSet<String> visitedResource = new HashSet<String>();

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
		Resource r = e.getResource(DbpediaDataset.getSingleton());
		if(r == null) {
			r = e.getResource(LinkedGeoDataDataset.getSingleton());
		}
		e.setIdentifier(r.getUrl());
		return resources.size() - oldResSize;
	}
		
	private static void findResources(Set<Resource> resources, Set<Resource> resSet) {
		Set<Resource> newResources = new HashSet<Resource>();
		for(Resource r: resSet) {
			Set<Resource> searchRes = SemanticEnrichment.findSameAs(r);
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

	public static void retrieveAssertions(Entity e, Dataset d, String p, HashSet<String> scope) {
		Resource r = e.getResource(d);
		if(r != null) {
			String uri = r.getUrl();
			if(!numberOfHits.containsKey(uri)) {
				retrieveAssertions(r, p, scope);
				numberOfHits.put(uri, 1);
			} else {
				numberOfHits.put(uri, numberOfHits.get(uri) + 1);
			}
//TODO remove print
			System.out.println(uri+" ::: "+ numberOfHits.get(uri));
		}
	}
	
	public static void retrieveAssertions(Entity e, Dataset d, List<String> lstP, HashSet<String> scope) {
		Resource r = e.getResource(d);
		if(r != null) {
			String uri = r.getUrl();
			if(!numberOfHits.containsKey(uri)) {
				for(String p : lstP) {
					retrieveAssertions(r, p, scope);
				}
				numberOfHits.put(uri, 1);
			} else {
				numberOfHits.put(uri, numberOfHits.get(uri) + 1);
			}
//TODO remove print
			System.out.println(uri+" ::: "+ numberOfHits.get(uri));
		}
	}
	
	public static void retrieveAssertions(Resource r, String property, HashSet<String> scope) {
		Facet mf = Facet.getSingleton();
		String datasetName = r.getDataset().getDomain();
		Model datasetModel = mf.openGraph(datasetName);
        
		Model model = ModelFactory.createDefaultModel();
		String url = r.getUrl();
		try {
			model.read(url);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		String uri;
		Property p = model.getProperty(property);
		com.hp.hpl.jena.rdf.model.Resource datasetR, datasetObj, jenaR = model.getResource(url);
		if(p != null && jenaR != null) {
            datasetR = datasetModel.createResource(url);
			Iterator<RDFNode> objIter = model.listObjectsOfProperty(jenaR, p);
			RDFNode obj;
            while(objIter.hasNext()) {
            	obj = objIter.next();
            	if(obj.isResource()) {
            		uri = obj.asResource().getURI();
	            	if(isInScope(uri, scope)){
	            		datasetObj = datasetModel.createResource(uri);
	            		datasetModel.add(datasetR, p, datasetObj);
	            	}
            	} else {
            		datasetModel.add(datasetR, p, obj);
            	}
            }
        }
	}
	


	public static void retrieveTransitiveAssertions(Dataset singleton,
			String p1, String p2, HashSet<String> scope) {
		Model model = Facet.getSingleton().openGraph(singleton.getDomain());
		Property p = model.getProperty(p1);

		int accum;
		RDFNode obj;
		com.hp.hpl.jena.rdf.model.Resource father;
		String sonURI, fatherURI;
		Statement st;
		Iterator<Statement> stIter = model.listStatements(new SimpleSelector(null, p, (RDFNode) null));
        while(stIter.hasNext()) {
        	st = stIter.next();
        	obj = st.getObject();
        	if(obj.isResource()) {
        		fatherURI =  st.getResource().getURI();
        		father = model.createResource(fatherURI);
        		sonURI = st.getSubject().getURI();

            	if(!numberOfHits.containsKey(fatherURI)) {
	    			accum = numberOfHits.get(sonURI);
	    			numberOfHits.put(fatherURI, 0+accum);
	    		} else if(numberOfHits.containsKey(fatherURI)) {
	    			accum = numberOfHits.get(fatherURI);
	    			numberOfHits.put(fatherURI, (accum + numberOfHits.get(sonURI)));
	    		}
            	System.out.println(">>>"+fatherURI);
        		
        		if(isInScope(fatherURI, scope)){
                	System.out.println("OK");
        			retrieveTransitiveAssertions(father, singleton, p2, scope);
        		}
        	}
        }
	}
	
	private static void retrieveTransitiveAssertions(
			com.hp.hpl.jena.rdf.model.Resource r, Dataset singleton,
			String property, HashSet<String> scope) {
		String url = r.getURI();
		if(!visitedResource.contains(url)){
			visitedResource.add(url);
			
			Facet mf = Facet.getSingleton();
			String datasetName = singleton.getDomain();
			Model datasetModel = mf.openGraph(datasetName);
	        
			Model model = ModelFactory.createDefaultModel();
			try {
				model.read(url);
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}
			
			Property p = model.getProperty(property);
			RDFNode obj;
			com.hp.hpl.jena.rdf.model.Resource father;
			String fatherURI;
			RDFVisitorURI rv = RDFVisitorURI.getSingleton();
			//TODO remove print
        	System.out.println("["+ r +", "+ p);
			if(p != null && r != null) {
	            Iterator<RDFNode> objIter = model.listObjectsOfProperty(r, p);
	            while(objIter.hasNext()) {
	            	obj = objIter.next();
	        		fatherURI = (String) obj.visitWith(rv);
	        		father = datasetModel.createResource(fatherURI);
	        		datasetModel.add(r, p, father);
//TODO remove print
	            	System.out.println("["+ r +", "+ p +", "+ father +"]");

	        		if(isInScope(fatherURI, scope)){
	            		retrieveTransitiveAssertions(father, singleton, property, scope);
	            	}
	            }
			}
		}
	}

	public static void findEquivalentClass(String mappingURI) {
		Facet mf = Facet.getSingleton();
		Model model = ModelFactory.createDefaultModel();
//TODO remove print
System.out.println("-");
System.out.println("Find equivalent classes...");
System.out.println("-");
		
		try {
			model.read(mappingURI);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		RDFVisitorMapping rv = RDFVisitorMapping.getSingleton();
		RDFVisitorMapping.setModel(model);
		Property p = model.getProperty("http://www.w3.org/2002/07/owl#equivalentClass");
        Iterator<Statement> stIter = model.listStatements(new SimpleSelector(null, p, (RDFNode) null));
        Statement st;
        com.hp.hpl.jena.rdf.model.Resource sub, obj;
        Model dm1, dm2, datasetModel = mf.openGraph("semovdim.org");
  		while(stIter.hasNext()) {
        	st = stIter.next();
        	obj = (com.hp.hpl.jena.rdf.model.Resource) st.getObject().visitWith(rv);
        	sub = st.getSubject();
        
        	dm1 = mf.openGraph(ResourceFactory.getDomain(sub.getURI()));
        	dm2 = mf.openGraph(ResourceFactory.getDomain(obj.getURI()));
        	
        	if(dm1.containsResource(sub) && dm2.containsResource(obj)) {
            	datasetModel.add(st.getSubject(), st.getPredicate(), obj);
//TODO remove print
            	System.out.println("["+ sub +", "+ st.getPredicate() +", "+ obj +"]");
        	}
        }
	}

	public static void saveSameAs(Entity e) {
        Model datasetModel = Facet.getSingleton().openGraph("semovdim.org");
		Property p = datasetModel.getProperty("http://www.w3.org/2002/07/owl#sameAs");
		com.hp.hpl.jena.rdf.model.Resource sub, obj;
		for(Resource r : e.getResources()) {
			sub = datasetModel.createResource(r.getUrl());
			for(Resource same : e.getResources()) {
				obj = datasetModel.createResource(same.getUrl());
				datasetModel.add(sub, p, obj);	
			}
		}		
	}
	
	private static boolean isInScope(String uri, HashSet<String> scope) {
		boolean resourceInScope = false;
		Iterator<String> strIter = scope.iterator();
		while(!resourceInScope && strIter.hasNext()) {
			resourceInScope = uri.contains(strIter.next());
		}
		return resourceInScope;
	}
}
