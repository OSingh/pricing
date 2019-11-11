package com.pricing.spider.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.pricing.spider.core.SourceDomainEnum;

@XmlRootElement
public class Product {
	private SourceDomainEnum domain;
	private Map<String, Double> map = new HashMap<>();
	
	public void setDomain(SourceDomainEnum domain) {
		this.domain = domain;
	}

	public String getDomain() {
		return this.domain.name();
	}
	
	public void addProduct(String name, double price) {
		this.map.put(name, price);
	}
	
	public Map<String, Double> getProduct() {
		return this.map;
	}

}