package com.fabiosalvini.spatialhierarchybuilder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import com.fabiosalvini.spatialhierarchybuilder.Entity;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.Hierarchy;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyElement;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevel;

public class HierarchyDAO {

	public HierarchyDAO() {
	}
	
	public void createHierarchy(Entity e) throws Exception {
		Connection con = DBAccess.getConnection();
        String query = "select * from hierarchy where idposition=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, e.getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
        	return;
        } else {
        	String queryInsert = "insert into hierarchy (idposition) values(?)";
	        PreparedStatement psInsert = con.prepareStatement(queryInsert);
	        psInsert.setInt(1, e.getId());
	        psInsert.executeUpdate();
        }
	}
	
	public void saveHierarchy(Entity e) throws Exception {
		Connection con = DBAccess.getConnection();
		Hierarchy h = e.getHierarchy();
		if(h == null || h.isEmpty()) {
			return;
		}
		createHierarchy(e);
		String query = "UPDATE hierarchy SET continent=?,country=?,admn1=?,admn2=?,admn3=?,admn4=?,admn5=?,city=?,address=? WHERE idposition=?";
		PreparedStatement ps = con.prepareStatement(query);
		HierarchyElement elem;
		elem = h.getElementAtLevel(HierarchyLevel.CONTINENT);
		if(elem != null) {
			ps.setString(1, elem.getName());
		} else {
			ps.setNull(1, Types.NULL);
		}
		elem = h.getElementAtLevel(HierarchyLevel.COUNTRY);
		if(elem != null) {
			ps.setString(2, elem.getName());
		} else {
			ps.setNull(2, Types.NULL);
		}
		elem = h.getElementAtLevel(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_1);
		if(elem != null) {
			ps.setString(3, elem.getName());
		} else {
			ps.setNull(3, Types.NULL);
		}
		elem = h.getElementAtLevel(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_2);
		if(elem != null) {
			ps.setString(4, elem.getName());
		} else {
			ps.setNull(4, Types.NULL);
		}
		elem = h.getElementAtLevel(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_3);
		if(elem != null) {
			ps.setString(5, elem.getName());
		} else {
			ps.setNull(5, Types.NULL);
		}
		elem = h.getElementAtLevel(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_4);
		if(elem != null) {
			ps.setString(6, elem.getName());
		} else {
			ps.setNull(6, Types.NULL);
		}
		elem = h.getElementAtLevel(HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_5);
		if(elem != null) {
			ps.setString(7, elem.getName());
		} else {
			ps.setNull(7, Types.NULL);
		}
		elem = h.getElementAtLevel(HierarchyLevel.CITY);
		if(elem != null) {
			ps.setString(8, elem.getName());
		} else {
			ps.setNull(8, Types.NULL);
		}
		elem = h.getElementAtLevel(HierarchyLevel.ADDRESS);
		if(elem != null) {
			ps.setString(9, elem.getName());
		} else {
			ps.setNull(9, Types.NULL);
		}
		ps.setInt(10, e.getId());
		ps.executeUpdate();
        con.close();
	}
	
	public Hierarchy getHierarchy(Entity e) {
		Hierarchy hierarchy = new Hierarchy();
		if(e == null) {
			return null;
		}
		try (Connection con = DBAccess.getConnection()) {
	        String query = "select * from hierarchy where idresource=";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setInt(1, e.getId());
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	        	String continent = rs.getString("continent");
	        	if(continent != null) {
	        		HierarchyElement elem = new HierarchyElement(continent,HierarchyLevel.CONTINENT);
	        		hierarchy.addHierarchyElement(elem);
	        	}
	        	String country = rs.getString("country");
	        	if(country != null) {
	        		HierarchyElement elem = new HierarchyElement(country,HierarchyLevel.COUNTRY);
	        		hierarchy.addHierarchyElement(elem);
	        	}
	        	String admn1 = rs.getString("admn1");
	        	if(admn1 != null) {
	        		HierarchyElement elem = new HierarchyElement(admn1,HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_1);
	        		hierarchy.addHierarchyElement(elem);
	        	}
	        	String admn2 = rs.getString("admn2");
	        	if(admn2 != null) {
	        		HierarchyElement elem = new HierarchyElement(admn2,HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_2);
	        		hierarchy.addHierarchyElement(elem);
	        	}
	        	String admn3 = rs.getString("admn3");
	        	if(admn3 != null) {
	        		HierarchyElement elem = new HierarchyElement(admn3,HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_3);
	        		hierarchy.addHierarchyElement(elem);
	        	}
	        	String admn4 = rs.getString("admn4");
	        	if(admn4 != null) {
	        		HierarchyElement elem = new HierarchyElement(admn4,HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_4);
	        		hierarchy.addHierarchyElement(elem);
	        	}
	        	String admn5 = rs.getString("admn5");
	        	if(admn5 != null) {
	        		HierarchyElement elem = new HierarchyElement(admn1,HierarchyLevel.ADMINISTRATIVE_AREA_LEVEL_5);
	        		hierarchy.addHierarchyElement(elem);
	        	}
	        	String city = rs.getString("city");
	        	if(city != null) {
	        		HierarchyElement elem = new HierarchyElement(city,HierarchyLevel.CITY);
	        		hierarchy.addHierarchyElement(elem);
	        	}
	        	String address = rs.getString("address");
	        	if(address != null) {
	        		HierarchyElement elem = new HierarchyElement(address,HierarchyLevel.ADDRESS);
	        		hierarchy.addHierarchyElement(elem);
	        	}
	        	
	        }
	        con.close();
		} catch(Exception ex) {
		}
        return hierarchy;
	}

}
