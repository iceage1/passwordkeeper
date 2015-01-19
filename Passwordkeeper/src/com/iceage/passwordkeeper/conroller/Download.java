package com.iceage.passwordkeeper.conroller;
import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;


@Path("/getResource")
public class Download {

	private static final String FILE_PATH = "d:\\ptc\\a\\test.zip";
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFile() {
	    File file = new File(FILE_PATH);
	    ResponseBuilder response = Response.ok((Object) file);
	    response.header("Content-Disposition",
	        "attachment; filename=newfile.zip");
	    return response.build();

	}
} 
