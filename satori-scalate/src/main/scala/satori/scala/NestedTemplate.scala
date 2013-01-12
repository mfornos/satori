package satori.scala
import org.fusesource.scalate.filter.Filter
import org.fusesource.scalate.RenderContext

trait NestedTemplate {
    
  def templateExt(context: RenderContext): String = {
      context.currentTemplate.substring(context.currentTemplate.lastIndexOf(".") + 1)
  }
  
  def render(context: RenderContext, content: String, lang: Option[String] = None): String = {
      val ext = lang match {
        case Some(l) => l
        case None => templateExt(context)
      }
      context.capture(context.engine.compileText(ext, content).render(context))
  }
  
}