package com.fabiosalvini.spatialhierarchybuilder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.Entity;

public class EntityDAO {

	public EntityDAO() {
	}
	
	public Set<Entity> getAllEntities() {
		Set<Entity> entities = new HashSet<Entity>();
		try (Connection con = DBAccess.getConnection()) {
	        String query = "select idposition, text, lat, lon from position where idposition in (select idposition from match)";
	        //String query = "select idposition, text, lat, lon from position where idposition in (select idposition from match) AND idposition not in (select idposition from hierarchy)";
	        PreparedStatement ps = con.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	        	Integer idposition = rs.getInt("idposition");
	        	String text = rs.getString("text");
	        	Double lat = rs.getDouble("lat");
	        	Double lon = rs.getDouble("lon");
	        	Entity e = new Entity(idposition, text);
	        	e.setLatitude(lat);
	        	e.setLongitude(lon);
	        	entities.add(e);
	        }
	        con.close();
		} catch(Exception e) {
		}
        return entities;
	}

}
