package com.se.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * Filter that returns a response with headers that allows for Cross-Origin
 * Requests (CORs) to be performed against the platform API.
 */
public class ResponseCorsFilter implements ContainerResponseFilter {

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

		ResponseBuilder resp = Response.fromResponse(response.getResponse());
		resp.header("Access-Control-Allow-Origin", "*").header(
				"Access-Control-Allow-Methods",
				"OPTIONS, GET, POST, PUT, DELETE");

		String reqHead = request.getHeaderValue("Access-Control-Request-Headers");

		if (null != reqHead && !reqHead.equals(null)) {
			resp.header("Access-Control-Allow-Headers", reqHead);
		}

		response.setResponse(resp.build());
		return response;
	}

}
