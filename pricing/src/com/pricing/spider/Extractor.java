package com.pricing.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor implements Callable<IDomain> {
    private List<String> articles;
    private String[] cssClass = {"a-price-whole", "_1vC4OE _2rQ-NK"};
    private static Map<SourceDomain, String> map = new HashMap();
    private String URI;
    private SourceDomain domain;

    public Extractor(String URI, SourceDomain domain) {
    	this.URI = URI;
    	this.domain = domain;
        articles = new ArrayList<>();
    }
    
    static {
		List<SourceDomain> sourceDomain = Arrays.asList(SourceDomain.values());
		for(SourceDomain source : sourceDomain) {
			map.put(source, source.getPriceClass());
		}
    }

    //Find all URLs that start with "http://www.mkyong.com/page/" and add them to the HashSet
    private IDomain getPageLinks() {
            try {
            	Document document = Jsoup.connect(URI).get();
                //Elements body = document.body().select("href");
                //System.out.println(URI);
                //System.out.println(document.title());
                Elements articleLinks = document.body().getElementsByClass(map.get(domain));
                List<String> price = new LinkedList<>();
                for (Element article : articleLinks) {
                    //System.out.println("Price: " + article.text());
                    price.add(article.text());
                }
                IDomain modal;
                switch(domain) {
                case AMAZON:
                	modal = new Amazon();
                	break;
                case FLIPKART:
                	modal = new Amazon();
                	break;
                default:
                	modal = null;
                }
                modal.setProduce(document.title());
                modal.setPrice(price);
                return modal;
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
			return null;
    }
    
    //Connect to each link saved in the article and find all the articles in the page
/*    private void getArticles() {
        links.forEach(x -> {
            Document document;
            try {
                document = Jsoup.connect(x).get();
                System.out.println(x);
                System.out.println(document.title());
                for(int i = 0; i < cssClass.length; i++) {
                    Elements articleLinks = document.getElementsByClass(cssClass[i]);
                    for (Element article : articleLinks) {
                    	System.out.println("Price: " + article.text());
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
    }*/

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //Extractor bwc = new Extractor();
       // bwc.getPageLinks("https://www.flipkart.com/search?q=Redmi%20Note%207%20Pro%20%28Neptune%20Blue%2C%2064%20GB%29%20%20%284%20GB%20RAM%29&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off");
       // bwc.getPageLinks("https://www.amazon.in/s/ref=nb_sb_ss_sc_1_7?url=search-alias%3Daps&field-keywords=adidas+running+shoes+men");
        //bwc.getArticles();
    	
    	CrawlController Crawler = new CrawlController();
    	List<Future<IDomain>> future = Crawler.crawlAndGet("redmi note 7 pro neptune blue 64 gb 4 gb ram");	
    	for(Future<IDomain> futureee : future) {
    		System.out.println(futureee.get().getProduct());
    		System.out.println(futureee.get().getPrice());
    	}

    }

	@Override
	public IDomain call() {
		return getPageLinks();
	}
}

enum SourceDomain {
	AMAZON("https://www.amazon.in/s/ref=nb_sb_ss_sc_1_7?url=search-alias%3Daps&field-keywords=", "a-price-whole"),
	FLIPKART("https://www.flipkart.com/search?q=", "_1vC4OE _2rQ-NK");
	//MYNTRA("https://www.myntra.com");
	
	private String url;
	private String priceClass;
	
	private SourceDomain(String url, String priceClass) {
		this.url = url;
		this.priceClass = priceClass;
	}
	
	public String getURL() {
		return this.url;
	}
	public String getPriceClass() {
		return this.priceClass;
	}
}

class CrawlController {
	
	//Call below method from resource
	public List<Future<IDomain>> crawlAndGet(String item) {
		CrawlerPool l_pool = new CrawlerPool();
		List<SourceDomain> sourceDomain = Arrays.asList(SourceDomain.values());
		List<Future<IDomain>> future = new LinkedList<>();

		try {
			for(SourceDomain domain : sourceDomain)
			{
				String URI = getURI(domain, item);
		        Extractor bwc = new Extractor(URI, domain);
		        future.add(l_pool.execute(bwc));
			}
			return future;
		}		
		finally {
			l_pool.shutdown();
		}
	}
	
	public String getURI(SourceDomain domain, String item) {
		switch(domain) {
		case FLIPKART:
			return FlipkartURI.getURI(item);
		case AMAZON:
			return AmazonURI.getURI(item);
			default:
				return null;
		}
	}
	
}

class CrawlerPool {
	private static ThreadPoolExecutor service = null;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private Future<IDomain> future;

	public CrawlerPool() {
		service = new ThreadPoolExecutor(5, 7, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
	}
	
    public Future<IDomain> execute(Extractor task){
    	return service.submit(task);
    }
    
    public void shutdown(){
        if(null != service) {
            service.shutdown();
        }
    }
}

class FlipkartURI {
	private static final String END = "&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off";
	private static final String regex = "[,()\\s]";
	private static final Map<Character, String> SpecialCharMap = new HashMap();
	
	static {
		SpecialCharMap.put(',', "%2C");
		SpecialCharMap.put('(', "%28");
		SpecialCharMap.put(')', "%29");
		SpecialCharMap.put(' ', "%20");
	}
	
	public static String getURI(String uri) {
		StringBuilder flipkartURI = new StringBuilder(uri);
		Matcher matcher = Pattern.compile(regex).matcher(uri);
		int i = 0;
		while(matcher.find()) {
			flipkartURI.replace(matcher.start() + i, (matcher.start() + i+1), SpecialCharMap.get(uri.charAt(matcher.start())));
			i+=2;
		}
		return SourceDomain.FLIPKART.getURL() + flipkartURI.toString() + END;
	}
}

class AmazonURI {
	private static final String regex = "[\\s]";
	private static final String END = "%29&i=electronics&ref=nb_sb_noss";

	public static String getURI(String uri) {
		StringBuilder amazonURI = new StringBuilder(uri);
		Matcher matcher = Pattern.compile(regex).matcher(uri);
		while(matcher.find()) {
			amazonURI.replace(matcher.start(), (matcher.start() + 1), "+");
		}
		return SourceDomain.AMAZON.getURL() + amazonURI.toString();
	}
}

interface IDomain {
	void setPrice(List<String> price);
	String getProduct();
	void setProduce(String string);
	List<String> getPrice();
}

class Flipkart implements IDomain {
	List<String> price;
	String product;
	
	public List<String> getPrice() {
		return this.price;
	}
	public String getProduct() {
		return this.product;
	}
	
	public void setPrice(List<String> price) {
		this.price = price;
	}
	public void setProduce(String produce) {
		this.product = produce;
	}
}
class Amazon implements IDomain {
	List<String> price;
	String product;
	
	public List<String> getPrice() {
		return this.price;
	}
	public String getProduct() {
		return this.product;
	}
	
	public void setPrice(List<String> price) {
		this.price = price;
	}
	public void setProduce(String produce) {
		this.product = produce;
	}
}