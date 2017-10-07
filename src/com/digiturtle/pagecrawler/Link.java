package com.digiturtle.pagecrawler;

public class Link {
	
	private int from, to;
	
	public Link(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	public int getPageFrom() {
		return from;
	}
	
	public int getPageTo() {
		return to;
	}

}
