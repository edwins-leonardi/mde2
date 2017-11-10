package com.contaazul.mde.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("status")
public class StatusResource {
	@GET
	public String getStatus() {
		return "OK";
	}
}
