package com.pricing.spider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class CrawlController {

	// Call below method from resource
	public SearchResult crawlAndGet(final String item) throws InterruptedException {
		CrawlerPool l_pool = new CrawlerPool();
		List<SourceDomain> sourceDomain = Arrays.asList(SourceDomain.values());
		CountDownLatch latch = new CountDownLatch(sourceDomain.size());

		SearchResult result = new SearchResult();
		try {
			for (SourceDomain domain : sourceDomain) {
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