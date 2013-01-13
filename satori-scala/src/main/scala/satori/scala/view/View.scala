package satori.scala.view
import satori.utils.ResponseUtils._
import satori.view.SatoriView

class View(_templateName: String = resolveName(), _model: Any = None, _csrfToken: String = null) extends SatoriView {
  
  def model = _model
  def templateName = _templateName
  def csrfToken = _csrfToken
  
  override def getTemplateName() = templateName
  override def getModel() = model.asInstanceOf[AnyRef]
  override def getCsrfToken() = csrfToken
  
}

object View {
  
  def apply() = new View(resolveName(2), None)
  def apply(model: Object) = new View(resolveName(2), model)
  def apply(templateName: String, model: Object) = new View(templateName, model)
  
}

