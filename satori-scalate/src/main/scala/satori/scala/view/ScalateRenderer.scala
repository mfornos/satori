package satori.scala.view

import java.io.OutputStream
import java.lang.String

import org.fusesource.scalate.servlet.ServletHelper
import org.fusesource.scalate.servlet.ServletTemplateEngine
import org.fusesource.scalate.servlet.TemplateEngineServlet
import org.fusesource.scalate.support.TemplateFinder
import org.fusesource.scalate.util.Log
import org.fusesource.scalate.util.ResourceNotFoundException

import ScalateRenderer.debug
import javax.servlet.ServletContext
import javax.ws.rs.ext.Provider
import javax.ws.rs.WebApplicationException
import satori.config.Configuration
import satori.security.CsrfHelper
import satori.view.providers.ViewContext
import satori.view.providers.ViewRenderer
import satori.view.SatoriView

object ScalateRenderer extends Log

/**
 * Renders a [[satori.view.SatoriView]] using the Scalate template engine
 *
 */
@Provider
class ScalateRenderer(config: Configuration, servletContext: ServletContext) extends ViewRenderer {
  import ScalateRenderer._

  protected var errorUris: List[String] = ServletHelper.errorUris()

  val basePath = config.get("scalate.templates.path", "/views")

  val engine = ServletTemplateEngine(servletContext)

  val finder = new TemplateFinder(engine)

  def accepts(view: SatoriView) = {
    val template = basePath + view.getTemplateName
    !finder.findTemplate(template).isEmpty
  }

  def render(view: SatoriView, context: ViewContext, out: OutputStream): Unit = {

    val request = context.request
    val response = context.response

    def render(template: String) = TemplateEngineServlet.render(template, engine, servletContext, request, response)

    try {
      val template = basePath + view.getTemplateName
      finder.findTemplate(template) match {
        case Some(name) =>
          debug("Attempting to generate View for %s", name)

          response.setCharacterEncoding("UTF-8")
          request.setAttribute("it", view.getModel)
          request.setAttribute(CsrfHelper.CSRF_TOKEN_PARAM, view.getCsrfToken)
          render(name)

        case _ =>
          throw new ResourceNotFoundException(template)
      }
    } catch {
      case e: Exception =>
        // lets forward to the error handler
        var notFound = true
        for (uri <- errorUris if notFound) {
          if (servletContext.getResource(uri) != null) {

            // we need to expose all the errors property here...
            request.setAttribute("javax.servlet.error.exception", e)
            request.setAttribute("javax.servlet.error.exception_type", e.getClass)
            request.setAttribute("javax.servlet.error.message", e.getMessage)
            request.setAttribute("javax.servlet.error.request_uri", request.getRequestURI)
            request.setAttribute("javax.servlet.error.servlet_name", request.getServerName)

            // TODO how to get the status code???
            val status = 500
            request.setAttribute("javax.servlet.error.status_code", status)

            request.setAttribute("it", e)
            render(uri)
            notFound = false
          }
        }
        if (notFound) {
          throw new WebApplicationException(e)
        }
    } finally {
      // Ensure headers are committed
      out.flush()
    }
  }

}

