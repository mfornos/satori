package satori.scala.view.j

import satori.utils.Scala._
import satori.utils.ResponseUtils._

/**
 * A wrapper for java based template engines
 */
class View(path: String = resolveName(), model: Object) extends satori.view.View(path, model) {
    
  def <<(m: Map[String, Any]) =  {
    super.addAll(asJava(m))
    this
  }
  
  def <<(kvs: (String, Any)*) = {
    for(kv <- kvs) this + kv
    this
  }
  
  def +(kv: (String, Any)) = {
    super.add(kv._1, kv._2.asInstanceOf[Object])
    this
  }
  
}

object View {
  def apply() = new View(resolveName(2), null)
  def apply(model: Object) = new View(resolveName(2), model)
  def apply(path: String, model: Object) = new View(path, model)
}
