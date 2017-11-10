package com.contaazul.mde.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import buzz.MyLogService;

@Path("/test")
public class TestResource {

	@Inject
	private MyLogService service;
	// private TestService service;

	@GET
	public String run() {
		return "Porra " + service.getMessage();
	}

}
