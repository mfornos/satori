package controllers;

import static satori.utils.ResponseUtils.view;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import satori.view.View;
import db.H2;

@Path("/metro")
public class Metro {

    @GET
    public View home(@QueryParam("q") String q, @QueryParam("p") int p) {

        return view(H2.search(q, p));

    }
    
    @GET
    @Path("/card/{id}")
    public View card(@PathParam("id")String id) {

        return view(H2.get(id));

    }

}
