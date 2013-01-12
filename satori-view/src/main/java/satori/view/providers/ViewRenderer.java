package satori.view.providers;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;

import satori.view.SatoriView;

public interface ViewRenderer {

    boolean accepts(SatoriView view);

    void render(SatoriView view, ViewContext context, OutputStream out) throws IOException, WebApplicationException;

}
