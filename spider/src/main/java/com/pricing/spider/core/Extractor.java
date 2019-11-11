package com.pricing.spider.core;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import com.pricing.spider.model.Product;
import com.pricing.spider.model.SearchResult;

import java.io.IOException;
import java.net.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor implements Runnable {
	private String searchItem;
	private SourceDomainEnum domain;
	private SearchResult result;
	private CountDownLatch latch;

	public Extractor(SourceDomainEnum domain, String searchItem, SearchResult result, CountDownLatch latch) {
		this.domain = domain;
		this.searchItem = searchItem;
		this.result = result;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		Product product = getPageLinks();
		this.result.addResult(product);
		this.latch.countDown();
	}

	private Product getPageLinks() {
		try {
			Document document = Jsoup.connect(domain.getURL()).get();

			FormElement searchForm = document.body().select(domain.getFormName()).forms().get(0);
			Elements searchText = searchForm.getElementsByAttributeValue("name", domain.getSearchBoxId());
			searchText.val(this.searchItem);

			Connection.Response res = searchForm.submit().execute();
			Element documentBody = res.parse().body();

			Elements nameList = documentBody.getElementsByClass(this.domain.getItemNameCSS());
			Elements priceList = documentBody.getElementsByClass(this.domain.getPriceClass());

			int noOfItems = documentBody.getElementsByClass(this.domain.getSearchResultCSS()).size();
			
			Product product = new Product();
			product.setDomain(domain);
			System.out.println(domain.name());

			for (int i = 0; i < noOfItems; i++) {
				try {
					Element itemName = nameList.get(i);
					Element price = priceList.get(i);
					double doublePrice = ProductUtils.getIntValue(price.text());
					product.addProduct(itemName.text(), doublePrice);
					System.out.println(itemName.text() + "  " + doublePrice);
				} catch (Exception e) {
					continue;
				}

			}
			document.clearAttributes();
			return product;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}