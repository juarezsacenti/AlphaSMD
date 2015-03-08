package com.fabiosalvini.spatialhierarchybuilder.statistics;

import java.util.HashMap;
import java.util.Map;

import com.fabiosalvini.spatialhierarchybuilder.resources.Resource;

public class Statistics {
	
	private static Statistics singleton = new Statistics();
	
	private Map<Connection, Integer> sameAsCount;

	private Statistics() {
		sameAsCount = new HashMap<Connection, Integer>();
	}

	public static Statistics getSingleton() {
		return singleton;
	}
	
	public void addSameAsMatch(Resource usedR, Resource foundR) {
		Connection c = new Connection(usedR.getDataset().getDomain(), foundR.getDataset().getDomain());
		Integer count = sameAsCount.get(c);
		count = (count != null) ? count + 1 : 0;
		sameAsCount.put(c, count);
	}

	@Override
	public String toString() {
		String text = "##### Statistics #####\n";
		text += "SameAsCount: \n";
		for(Connection c : sameAsCount.keySet()) {
			text += "  - " + c.getFromDomain() + " -> " + c.getToDomain() +": " + sameAsCount.get(c) +"\n";
		}
		text += "######################";
		return text;
	}

}
