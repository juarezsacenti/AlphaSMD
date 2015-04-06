package com.fabiosalvini.spatialhierarchybuilder.steps;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;

public class RDFVisitorMapping implements RDFVisitor {

	private static RDFVisitorMapping singleton = new RDFVisitorMapping();
	private static Model model;

	public Resource visitBlank(Resource r, AnonId id) {
		Resource ret = null;
		Property p = model.getProperty("http://www.w3.org/2002/07/owl#hasValue");
		Iterator<RDFNode> objIter = model.listObjectsOfProperty(r, p);
		RDFVisitorURI rv = RDFVisitorURI.getSingleton();
		String uri;
		if(objIter.hasNext()) {
			uri = (String) objIter.next().visitWith(rv);
			ret = model.getResource(uri);
		}
		return ret;
	}

	public Resource visitURI(Resource r, String uri) {
		return r;
	}
	
	public Resource visitLiteral(Literal l) {
		System.out.println(l);
		try {
			URL uri = new URL(l.getString());
			return ResourceFactory.createResource(uri.toString()); 
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Object must be a URI");
		}
	}

	public static RDFVisitorMapping getSingleton() {
		return singleton;
	}
	
	public static void setModel(Model m) {
		model = m;
	}
}
