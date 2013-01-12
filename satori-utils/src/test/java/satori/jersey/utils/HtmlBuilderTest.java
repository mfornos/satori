package satori.jersey.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import satori.utils.Html;

public class HtmlBuilderTest {

    @Test
    public void simpleTest() {
        Html builder = Html.builder();
        builder.begin("ul").attr("class", "menu").begin("li").text("Hello").end("li").end("ul");
        assertEquals("<ul class=\"menu\"><li>Hello</li></ul>", builder.emit());
    }

    @Test
    public void closeTest() {
        Html builder = Html.builder();
        builder.begin("ul").attr("class", "menu").close().begin("li").close().text("Hello").end("li").end("ul");
        assertEquals("<ul class=\"menu\"><li>Hello</li></ul>", builder.emit());

        builder = Html.builder();
        builder.begin("i").attr("class", "ico").close().end();
        assertEquals("<i class=\"ico\"></i>", builder.emit());
    }

    @Test
    public void attrsTest() {
        Html builder = Html.builder();
        builder.begin("p").attrs("class", "test", "id", "p1");
        assertEquals("<p class=\"test\" id=\"p1\" />", builder.emit());
    }

    @Test
    public void writeTest() {
        Html builder = Html.builder();
        builder.write("<p class=\"test\" id=\"p1\">");
        assertEquals("<p class=\"test\" id=\"p1\">", builder.emit());
    }

    @Test
    public void endsTest() {
        Html builder = Html.builder();
        builder.begin("ul").attr("class", "menu").begin("li").text("Hello").end().end();
        assertEquals("<ul class=\"menu\"><li>Hello</li></ul>", builder.emit());

        builder = Html.builder();
        builder.begin("p").begin("img").attr("src", "test.png").end().end();
        assertEquals("<p><img src=\"test.png\" /></p>", builder.emit());

        builder = Html.builder();
        builder.begin("p").begin("img").attr("src", "test.png").endAll();
        assertEquals("<p><img src=\"test.png\" /></p>", builder.emit());

        builder = Html.builder();
        builder.begin("p").begin("img").attr("src", "test.png");
        assertEquals("<p><img src=\"test.png\" /></p>", builder.emit());
    }

}
