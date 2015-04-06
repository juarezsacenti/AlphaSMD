package com.fabiosalvini.spatialhierarchybuilder;

import java.util.Set;
import java.util.logging.Logger;

import com.fabiosalvini.spatialhierarchybuilder.database.EntityDAO;
import com.fabiosalvini.spatialhierarchybuilder.database.FacetDAO;
import com.fabiosalvini.spatialhierarchybuilder.database.HierarchyDAO;
import com.fabiosalvini.spatialhierarchybuilder.database.ResourceDAO;
import com.fabiosalvini.spatialhierarchybuilder.datasets.DbpediaDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.LinkedGeoDataDataset;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.Hierarchy;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;
import com.fabiosalvini.spatialhierarchybuilder.steps.Facet;
import com.fabiosalvini.spatialhierarchybuilder.steps.HierarchyExtraction;
import com.fabiosalvini.spatialhierarchybuilder.steps.SemanticEnrichment;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;


public class DefineHierarchy {
	
	private static final Logger LOG = Logger.getLogger(DefineHierarchy.class.getName());
	
    public static void main( String[] args ) throws Exception {
    	Model datasetModel;

    	FacetDAO.getFacet("Facet2-1");

    	String p1 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", p2 = "http://www.w3.org/2000/01/rdf-schema#subClassOf";
    	HierarchyExtraction.removeAntecipatedAssociations(LinkedGeoDataDataset.getSingleton(), p1, p2);
    	HierarchyExtraction.removeAntecipatedAssociations(DbpediaDataset.getSingleton(), p1, p2);

    	HierarchyExtraction.removeAntecipatedAssociations(LinkedGeoDataDataset.getSingleton(), p2, p2);
    	HierarchyExtraction.removeAntecipatedAssociations(DbpediaDataset.getSingleton(), p2, p2);
    	
    	Property property;
    	StmtIterator itS;
    	int count1, count2; 
    	
    	datasetModel = Facet.getSingleton().openGraph(LinkedGeoDataDataset.getSingleton().getDomain());
    	property = datasetModel.getProperty(p1);
    	itS = datasetModel.listStatements(new SimpleSelector(null, property, (RDFNode) null));
        count1 = 0;
    	while(itS.hasNext()) {
        	itS.next();
        	count1++;
        }
    	property = datasetModel.getProperty(p2);
    	itS = datasetModel.listStatements(new SimpleSelector(null, property, (RDFNode) null));
        count2 = 0;
    	while(itS.hasNext()) {
        	itS.next();
        	count2++;
        }
    	System.out.println(LinkedGeoDataDataset.getSingleton().getDomain() +": "+count1+", "+count2);
 
    	datasetModel = Facet.getSingleton().openGraph(DbpediaDataset.getSingleton().getDomain());
    	property = datasetModel.getProperty(p1);
    	itS = datasetModel.listStatements(new SimpleSelector(null, property, (RDFNode) null));
        count1 = 0;
    	while(itS.hasNext()) {
        	itS.next();
        	count1++;
        }
       	property = datasetModel.getProperty(p2);
    	itS = datasetModel.listStatements(new SimpleSelector(null, property, (RDFNode) null));
        count2 = 0;
    	while(itS.hasNext()) {
        	itS.next();
        	count2++;
        }
    	System.out.println(DbpediaDataset.getSingleton().getDomain() +": "+count1+", "+count2);
    	
    	Set<Entity> entities = new EntityDAO().getAllEntities();
    	ResourceDAO rDAO = new ResourceDAO();
    	HierarchyDAO hDAO = new HierarchyDAO();
    	System.out.println("Loading Resources...");
    	for(Entity e: entities) {
    		Set<Resource> r = rDAO.getResourcesOfEntity(e);
    		if(!r.isEmpty()) {
    			e.addResources(r);
    		}
    		Hierarchy h = hDAO.getHierarchy(e);
    		e.setObjHierarchy(h);
    	}
    	int total = entities.size();
    	System.out.println(total);
  
    	System.out.println("Retrieving Hierarchy...");
		Model model = Facet.getSingleton().openGraph("semovdim.org");
		model.createProperty("http://semovdim.org/rdf#categoryHierarchy");
		
    	int count = 1 ;
    	for(Entity e: entities) {
        	SemanticEnrichment.findResources(e);
        	HierarchyExtraction.retrieveCatHierarchy(e);
        	HierarchyExtraction.saveNumberOfHits(e);
        	System.out.println(count+" / "+total);
        	count++;
        }
//    	HierarchyExtraction.saveNumberOfHits();
    	
    	FacetDAO.createFacet("Facet2-2");
    	FacetDAO.saveFacet("Facet2-2");
    	
        
    	LOG.info("Application end");    	
    }
}
