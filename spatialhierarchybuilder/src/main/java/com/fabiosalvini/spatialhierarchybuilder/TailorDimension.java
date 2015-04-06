package com.fabiosalvini.spatialhierarchybuilder;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Writer;
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
import com.fabiosalvini.spatialhierarchybuilder.steps.DimensionTailoring;
import com.fabiosalvini.spatialhierarchybuilder.steps.Facet;
import com.fabiosalvini.spatialhierarchybuilder.steps.HierarchyExtraction;
import com.fabiosalvini.spatialhierarchybuilder.steps.SemanticEnrichment;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.SimpleSelector;

public class TailorDimension {
	
	private static final Logger LOG = Logger.getLogger(TailorDimension.class.getName());
	
    public static void main( String[] args ) throws Exception {

    	FacetDAO.getFacet("Facet2-2");
    	Model hier, model = Facet.getSingleton().openGraph("semovdim.org");
    	Property p = model.getProperty("http://semovdim.org/rdf#categoryHierarchy");
    			
    	hier = ModelFactory.createDefaultModel();
    	hier.add(model.listStatements(new SimpleSelector(null, p, (RDFNode) null)));
    	
        try {
            PrintStream ps= new PrintStream("./TDB/catHierarchySMoD2.rdf");
            hier.write(ps);
            ps.close();
        }
        catch (FileNotFoundException e) { System.out.println(e); }
        
//       	Set<Entity> entities = new EntityDAO().getAllEntities();
//    	ResourceDAO rDAO = new ResourceDAO();
//    	HierarchyDAO hDAO = new HierarchyDAO();
//    	System.out.println("Loading Resources...");
//    	for(Entity e: entities) {
//    		Set<Resource> r = rDAO.getResourcesOfEntity(e);
//    		if(!r.isEmpty()) {
//    			e.addResources(r);
//    			Resource r2 = e.getResource(DbpediaDataset.getSingleton());
//    			if(r2 == null) {
//    				r2 = e.getResource(LinkedGeoDataDataset.getSingleton());
//    			}
//    			e.setIdentifier(r2.getUrl());
//    		}
//    		Hierarchy h = hDAO.getHierarchy(e);
//    		e.setObjHierarchy(h);
//    	}
//    	int total = entities.size();
//    	System.out.println(total);
//  
//    	System.out.println("Retrieving Hierarchy...");
//		Model dim, model = Facet.getSingleton().openGraph("semovdim.org");
//		Property p = model.getProperty("http://semovdim.org/rdf#categoryHierarchy");
//		
//    	int count = 1 ;
//    	for(Entity e: entities) {
//        	DimensionTailoring.loadNumberOfHits(e);
//        	System.out.println(count+" / "+total);
//        	count++;
//        }
//    	for(int i=1;i<=1024;i=i*2) {
//    		dim = DimensionTailoring.tailorDimension(model, p, i);
//    		DimensionTailoring.analyze(dim);
//    	}
    	LOG.info("Application end");
    }
}
