package com.pricing.spider;

import java.util.LinkedList;
import java.util.List;

class SearchResult {
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
