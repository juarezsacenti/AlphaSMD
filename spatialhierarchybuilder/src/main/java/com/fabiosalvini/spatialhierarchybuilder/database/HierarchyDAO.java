package com.fabiosalvini.spatialhierarchybuilder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.fabiosalvini.spatialhierarchybuilder.Entity;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevelInstance;

public class HierarchyDAO {

	public HierarchyDAO() {
	}
	
	public void saveHierarchy(Entity e, HierarchyLevelInstance hli) throws Exception {
		Connection con = DBAccess.getConnection();
		List<HierarchyLevelInstance> hliList = new LinkedList<HierarchyLevelInstance>();
		while(hli != null) {
			hliList.add(0, hli);
			hli = hli.getParent();
		}
		Integer elementId = null;
		Integer levelId = null;
		for(HierarchyLevelInstance element : hliList) {
			levelId = getHierarchyLevelId(con, element.getLevel().getName(), levelId);
			elementId = getHierarchyLevelInstanceId(con, element.getName(), levelId, elementId);
		}
		String query = "INSERT INTO position_hlevel_instance (idposition,idhlevel_instance) VALUES (?,?)";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, e.getId());
		ps.setInt(2, elementId);
		ps.executeUpdate();
        con.close();
	}
	
	public int getHierarchyLevelId(Connection con, String name, Integer parentId) throws Exception {
		String query;
		if(parentId != null) {
			query = "SELECT idhierarchy_level FROM hierarchy_level WHERE name=? AND parent_hierarchy_level=?";
		} else {
			query = "SELECT idhierarchy_level FROM hierarchy_level WHERE name=? AND parent_hierarchy_level IS NULL";
		}
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, name);
		if(parentId != null) {
			ps.setInt(2, parentId);
		}
		ResultSet rs = ps.executeQuery();
        if (rs.next()) {
        	return rs.getInt("idhierarchy_level");
        	
        }
        query = "INSERT INTO hierarchy_level (name, parent_hierarchy_level) VALUES (?,?)";
        ps = con.prepareStatement(query);
        ps.setString(1, name);
        if(parentId != null) {
			ps.setInt(2, parentId);
		} else {
			ps.setNull(2, Types.NULL);
		}
        ps.executeUpdate();
        
        return getHierarchyLevelId(con, name, parentId);
	}
	
	public int getHierarchyLevelInstanceId(Connection con, String name, Integer levelId, Integer parentId) throws Exception {
		String query;
		if(parentId != null) {
			query = "SELECT idhlevel_instance FROM hlevel_instance WHERE name=? AND idhierarchy_level=? AND parent_hlevel_instance=?";
		} else {
			query = "SELECT idhlevel_instance FROM hlevel_instance WHERE name=? AND idhierarchy_level=? AND parent_hlevel_instance IS NULL";
		}
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, name);
		ps.setInt(2,levelId);
		if(parentId != null) {
			ps.setInt(3, parentId);
		}
		ResultSet rs = ps.executeQuery();
        if (rs.next()) {
        	return rs.getInt("idhlevel_instance");
        	
        }
        query = "INSERT INTO hlevel_instance (name,idhierarchy_level,parent_hlevel_instance) VALUES (?,?,?)";
        ps = con.prepareStatement(query);
        ps.setString(1, name);
        ps.setInt(2,levelId);
        if(parentId != null) {
			ps.setInt(3, parentId);
		} else {
			ps.setNull(3, Types.NULL);
		}
        ps.executeUpdate();
        
        return getHierarchyLevelInstanceId(con, name, levelId, parentId);
	}

}
