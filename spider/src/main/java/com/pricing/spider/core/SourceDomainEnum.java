package com.pricing.spider.core;

public enum SourceDomainEnum {
	// AMAZON("https://www.amazon.in/s/ref=nb_sb_ss_sc_1_7?url=search-alias%3Daps&field-keywords=",
	// "a-price-whole"),
	// AMAZON("https://www.amazon.in", "[name=site-search]", "field-keywords",
	// "a-size-medium a-color-base a-text-normal", "a-price-whole"),
	// AMAZON("https://www.amazon.in", "[name=site-search]", "field-keywords",
	// "sg-col-20-of-24 s-result-item sg-col-0-of-12 sg-col-28-of-32
	// sg-col-16-of-20 sg-col sg-col-32-of-36 sg-col-12-of-16 sg-col-24-of-28",
	// "a-size-medium a-color-base a-text-normal", "a-price-whole"),

	AMAZON("https://www.amazon.in", "[name=site-search]", "field-keywords",
			"sg-col-4-of-12 sg-col-8-of-16 sg-col-16-of-24 sg-col-12-of-20 sg-col-24-of-32 sg-col sg-col-28-of-36 sg-col-20-of-28",
			"a-size-medium a-color-base a-text-normal", "a-price-whole"),
	// FLIPKART("https://www.flipkart.com/search?q=", "[_1WMLwI
	// header-form-search]", "q", "_1vC4OE _2rQ-NK");
	// FLIPKART("https://www.flipkart.com", "[action=/search]", "q", "_3wU53n",
	// "_1vC4OE _2rQ-NK");
	FLIPKART("https://www.flipkart.com", "[action=/search]", "q", "_3O0U0u", "_3wU53n", "_1vC4OE _2rQ-NK");
	// MYNTRA("https://www.myntra.com");

	private String url;
	private String formName;
	private String searchBoxId;
	private String searchResultCSS;
	private String itemNameCSS;
	private String priceClassCSS;

	private SourceDomainEnum(String url, String formName, String searchBoxId, String searchResultCSS, String itemNameCSS,
			String priceClass) {
		this.url = url;
		this.formName = formName;
		this.searchBoxId = searchBoxId;
		this.searchResultCSS = searchResultCSS;
		this.itemNameCSS = itemNameCSS;
		this.priceClassCSS = priceClass;
	}

	public String getURL() {
		return this.url;
	}

	public String getFormName() {
		return this.formName;
	}

	public String getSearchBoxId() {
		return this.searchBoxId;
	}

	public String getSearchResultCSS() {
		return this.searchResultCSS;
	}

	public String getItemNameCSS() {
		return this.itemNameCSS;
	}

	public String getPriceClass() {
		return this.priceClassCSS;
	}
}
