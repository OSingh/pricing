package com.pricing.spider;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Product {
	private SourceDomain domain;
	List<Double> price;
	List<String> name;

	Product(SourceDomain domain) {
		this.domain = domain;
		price = new LinkedList<>();
		name = new LinkedList<>();
	}

	public String getDomain() {
		return this.domain.name();
	}
	
	public List<Double> getPrice() {
		return this.price;
	}

	public List<String> getProduct() {
		return this.name;
	}

	public void addPrice(double price) {
		this.price.add(price);
	}

	public void addProduce(String produce) {
		this.name.add(produce);
	}
}