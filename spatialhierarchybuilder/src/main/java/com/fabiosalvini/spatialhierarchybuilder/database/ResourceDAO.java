package com.fabiosalvini.spatialhierarchybuilder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.Entity;
import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;
import com.fabiosalvini.spatialhierarchybuilder.resources.ResourceFactory;

public class ResourceDAO {

	public ResourceDAO() {
	}
	
	public Set<Resource> getResourcesOfEntity(Entity e) {
		Set<Resource> resources = new HashSet<Resource>();
		//Add the GADM resource
		Double lat = e.getLatitude();
		Double lon = e.getLongitude();
		if(lat != null && lon != null) {
			Resource r = ResourceFactory.getResource(lat, lon);
			if(r != null) {
				resources.add(r);
			}
		}
		//Add the resources from DB
		try (Connection con = DBAccess.getConnection()) {
	        String query = "select r.url from position p inner join match m on m.idposition = p.idposition inner join resource r on m.idresource = r.idresource where p.idposition = ?";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setInt(1, e.getId());
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	        	String url = rs.getString("url");
	        	Resource r = ResourceFactory.getResource(url);
	        	if(r != null) {
	        		resources.add(r);
	        	}
	        }
	        con.close();
		} catch(Exception ex) {
		}
        return resources;
	}

}
