package utils
import java.util.Calendar

class Date(date: java.util.Date) {
  
  def noTime = { 
    val cal: Calendar =  Calendar.getInstance()
    cal.setTime(date)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)  
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0) 
    cal.getTime
  }
  
  def get = date
  
}