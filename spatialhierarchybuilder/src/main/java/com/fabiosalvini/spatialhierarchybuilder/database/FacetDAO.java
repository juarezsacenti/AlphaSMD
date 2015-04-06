package com.fabiosalvini.spatialhierarchybuilder.database;

import java.io.File;
import java.nio.file.Files;
import java.util.Iterator;

import com.fabiosalvini.spatialhierarchybuilder.steps.Facet;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;

public abstract class FacetDAO {

	public static void createFacet(String name) throws Exception {
		File repo = new File(TDBAccess.getTDBDirectory() + name);
		repo.mkdirs();
		
		File source = new File(TDBAccess.getTDBDirectory() + "tdb-default.cfg");
		File dest = new File(TDBAccess.getTDBDirectory() + name + "/tdb.cfg");
		
	    Files.copy(source.toPath(), dest.toPath());
	}
	
	public static void saveFacet(String name) throws Exception {
		Dataset dataset = TDBAccess.getConnection(name);
		Facet facet = Facet.getSingleton();
		Iterator<String> it = facet.listGraphs().toList().iterator();
		String modelName;
		
		dataset.begin(ReadWrite.WRITE) ;
		try {
		    Model model = dataset.getDefaultModel() ;
			model.add(facet.getGraph());
			
			while (it.hasNext()){
				modelName = it.next();
				if(modelName != null) {
					dataset.addNamedModel(modelName, facet.openGraph(modelName));
				}
			}
			
			dataset.commit() ;
		} finally { 
			dataset.end() ; 
		}
	}
	
	public static void getFacet(String name) {
		Dataset dataset = TDBAccess.getConnection(name);
		Facet facet = Facet.getSingleton();
		String modelName;
		Model model;
		
		facet.close();
		
		dataset.begin(ReadWrite.READ) ;
		try {
		    model = facet.getGraph();
		    model.add(dataset.getDefaultModel());
		
		    Iterator<String> it = dataset.listNames();
			while (it.hasNext()){
				modelName =  it.next();
				model = facet.openGraph(modelName);
			    model.add(dataset.getNamedModel(modelName));
			}
			
			dataset.commit() ;
		} finally { 
			dataset.end() ; 
		}	
	}
}
