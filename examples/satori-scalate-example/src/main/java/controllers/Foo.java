package controllers;

import static satori.utils.ResponseUtils.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import satori.view.View;

@Path("/foo")
public class Foo {
    
    @GET
    public View home() {
        return view("Hello scaml!");
    }
    
}
