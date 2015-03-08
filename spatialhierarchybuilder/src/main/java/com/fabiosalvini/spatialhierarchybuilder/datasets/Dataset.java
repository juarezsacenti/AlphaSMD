package com.fabiosalvini.spatialhierarchybuilder.datasets;

import java.util.Set;

public interface Dataset {

	String getDomain();
	String getSparqlEndpoint();
	Set<String> getSameAsProperties();
	Set<Dataset> getLinkedByDatasets();

}
