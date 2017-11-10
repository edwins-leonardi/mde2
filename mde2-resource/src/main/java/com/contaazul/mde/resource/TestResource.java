package com.contaazul.mde.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import br.com.mde.TestService;

@Path("/test")
public class TestResource {

	@Inject
	private TestService service;

	@GET
	public String run() {
		return "Porra " + service.getMessage();
	}

}
