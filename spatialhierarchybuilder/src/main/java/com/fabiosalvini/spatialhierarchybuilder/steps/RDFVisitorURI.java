package com.fabiosalvini.spatialhierarchybuilder.steps;

import java.net.MalformedURLException;
import java.net.URL;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;

public class RDFVisitorURI implements RDFVisitor {

	private static RDFVisitorURI singleton = new RDFVisitorURI();

	public String visitBlank(Resource r, AnonId id) {
		throw new IllegalArgumentException("Object must be a URI");
	}

	public String visitURI(Resource r, String uri) {
		return uri;
	}
	
	public String visitLiteral(Literal l) {
		System.out.println(l);
		try {
			URL url3 = new URL(l.getString());
			return url3.toString(); 
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Object must be a URI");
		}
	}

	public static RDFVisitorURI getSingleton() {
		return singleton;
	}
}
