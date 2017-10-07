package com.digiturtle.pagecrawler;

import javax.net.ssl.SSLHandshakeException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageCrawler {
	
	class Int {
		int i = 0;
	}

	public Crawl crawl(String sourceUrl, int maxLayers) throws Exception {
		Crawl crawl = new Crawl();
		Int pass = new Int(), fail = new Int();
		crawl(sourceUrl, crawl, 0, maxLayers, pass, fail);
		crawl.setSuccessRate((float) pass.i / (float) (pass.i + fail.i));
		return crawl;
	}
	
	private int crawl(String sourceUrl, Crawl crawl, int layers, int maxLayers, Int pass, Int fail) throws Exception {
		if (layers == maxLayers) {
			return -1;
		}
		if (sourceUrl.contains("mailto:")) {
			return -1; // Don't follow email links
		}
		if (sourceUrl.startsWith("#")) {
			return -1; // Don't follow links on the same page
		}
		System.out.println("Crawling... " + layers + ", " + sourceUrl);
		Document html = null;
		try {
			html = Jsoup.connect(sourceUrl).get();
			pass.i++;
		} catch (Exception e) {
			if (e instanceof HttpStatusException) {
				HttpStatusException http = (HttpStatusException) e;
				if (http.getStatusCode() == 401) { // Access Denied Error
					return -1;
				}
			}
			if (e instanceof UnsupportedMimeTypeException || e instanceof SSLHandshakeException) {
				return -1;
			}
			fail.i++;
			e.printStackTrace(System.err);
			return -1;
		}
		Page page = crawl.getPage(sourceUrl);
		if (page == null) {
			page = new Page(crawl.createId(sourceUrl), sourceUrl, null);
			crawl.addPage(page);
		}
		Elements titles = html.getElementsByTag("title");
		String title = sourceUrl;
		if (titles.size() > 0) {
			title = titles.get(0).text();
		}
		page.setTitle(title);
		Elements links = html.getElementsByTag("a");
		for (int i = 0; i < links.size(); i++) {
			Element link = links.get(i);
			String targetUrl = link.attr("href");
			if (targetUrl.startsWith("#")) {
				// Ignore Links to the Page
			} else {
				if (targetUrl.startsWith("//")) {
					targetUrl = "http:" + targetUrl;
				}
				if (targetUrl.startsWith("http://") || targetUrl.startsWith("https://")) {
					// Full URL
				} else {
					int lastSlash = sourceUrl.lastIndexOf('/');
					if (sourceUrl.charAt(lastSlash - 1) == '/') {
						targetUrl = sourceUrl + "/" + targetUrl;
					} else {
						String baseUrl = sourceUrl;
						String urlWithoutPrefix = sourceUrl.substring(sourceUrl.indexOf("://") + 3);
						String rootUrl = sourceUrl.substring(0, urlWithoutPrefix.indexOf('/') + sourceUrl.indexOf("://") + 3);
						if (!(targetUrl.lastIndexOf('?') < targetUrl.lastIndexOf('.'))) {
							baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf('/'));
						}
						if (baseUrl.endsWith("/")) {
							baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
						}
						String destinationUrl;
						if (targetUrl.startsWith("/")) {
							destinationUrl = rootUrl + targetUrl;
						} else {
							destinationUrl = baseUrl + "/" + targetUrl;
						}
						targetUrl = destinationUrl;
					}
				}
				int toId = crawl(targetUrl, crawl, layers + 1, maxLayers, pass, fail);
				if (toId != -1) {
					crawl.addLink(page.getID(), toId);
				}
			}
		}
		return page.getID();
	}
	
}
