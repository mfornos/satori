package views

import org.fusesource.scalate.TemplateSource
import org.fusesource.scalate.support.TemplatePackage
import org.fusesource.scalate.Binding


/**
 * Defines some common imports, attributes and methods across templates in package foo and below
 */
class ScalatePackage extends TemplatePackage {

  /** Returns the Scala code to add to the top of the generated template method */
   def header(source: TemplateSource, bindings: List[Binding]) = """
     
// some shared imports
import satori.scala.UriHelpers._

  """
}