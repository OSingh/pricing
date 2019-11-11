package com.pricing.spider.model;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchResult {
	private List<Product> products;

	public SearchResult() {
		this.products = new LinkedList<>();
	}

	public List<Product> getDomainList() {
		return products;
	}

	public void addResult(Product product) {
		this.products.add(product);
	}
}
