package com.pricing.spider.core;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.pricing.spider.model.SearchResult;

public class CrawlController {

	// Call below method from resource
	public SearchResult crawlAndGet(final String item) throws InterruptedException {
		CrawlerPool l_pool = new CrawlerPool();
		List<SourceDomainEnum> sourceDomainEnum = Arrays.asList(SourceDomainEnum.values());
		CountDownLatch latch = new CountDownLatch(sourceDomainEnum.size());

		SearchResult result = new SearchResult();
		try {
			for (SourceDomainEnum domain : sourceDomainEnum) {
				Extractor bwc = new Extractor(domain, item, result, latch);
				l_pool.execute(bwc);
			}
			latch.await();
			return result;
		} finally {
			l_pool.shutdown();
		}
	}

}