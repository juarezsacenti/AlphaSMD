package com.fabiosalvini.spatialhierarchybuilder.services;


public class Level implements Comparable<Level>{

	String label;
	String level;
	
	public Level(String label, String level) {
		super();
		this.label = label;
		this.level = level;
	}

	public int compareTo(Level arg0) {
		Level other = ((Level)arg0);
		if(level.equals(other.level)) {
			return 0;
		}
		if(level.equals("Country")) {
			return -1;
		} else if(other.level.equals("Country")){
			return 1;
	    }if(level.length() < other.level.length()) {
			return 1;
		}
		return level.compareTo(other.level);
	}

}
