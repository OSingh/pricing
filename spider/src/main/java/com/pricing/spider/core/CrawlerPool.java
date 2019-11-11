package com.pricing.spider.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CrawlerPool {
	private static ThreadPoolExecutor service = null;

	public CrawlerPool() {
		service = new ThreadPoolExecutor(5, 7, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	public void execute(Extractor task) {
		service.submit(task);
	}

	public void shutdown() {
		if (null != service) {
			service.shutdown();
		}
	}
}