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
    	for(Entity e: entities) {
    		Set<Resource> r = rDAO.getResourcesOfEntity(e);
    		if(!r.isEmpty()) {
    			e.addResources(r);
    		}
    		Hierarchy h = hDAO.getHierarchy(e);
    		e.setHierarchy(h);
    	}
    	System.out.println(entities.size());
        
    	int count = 1;
        for(Entity e: entities) {
        	ResourceSearcher.findResources(e);
        	HierarchyBuilder.retrieveHierarchy(e);
        	System.out.println(count);
        	count++;
        }
        
        LOG.info("Application end");
    }
}
