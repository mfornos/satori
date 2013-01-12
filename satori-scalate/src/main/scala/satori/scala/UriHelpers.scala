package satori.scala

import org.fusesource.scalate.servlet.ServletRenderContext
import satori.i18n.Translation._
import satori.utils.PathUtils
import javax.ws.rs.core.UriBuilder

object UriHelpers {
  
  def msg(key: String)(implicit renderContext: ServletRenderContext): String = translate(key)
  
  def <(path: String, args: Any*)(implicit renderContext: ServletRenderContext): String = <(uriBuilder(path), args: _*)
  
  def <(uri: UriBuilder, args: Any*)(implicit renderContext: ServletRenderContext): String = {
    var u = 
    Option.apply(args) match {
      case Some(a) => uri.build(a.map(_.asInstanceOf[AnyRef]) : _*)
      case None => uri
    }
    renderContext.contextPath + PathUtils.absolutize(u.toString())
  }
  
  def uriBuilder(path: String): UriBuilder = UriBuilder.fromPath(path)
  
}