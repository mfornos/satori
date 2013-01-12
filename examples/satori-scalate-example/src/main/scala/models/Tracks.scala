package models
import java.util.Date
import scala.collection.immutable.TreeMap
import javax.ws.rs.FormParam
import satori.jersey.params.DateParam
import java.util.concurrent.atomic.AtomicLong
import Option.{apply => ?}
import java.io.InputStream
import com.sun.jersey.multipart.FormDataParam
import com.sun.jersey.core.util.Base64
import com.google.common.io.ByteStreams
import com.sun.jersey.core.header.FormDataContentDisposition
import javax.ws.rs.core.MediaType


object Tracks {
  
  val idGen : AtomicLong = new AtomicLong
  var points: TreeMap[Date, TrackPoint] = TreeMap()(implicitly[Ordering[Date]].reverse)
  def remove(date: DateParam) = points -= date.get
  
  {
    new TrackPoint("Point one", new Date(0)).save()
    new TrackPoint("Point two", new Date(0)).save()
  }
  
}

class FileHolder(mime: MediaType, in : InputStream, point : TrackPoint) {
  
  val _content = "data:%s;base64,%s" format(mime, new  String(Base64.encode(ByteStreams.toByteArray(in))))
  def content = _content  
  Tracks.points.get(point.date).get.file = Some(this)
  
}

class TrackPoint(var _id: Option[Long], var _data: String, _date: Date) {
  
  implicit def dateWrapper(date: java.util.Date) = new utils.Date(date)
  
  def this(@FormParam("id")  _id: Long, @FormParam("txt") _data: String, @FormParam("date") _date : DateParam) = this(if(_id == 0L) None else Some(_id), _data, ?(_date).getOrElse(new DateParam()).get)
  def this(_data: String) = this(None, _data, new Date())
  def this(_data: String, _date: Date) = this(None, _data, _date)
  
  def id = _id
  var file : Option[FileHolder] = None
  val list = scala.collection.mutable.Map[Option[Long], String](data)
  def sorted = list.toSeq.sortBy(_._1).reverse
  def date = _date.noTime
  def dateString = new DateParam(_date).toString
  
  private def data = {
    
    id.getOrElse(_id = Some(Tracks.idGen.incrementAndGet))
    (id -> _data)
  
  }
    
  def save() = {
    Tracks.points.get(date) match {
      case Some(point) =>{
        val kv = data
        point.list.put(kv._1, kv._2)
      }
      case _ => Tracks.points += (date -> this)
    }
    this
  }
  
  def save(in:Option[InputStream], mime: MediaType) {
    save()
    in match {
      case Some(in) => new FileHolder(mime, in, this)
      case None => Nil
    }
  }
  
}
