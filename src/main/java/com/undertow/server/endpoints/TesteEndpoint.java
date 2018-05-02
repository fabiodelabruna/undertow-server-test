package com.undertow.server.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/pessoas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TesteEndpoint {

    private static Logger LOGGER = Logger.getLogger(TesteEndpoint.class);

    @POST
    @Path("/adicionar")
    public void listar(@Suspended AsyncResponse response, String body) {
        LOGGER.info(body.trim());
        response.resume(Response.ok().build());
    }

}
