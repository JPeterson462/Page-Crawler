package com.digiturtle.pagecrawler;

import java.util.ArrayList;

public class PageRanker {
	
	public ArrayList<PageRank> compuateRanking(Crawl crawl) {
		ArrayList<PageRank> ranks = new ArrayList<>();
		ArrayList<Page> pages = crawl.getPages();
		for (int i = 0; i < pages.size(); i++) {
			Page page = pages.get(i);
			int fromCount = crawl.getLinksFrom(page.getID()), toCount = crawl.getLinksTo(page.getID());
			ranks.add(new PageRank(page, fromCount, toCount));
		}
		return ranks;
	}

}
