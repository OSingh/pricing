package com.pricing.spider.core;

import com.pricing.spider.model.Product;
import com.pricing.spider.model.SearchResult;

public class Client {

	public static void main(String[] args) throws InterruptedException {
		
		// Extractor bwc = new Extractor();
		// bwc.getPageLinks("https://www.flipkart.com/search?q=Redmi%20Note%207%20Pro%20%28Neptune%20Blue%2C%2064%20GB%29%20%20%284%20GB%20RAM%29&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off");
		// bwc.getPageLinks("https://www.amazon.in/s/ref=nb_sb_ss_sc_1_7?url=search-alias%3Daps&field-keywords=adidas+running+shoes+men");
		// bwc.getArticles();

		CrawlController Crawler = new CrawlController();
		SearchResult result = Crawler.crawlAndGet("Redmi Note 4 Pro mobile (Neptune Blue, 64 GB) (4 GB RAM)");
		/*for (Product produce : result.getDomainList()) {
			String sourceName = produce.getDomain();
			System.out.println(sourceName + ": Total "
					+ produce.getProduct().size() + " items");

			for (int i = 0; i < produce.getProduct().size(); i++) {
				System.out.println(produce.getProduct().get(i));
			}
			System.out.println("\n-------------------------\n");
		}*/
		System.out.println("Total source: " + result.getDomainList().size());
	}

}
