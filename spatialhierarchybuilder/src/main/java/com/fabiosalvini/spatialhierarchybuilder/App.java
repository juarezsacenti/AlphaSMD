package com.fabiosalvini.spatialhierarchybuilder;

import java.util.Set;
import java.util.logging.Logger;

import com.fabiosalvini.spatialhierarchybuilder.database.EntityDAO;
import com.fabiosalvini.spatialhierarchybuilder.database.HierarchyDAO;
import com.fabiosalvini.spatialhierarchybuilder.database.ResourceDAO;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.Hierarchy;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;
import com.fabiosalvini.spatialhierarchybuilder.services.HierarchyBuilder;
import com.fabiosalvini.spatialhierarchybuilder.services.ResourceSearcher;


public class App {
	
	private static final Logger LOG = Logger.getLogger(App.class.getName());
	
    public static void main( String[] args ) throws Exception {
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
        
    	int count = 1 + hDAO.getNumberOfLeaves();

        for(Entity e: entities) {
        	System.out.println("Finding Resources...");
        	ResourceSearcher.findResources(e);
        	System.out.println("Retrieving Hierarchy...");
        	HierarchyBuilder.retrieveHierarchy(e);
        	System.out.println(count+" / "+total);
        	count++;
        }
        
        LOG.info("Application end");
    }
}
