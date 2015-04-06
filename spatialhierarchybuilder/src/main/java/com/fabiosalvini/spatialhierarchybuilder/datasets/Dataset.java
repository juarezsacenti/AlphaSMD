package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.CatHierarchyLevel;
import com.fabiosalvini.spatialhierarchybuilder.hierarchies.ObjHierarchyLevel;

public interface Dataset {

	String getDomain();
	String getSparqlEndpoint();
	Set<String> getSameAsProperties();
	Set<Dataset> getLinkedByDatasets();
	Set<ObjHierarchyLevel> getObjHierarchyLevels();
	ObjHierarchyLevel getObjHierarchyLevelFromName(String name);
	Set<CatHierarchyLevel> getCatHierarchyLevels();
	CatHierarchyLevel getCatHierarchyLevelFromName(String name);
}
