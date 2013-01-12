package satori.scala

import org.fusesource.scalate.{TemplateEngine, TemplateEngineAddOn}
import org.fusesource.scalate.filter.Filter
import org.fusesource.scalate.RenderContext
import satori.i18n.TextFlow

object TextFlowFilter extends Filter with TemplateEngineAddOn with NestedTemplate {

  def filter(context: RenderContext, content: String) = {
    TextFlow.emit(render(context, content)).stripLineEnd
  }

  /**
   * Add the text flow filter to the template engine.
   */
  def apply(te: TemplateEngine) = {
    te.filters += "flow"->TextFlowFilter
    te.pipelines += "textflow"->List(TextFlowFilter)
  }
  
}