package com.pricing.spider.service;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pricing.spider.core.CrawlController;
import com.pricing.spider.model.SearchResult;

@Path("/Actions")
public class ReqController
{
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response insert(@FormParam("searchitem") String item)
	{
		CrawlController controller = new CrawlController();
		SearchResult result = null;
		try {
			result = controller.crawlAndGet(item);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(200).entity(result).build();
	}
}
