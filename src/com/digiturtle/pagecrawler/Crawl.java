package com.digiturtle.pagecrawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Crawl {
	
	private ArrayList<Page> vertices;
	
	private HashMap<Integer, ArrayList<Link>> edges;
	
	private float successRate;
	
	public Crawl() {
		vertices = new ArrayList<>();
		edges = new HashMap<>();
	}
	
	public void computeRanks(ArrayList<PageRank> ranks) {
		class Pair {
			int from, to;
			public Pair() {
				from = to = 0;
			}
		}
		HashMap<String, Pair> toFromPairs = new HashMap<>();
		HashMap<String, String> titles = new HashMap<>();
		HashMap<String, String> baseUrlMapping = new HashMap<>();
		for (int i = 0; i < vertices.size(); i++) {
			Page page = vertices.get(i);
			String url = page.getURL();
			String protocol = url.substring(0, url.indexOf("//") + 2);
			String baseUrl = url.substring(protocol.length());
			baseUrl = baseUrl.substring(0, baseUrl.indexOf('/'));
			Pair pair = toFromPairs.get(baseUrl);
			if (pair == null) {
				pair = new Pair();
				toFromPairs.put(baseUrl, pair);
			}
			if (baseUrlMapping.containsKey(baseUrl)) {
				if (baseUrlMapping.get(baseUrl).length() > url.length()) {
					baseUrlMapping.put(baseUrl, url);
					titles.put(baseUrl, page.getTitle());
				}
			} else {
				baseUrlMapping.put(baseUrl, url);
				titles.put(baseUrl, page.getTitle());
			}
			pair.from += getLinksFrom(page.getID());
			pair.to += getLinksTo(page.getID());
		}
		for (Map.Entry<String, Pair> entry : toFromPairs.entrySet()) {
			Page page = new Page(createId(entry.getKey()), entry.getKey(), titles.get(entry.getKey()));
			PageRank rank = new PageRank(page, entry.getValue().from, entry.getValue().to);
			ranks.add(rank);
		}
	}
	
	public int getLinksFrom(int pageId) {
		if (!edges.containsKey(pageId)) {
			return 0;
		}
		return edges.get(pageId).size();
	}
	
	public int getLinksTo(int pageId) {
		int linksTo = 0;
		for (Map.Entry<Integer, ArrayList<Link>> edge : edges.entrySet()) {
			for (int i = 0; i < edge.getValue().size(); i++) {
				if (edge.getValue().get(i).getPageTo() == pageId) {
					linksTo++;
				}
			}
		}
		return linksTo;
	}
	
	public float getSuccessRate() {
		return successRate;
	}
	
	public void setSuccessRate(float successRate) {
		this.successRate = successRate;
	}
	
	public ArrayList<Page> getPages() {
		return vertices;
	}
	
	public int createId(String url) {
		return url.hashCode();
	}
	
	public Page getPage(int id) {
		return binarySearch(vertices, 0, vertices.size(), id);
	}
	
	public Page getPage(String url) {
		for (int i = 0; i < vertices.size(); i++) {
			Page page = vertices.get(i);
			if (page.getURL().equalsIgnoreCase(url)) {
				return page;
			}
		}
		return null;
	}
	
	private Page binarySearch(ArrayList<Page> list, int start, int end, int pageId) {
		int mid = (end + start) / 2;
		if (list.get(mid).getID() == pageId) {
			return list.get(mid);
		}
		if (start == end) {
			return null;
		}
		if (list.get(mid).getID() > pageId) {
			return binarySearch(list, start, mid, pageId);
		}
		return binarySearch(list, mid + 1, end, pageId);
	}
	
	public void addPage(Page page) {
		vertices.add(page);
	}
	
	public void addLink(int fromId, int toId) {
		if (edges.containsKey(fromId)) {
			edges.get(fromId).add(new Link(fromId, toId));
		} else {
			ArrayList<Link> edgeList = new ArrayList<>();
			edgeList.add(new Link(fromId, toId));
			edges.put(fromId, edgeList);
		}
	}
	
	public ArrayList<Link> getLinks(int fromId) {
		return edges.get(fromId);
	}
	
}
