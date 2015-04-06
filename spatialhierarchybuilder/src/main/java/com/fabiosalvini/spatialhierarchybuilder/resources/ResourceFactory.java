package com.fabiosalvini.spatialhierarchybuilder.resources;

import java.util.HashSet;
import java.util.Set;

import com.fabiosalvini.spatialhierarchybuilder.datasets.Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.DbpediaDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.FreebaseDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GADMDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GeoSpeciesDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GeoVocabDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.GeonamesDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.InseeDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.LinkedGeoDataDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.OWL2Dataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.OntDesPatDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.OpenGisDataset;
import com.fabiosalvini.spatialhierarchybuilder.datasets.SchemaDataset;

public class ResourceFactory {
	
	private static Set<Resource> resources = new HashSet<Resource>();
	
	public static Resource getResource(String url) {
		for(Resource r: resources) {
			if(r.getUrl().equals(url)) {
				return r;
			}
		}
		String domain = getDomain(url);
		
		Resource r = null;
		switch (domain) {
		case DbpediaDataset.DOMAIN:
			if(url.startsWith("http://dbpedia.org")) {
				//Temporary discard all dbpedia resources that starts with a different url (like it.dbpedia.org)
				r = new DbpediaResource(url);
			}			
			break;
		
		case GeonamesDataset.DOMAIN:
			r =  new GeonamesResource(url);
			break;
			
		case LinkedGeoDataDataset.DOMAIN:
			r =  new LinkedGeoDataResource(url);
			break;

		case GADMDataset.DOMAIN:
			r =  new GADMResource(url);
			break;
		
		case OWL2Dataset.DOMAIN:
			r =  new OWL2Resource(url);
			break;
		
		case OntDesPatDataset.DOMAIN:
			r =  new OntDesPatResource(url);
			break;
			
		default:
			break;
		}

		if(r != null) {
			resources.add(r);
		}
		return r;
	}
	
	public static Resource getResource(double latitude, double longitude) {
		String url = "http://gadm.geovocab.org/services/withinRegion?lat="+latitude+"&long="+longitude+"#point";
		return getResource(url);
	}
	
	public static String getDomain(String uri) {
		String domain = null;
		if(uri.contains(DbpediaDataset.DOMAIN)) {
			if(uri.startsWith("http://dbpedia.org")) {
				domain = DbpediaDataset.DOMAIN;
			}
		}
		if(uri.contains(GeonamesDataset.DOMAIN)) {
			domain = GeonamesDataset.DOMAIN;
		}
		if(uri.contains(LinkedGeoDataDataset.DOMAIN)) {
			domain = LinkedGeoDataDataset.DOMAIN;
		}
		if(uri.contains(GADMDataset.DOMAIN)) {
			domain = GADMDataset.DOMAIN;
		} else if(uri.contains(GeoVocabDataset.DOMAIN)) {
			domain = GeoVocabDataset.DOMAIN;
		}
		if(uri.contains(OWL2Dataset.DOMAIN)) {
			domain = OWL2Dataset.DOMAIN;
		}
		if(uri.contains(OntDesPatDataset.DOMAIN)) {
			domain = OntDesPatDataset.DOMAIN;
		}
		if(uri.contains(SchemaDataset.DOMAIN)) {
			domain = SchemaDataset.DOMAIN;
		}
		if(uri.contains(OpenGisDataset.DOMAIN)) {
			domain = OpenGisDataset.DOMAIN;
		}
		if(uri.contains(FreebaseDataset.DOMAIN)) {
			domain = FreebaseDataset.DOMAIN;
		}
		if(uri.contains(GeoSpeciesDataset.DOMAIN)) {
			domain = GeoSpeciesDataset.DOMAIN;
		}
		if(uri.contains(InseeDataset.DOMAIN)) {
			domain = InseeDataset.DOMAIN;
		}
		if(domain == null) {
			System.out.println("### DOMAIN = "+ uri +" ###");
		}
		return domain;
	}
	
	public static Dataset getDataset(String uri) {
		Dataset dataset = null;
		if(uri.contains(DbpediaDataset.DOMAIN)) {
			if(uri.startsWith("http://dbpedia.org")) {
				dataset = DbpediaDataset.getSingleton();
			}
		}
		if(uri.contains(GeonamesDataset.DOMAIN)) {
			dataset = GeonamesDataset.getSingleton();
		}
		if(uri.contains(LinkedGeoDataDataset.DOMAIN)) {
			dataset = LinkedGeoDataDataset.getSingleton();
		}
		if(uri.contains(GADMDataset.DOMAIN)) {
			dataset = GADMDataset.getSingleton();
		} else if(uri.contains(GeoVocabDataset.DOMAIN)) {
			dataset = GeoVocabDataset.getSingleton();
		}
		if(uri.contains(OWL2Dataset.DOMAIN)) {
			dataset = OWL2Dataset.getSingleton();
		}
		if(uri.contains(OntDesPatDataset.DOMAIN)) {
			dataset = OntDesPatDataset.getSingleton();
		}
		if(uri.contains(SchemaDataset.DOMAIN)) {
			dataset = SchemaDataset.getSingleton();
		}
		if(uri.contains(OpenGisDataset.DOMAIN)) {
			dataset = OpenGisDataset.getSingleton();
		}
		if(uri.contains(FreebaseDataset.DOMAIN)) {
			dataset = FreebaseDataset.getSingleton();
		}
		if(uri.contains(GeoSpeciesDataset.DOMAIN)) {
			dataset = GeoSpeciesDataset.getSingleton();
		}
		if(uri.contains(InseeDataset.DOMAIN)) {
			dataset = InseeDataset.getSingleton();
		}
		return dataset;
	}
	
	public static String getURIresourceAsRDF(String uri) {
		String rdfUri;
		switch (getDomain(uri)) {			
		case LinkedGeoDataDataset.DOMAIN:
			rdfUri = uri.replace(".org/triplify", ".org/data/triplify")+"?output=xml";
			break;

		default:
			rdfUri = uri;
			break;
		}

		return rdfUri;
	}
}
