package satori.controllers;

import static satori.utils.ResponseUtils.notFound;
import static satori.utils.ResponseUtils.view;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import satori.view.View;

@Path("/helloworld")
public class DummyController {

    @GET
    @Path("hello")
    public View hello() {

        return view("/hello", "World áàíôü");

    }

    @GET
    public View home() {

        return view("/home", "Lorem ipsum");

    }

    @GET
    @Path("bad")
    public Response bad() {

        return notFound();

    }

}
