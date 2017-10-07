package com.digiturtle.pagecrawler;

public class Page implements Comparable<Page> {
	
	private int id;
	
	private String url, title;
	
	public Page(int id, String url, String title) {
		this.id = id;
		this.url = url;
		this.title = title;
	}
	
	public int getID() {
		return id;
	}
	
	public String getURL() {
		return url;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int compareTo(Page otherPage) {
		return Integer.compare(id, otherPage.id);
	}

}
