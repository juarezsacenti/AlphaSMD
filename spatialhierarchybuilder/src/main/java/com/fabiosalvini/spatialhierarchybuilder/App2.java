package com.fabiosalvini.spatialhierarchybuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.fabiosalvini.spatialhierarchybuilder.database.EntityDAO;
import com.fabiosalvini.spatialhierarchybuilder.database.FacetDAO;
import com.fabiosalvini.spatialhierarchybuilder.database.HierarchyDAO;
import com.fabiosalvini.spatialhierarchybuilder.database.ResourceDAO;
import com.fabiosalvini.spatialhierarchybuilder.datasets.DbpediaDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GADMDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GeonamesDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.LinkedGeoDataDataset;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.Hierarchy;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;
import com.fabiosalvini.spatialhierarchybuilder.steps.Facet;
import com.fabiosalvini.spatialhierarchybuilder.steps.HierarchyExtraction;
import com.fabiosalvini.spatialhierarchybuilder.steps.SemanticEnrichment;
import com.hp.hpl.jena.rdf.model.Model;


public class App2 {
	
	private static final Logger LOG = Logger.getLogger(App2.class.getName());
	
    public static void main( String[] args ) throws Exception {
    	// STEP 0: LOAD INPUT
    	//manually: create connection "postgresql.table" (modify database.DBAccess)
    	//manually: create KB dataset connection (database.TDBAccess)
    	//import semantically annotated MoD from "postgres.table"
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
        
    	int count = 1 ;

    	// STEP 1: ENRICH RESOURCE
    	FacetDAO.createFacet("Test1");
    	Model datasetModel = Facet.getSingleton().openGraph("semovdim.org");
		datasetModel.createProperty("http://www.w3.org/2002/07/owl#sameAs");
    	
		HashSet<String> scope = new HashSet<String>();
		scope.add(DbpediaDataset.getSingleton().getDomain());
		scope.add(LinkedGeoDataDataset.getSingleton().getDomain());
		scope.add(GeonamesDataset.getSingleton().getDomain());
		
    	System.out.println("Enriching Resources...");
        for(Entity e: entities) {
        	//find sameAs-resources from a KB dataset connection
        	SemanticEnrichment.findResources(e); // TODO add scope
        	SemanticEnrichment.saveSameAs(e);    // TODO put inside findResources
        	
        	//import resources' assertions from a KB dataset connection
        	SemanticEnrichment.retrieveAssertions(e, DbpediaDataset.getSingleton(), "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", scope);
        	SemanticEnrichment.retrieveAssertions(e, LinkedGeoDataDataset.getSingleton(), "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", scope);
        	
        	List<String> lstP = new ArrayList<String>();
        	lstP.add("http://www.geonames.org/ontology#featureCode");
        	lstP.add("http://www.geonames.org/ontology#featureClass");
        	SemanticEnrichment.retrieveAssertions(e, GeonamesDataset.getSingleton(), lstP, scope);
        	SemanticEnrichment.retrieveAssertions(e, GADMDataset.getSingleton(), "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", scope);

        	System.out.println(count+" / "+total);
        	count++;
//        	if(count > 5) {
//        		break;
//        	}
        }
        
        //import resources' indirect assertions from KB dataset connection
    	SemanticEnrichment.retrieveTransitiveAssertions(
    			DbpediaDataset.getSingleton(),
    			"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
    			"http://www.w3.org/2000/01/rdf-schema#subClassOf",
    			scope);
    	
    	SemanticEnrichment.retrieveTransitiveAssertions(
    			LinkedGeoDataDataset.getSingleton(),
    			"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
    			"http://www.w3.org/2000/01/rdf-schema#subClassOf",
    			scope);
    	
    	SemanticEnrichment.retrieveTransitiveAssertions(
    			GADMDataset.getSingleton(),
    			"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
    			"http://www.w3.org/2000/01/rdf-schema#subClassOf",
    			scope);
    	
    	SemanticEnrichment.findEquivalentClass("http://www.geonames.org/ontology/mappings_v3.01.rdf");
    	
//        SemanticEnrichment.saveNumberOfHits();
        FacetDAO.saveFacet("Test1");

        // Removing anticipated associations
//        String p1 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", p2 = "http://www.w3.org/2000/01/rdf-schema#subClassOf";
//    	HierarchyExtraction.removeAntecipatedAssociations(LinkedGeoDataDataset.getSingleton(), p1, p2);
//    	HierarchyExtraction.removeAntecipatedAssociations(DbpediaDataset.getSingleton(), p1, p2);
//
//    	HierarchyExtraction.removeAntecipatedAssociations(LinkedGeoDataDataset.getSingleton(), p2, p2);
//    	HierarchyExtraction.removeAntecipatedAssociations(DbpediaDataset.getSingleton(), p2, p2);

//      System.out.println("Retrieving Hierarchy...");
//    	//manually: create hierarchy levels (hierarchies.HierarchyLevel, database.HierarchyDAO, dataset.LODDataset
//    	//manually: create hierarchy (hierarchy table in postgres, database.hierarchyDAO)
//    	DefineHierarchy.retrieveObjHierarchy(e);

//    	DefineHierarchy.createLeavesNumberOfHits(e);

        //MISSING: integrate resource collection
        
//        for(Entity e: entities) {
//        	DefineHierarchy.createNonLeavesNumberOfHits(e);
//        }
//        
//        DefineHierarchy.saveNumberOfHits();
//        
//        for(Entity e: entities) {
//        	DefineHierarchy.retrieveCatHierarchy(e);
//        }
//        
    	// STEP 2: DEFINE HIERARCHIES
    	//MISSING: save hierarchies in facet
    	
        // STEP 3: TAILOR DIMENSION
        //MISSING: create numberOfHits assertions for Spatial Object hierarchy
    	//MISSING: tailor dimension
		    	
    	LOG.info("Application end");
    }
}
