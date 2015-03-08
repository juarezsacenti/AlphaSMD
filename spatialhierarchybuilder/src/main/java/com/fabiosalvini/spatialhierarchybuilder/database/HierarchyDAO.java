package com.fabiosalvini.spatialhierarchybuilder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevel;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevelInstance;

public class HierarchyDAO {

	public HierarchyDAO() {
	}
	
	public void saveHierarchy(Integer idresource, HierarchyLevelInstance hli) throws Exception {
		List<String> params = new LinkedList<String>();
		params.add(hli.getName());
		while(hli.getParent() != null) {
			hli = hli.getParent();
			params.add(0, hli.getName());
		}
		Connection con = DBAccess.getConnection();
        String insertQuery = "INSERT INTO resource_hierarchy (idresource,idhierarchy,level1_name,level2_name,level3_name,level4_name,level5_name,level6_name,level7_name,level8_name,level9_name,level10_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(insertQuery);
        ps.setInt(1, idresource);
        ps.setInt(2, 1);
        int i = 0;
        while(i < 10 && i < params.size()) {
        	ps.setString(i + 3, params.get(i));
        	i++;
        }
        while(i < 10) {
        	ps.setNull(i + 3, Types.NULL);
        	i++;
        }
        ps.executeUpdate();
        con.close();
	}
	
	private void saveHierarchy(HierarchyLevel hl) {
		try {
			List<String> params = new LinkedList<String>();
			params.add(hl.getName());
			while(hl.getParent() != null) {
				hl = hl.getParent();
				params.add(0, hl.getName());
			}
			Connection con = DBAccess.getConnection();
	        String insertQuery = "INSERT INTO hierarchy (level1,level2,level3,level4,level5,level6,level7,level8,level9,level10) VALUES (?,?,?,?,?,?,?,?,?,?)";
	        PreparedStatement ps = con.prepareStatement(insertQuery);
	        int i = 0;
	        while(i < 10 && i < params.size()) {
	        	ps.setString(i + 1, params.get(i));
	        	i++;
	        }
	        while(i < 10) {
	        	ps.setNull(i + 1, Types.NULL);
	        	i++;
	        }
	        ps.executeUpdate();
	        con.close();
		}catch(Exception e) {
			
		}
	}

}
