package com.fabiosalvini.spatialhierarchybuilder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

import com.fabiosalvini.spatialhierarchybuilder.database.FacetDAO;
import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.LinkedGeoDataDataset;
import com.fabiosalvini.spatialhierarchybuilder.steps.Facet;
import com.fabiosalvini.spatialhierarchybuilder.steps.HierarchyExtraction;
import com.fabiosalvini.spatialhierarchybuilder.steps.RDFVisitorURI;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;


public class ResultAnalysis {
	
	private static final Logger LOG = Logger.getLogger(ResultAnalysis.class.getName());
	
    public static void main( String[] args ) throws Exception {
 
    	FacetDAO.getFacet("Facet2-1");

    	Iterator<String> it = Facet.getSingleton().listGraphs().toList().iterator();
    	String modelName;
    	Model datasetModel;
    	Property p;
    	Iterator<Property> itP;
    	Iterator<Statement> itS;
    	Iterator<Resource> itR;
		HashSet<String> resources;
		HashSet<Property> listP;
    	while (it.hasNext()){
			modelName = it.next();
			datasetModel = Facet.getSingleton().openGraph(modelName);
			
			resources = new HashSet<String>();
			itR = datasetModel.listSubjects().toSet().iterator();
			while(itR.hasNext()) {
	        	resources.add(itR.next().getURI());        	
	        }
			
			RDFNode obj;
			RDFVisitorURI rv = new RDFVisitorURI();
			Iterator<RDFNode> objIter = datasetModel.listObjects().toSet().iterator();
			int count;
			while(objIter.hasNext()) {
				obj = objIter.next();
				if(obj.isResource()) {
					resources.add((String) obj.visitWith(rv));
				}
	        }
	        
			System.out.println(modelName+": "+ resources.size() + ", " + datasetModel.size());        	
			
			listP = new HashSet<Property>();
			itS = datasetModel.listStatements();
	        while(itS.hasNext()) {
	        	listP.add(itS.next().getPredicate());
	        }
	        itP = listP.iterator();
	        while(itP.hasNext()) {
	        	p = itP.next();
	        	itS = datasetModel.listStatements(new SimpleSelector(null, p, (RDFNode) null));
	            count = 0;
	        	while(itS.hasNext()) {
	        		itS.next();
	            	count++;
	            }
	        	System.out.println(p.toString()+": "+count);
	        }
    	}   
    	
    	it = Facet.getSingleton().listGraphs().toList().iterator();
		resources = new HashSet<String>();
    	while (it.hasNext()){
			modelName = it.next();
			datasetModel = Facet.getSingleton().openGraph(modelName);

			itR = datasetModel.listSubjects().toSet().iterator();
			while(itR.hasNext()) {
	        	resources.add(itR.next().getURI());        	
	        }
			
			RDFNode obj;
			RDFVisitorURI rv = new RDFVisitorURI();
			Iterator<RDFNode> objIter = datasetModel.listObjects().toSet().iterator();
			while(objIter.hasNext()) {
				obj = objIter.next();
				if(obj.isResource()) {
					resources.add((String) obj.visitWith(rv));
				}
	        }
    	}
    	
		System.out.println("TOTAL resources: "+ resources.size() );        	
   	
    	datasetModel = Facet.getSingleton().openGraph("semovdim.org");
//		p = datasetModel.getProperty("http://www.w3.org/2000/01/rdf-schema#subClassOf");
    	p = datasetModel.getProperty("http://semovdim.org/rdf#numberOfHits");
    	itS = datasetModel.listStatements(new SimpleSelector(null, p, (RDFNode) null));
        int count = 0;
    	while(itS.hasNext()) {
        	itS.next();
        	count++;
        }
    	System.out.println("numberOfHits: "+count);

    	Dataset singleton = LinkedGeoDataDataset.getSingleton();
		Property p2 = datasetModel.getProperty("http://www.w3.org/2000/01/rdf-schema#subClassOf");

        HashSet<com.hp.hpl.jena.rdf.model.Resource> fathers = new HashSet<com.hp.hpl.jena.rdf.model.Resource>();
    	com.hp.hpl.jena.rdf.model.Resource bakery, shop, amenity;
    	
    	bakery = datasetModel.getResource("http://linkedgeodata.org/ontology/Bakery");
    	shop = datasetModel.getResource("http://linkedgeodata.org/ontology/Shop");
    	amenity = datasetModel.getResource("http://linkedgeodata.org/ontology/Amenity");
    	fathers.add(bakery);
    	fathers.add(shop);
    	fathers.add(amenity);
    	
    	LOG.info("Application end");
    }
}
