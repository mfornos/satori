package controllers

import java.io.InputStream
import com.sun.jersey.api.core.InjectParam
import com.sun.jersey.core.header.FormDataContentDisposition
import com.sun.jersey.multipart.{FormDataParam, FormDataBodyPart}
import javax.ws.rs.{Consumes,DELETE,GET,POST,Path,QueryParam}
import javax.ws.rs.core.MediaType
import models.{TrackPoint,Tracks}
import satori.jersey.params.DateParam
import satori.scala.view.View
import satori.utils.ResponseUtils.{ok,seeOther}
import satori.view.flash.Flash
import javax.ws.rs.core.Context
import satori.validation.BeanValidator

@Path("/track")
class Track  (@Context flash: Flash) {
  
  @GET
  def home = View(Tracks.points)
  
  @POST
  def postHome(@InjectParam point: TrackPoint) = {
    
    point.save()
    seeOther("/track")
    
  }
  
  @POST
  @Consumes(Array(MediaType.MULTIPART_FORM_DATA))
  def postFile(@FormDataParam("image") in: InputStream, 
               @FormDataParam("image") detail: FormDataContentDisposition,
               @FormDataParam("image") body: FormDataBodyPart, 
               @FormDataParam("txt") data : String, 
               @FormDataParam("date") date : DateParam) = {
    
    new TrackPoint(data).save(if (detail.getFileName.nonEmpty) Some(in) else None, body.getMediaType())
    seeOther("/track")
    
  }
  
  @POST
  @Path("edit")
  def edit(@InjectParam point: TrackPoint) = point.save()._data
  
  @DELETE
  def delete(@QueryParam("date") date: DateParam) = {
    Tracks.remove(date)
    ok()
  }
  
}
