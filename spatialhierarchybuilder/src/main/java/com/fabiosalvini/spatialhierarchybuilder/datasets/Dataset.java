package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.hierarchies.HierarchyLevel;

public interface Dataset {

	String getDomain();
	String getSparqlEndpoint();
	Set<String> getSameAsProperties();
	Set<Dataset> getLinkedByDatasets();
	Set<HierarchyLevel> getHierarchyLevels();
	HierarchyLevel getHierarchyLevelFromName(String name);

}
