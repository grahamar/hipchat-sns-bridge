package controllers

import javax.inject.Inject

import com.zaneli.escalade.hipchat.Rooms
import com.zaneli.escalade.hipchat.param.Color
import play.api.mvc._

class HipchatBridge @Inject() (rooms: Rooms) extends Controller {

  def roomNotification(roomId: Int) = Action(parse.tolerantJson) { implicit request =>
    val message = (request.body \ "Message").as[String]
    rooms.message.call(roomId, "AWS Notifications", message, color = Some(Color.Purple))
    Ok
  }

}
