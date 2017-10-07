package com.digiturtle.pagecrawler.test;

import java.util.ArrayList;
import java.util.Collections;

import com.digiturtle.pagecrawler.Crawl;
import com.digiturtle.pagecrawler.PageCrawler;
import com.digiturtle.pagecrawler.PageRank;

public class TestPageCrawler {

	public static void main(String[] args) throws Exception {
		PageCrawler crawler = new PageCrawler();
		Crawl crawl = crawler.crawl("http://techmeme.com/", 2);
		ArrayList<PageRank> ranks = new ArrayList<>();
		crawl.computeRanks(ranks);
		Collections.sort(ranks);
		for (int i = 0; i < ranks.size(); i++) {
			PageRank rank = ranks.get(i);
			System.out.println(rank.getPage().getTitle() + " -> " + rank.getRank());
		}
	}
	
}
