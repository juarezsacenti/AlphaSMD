package com.fabiosalvini.spatialhierarchybuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.fabiosalvini.spatialhierarchybuilder.database.DBAccess;
import com.fabiosalvini.spatialhierarchybuilder.database.HierarchyDAO;
import com.fabiosalvini.spatialhierarchybuilder.resources.GADMResource;
import com.fabiosalvini.spatialhierarchybuilder.resources.GeonamesResource;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;
import com.fabiosalvini.spatialhierarchybuilder.resources.ResourceFactory;
import com.fabiosalvini.spatialhierarchybuilder.statistics.Statistics;


public class App {
	
	private static final Logger LOG = Logger.getLogger(App.class.getName());
	
    public static void main( String[] args ) throws Exception {
    	Set<Entity> entities = new HashSet<Entity>();
        
        Connection con = DBAccess.getConnection();
        
        LOG.info("Retrieving resources from DB");
        String urlsQuery = "SELECT l.text, r.idresource, r.url FROM resource r INNER JOIN label l ON l.idresource = r.idresource WHERE NOT EXISTS (SELECT * FROM resource_hierarchy rh WHERE rh.idresource=r.idresource)";
        PreparedStatement ps = con.prepareStatement(urlsQuery);
        java.sql.ResultSet rs = ps.executeQuery();
        while (rs.next()) {
        	String label = rs.getString("text");
        	Integer idresource = rs.getInt("idresource");
        	String url = rs.getString("url");
        	Resource r = ResourceFactory.getResource(idresource, url);
        	if(r != null) {
        		Entity e = new Entity(label);
        		e.addResource(r);
        		entities.add(e);
        	}
        }
        con.close();
        
        LOG.info("Populating entities resources");
        
        for(Entity e : entities) {
        	LOG.info("Elaborating entity: " + e.getLabel());
        	e.findResources();
        	Set<Resource> resources = e.resources;
        	for(Resource r: resources) {
        		if(r instanceof GeonamesResource) {
        			GeonamesResource gr = (GeonamesResource) r;
        			gr.retrieveHierarchy();
        			HierarchyDAO hdao = new HierarchyDAO();
        			hdao.saveHierarchy(gr.getIdresource(), gr.getHierarchyLevelInstance());
        			//gr.printHierarchy();
        			continue;
        		}
        		if(r instanceof GADMResource) {
        			GADMResource gr = (GADMResource) r;
        			gr.retrieveHierarchy();
        			HierarchyDAO hdao = new HierarchyDAO();
        			hdao.saveHierarchy(gr.getIdresource(), gr.getHierarchyLevelInstance());
        			//gr.printHierarchy();
        		}
        	}
        }
        
        System.out.println(Statistics.getSingleton().toString());
        
        LOG.info("Application end");
    }
}
