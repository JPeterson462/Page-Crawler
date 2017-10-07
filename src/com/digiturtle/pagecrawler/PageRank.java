package com.digiturtle.pagecrawler;

public class PageRank implements Comparable<PageRank> {
	
	private Page page;
	
	private int fromCount, toCount;
	
	public PageRank(Page page, int fromCount, int toCount) {
		this.page = page;
		this.fromCount = fromCount;
		this.toCount = toCount;
	}
	
	public Page getPage() {
		return page;
	}
	
	public int getFromCount() {
		return fromCount;
	}

	public int getToCount() {
		return toCount;
	}

	public int getRank() {
		return toCount;
	}

	@Override
	public int compareTo(PageRank o) {
		return Integer.compare(getRank(), o.getRank());
	}

}
