/*
 * Copyright 2011 Weigle Wilczek GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.weiglewilczek.chatter
package comet

import lib.{ ChatterServer, Message }
import java.text.DateFormat
import net.liftweb.http.{ CometActor, CometListener }
import net.liftweb.util.ClearClearable
import model.{MessageDB, Follow, User}
import net.liftweb.common.{Empty, Box, Full, Loggable}
import collection.SeqLike._

object  ChatterClient {
 def clientNodeSeq(userIds: List[String]) = {

   val cl = "lift:comet?type=chatterClient;name=" + userIds.mkString(",")

   <div id="messages" class={ cl } >
    <h3>Timeline</h3>
    <div id="message">
      <div><span class="sender">John Doe</span> at <span class="date">Jan 24, 2011 10:36:14 AM</span></div>
      <div class="text">Here is message 1.</div>
    </div>
   </div>
  }
}

class ChatterClient extends CometActor with CometListener with Loggable {

  override def lowPriority = {
    case ms: Vector[Message] => {
      logger.debug("Received messages: %s".format(ms))
      def following(message: Message) = {
        //logger debug "sender id = %s, user id = %s".format(message.sender.id, User.currentUserId.open_!)
        //logger debug "test MATCH = %s".format(message.sender.id.toString == (User.currentUserId openOr error("No current user!")))
        (message.sender.id.toString == (User.currentUserId openOr error("No current user!"))) ||
            (Follow.findAllFollowing map { _.id.is.toString } contains message.sender.id.toString)
      }

      //logger debug "unFiltered Messages : %s".format(ms)
      val filtered = ms filter following
      //logger debug "Filtered Messages : %s".format(filtered)
      messages = (filtered diff messages) ++ messages
      logger debug "Messages to render are: %s".format(messages)
      reRender()
    }
  }

  override def render = {
    def renderMessage(message: Message) =
      ".sender *" #> message.sender.email &
          ".date *" #> (DateFormat.getDateTimeInstance format message.date) &
          ".text *" #> message.text
    "#message" #> (messages map renderMessage) &
        ClearClearable
  }

  override def localSetup(): Unit  = {
    super.localSetup()
    logger debug "local Setup for the Ids: %s".format(name)

    val messagesForIds = idList match {
      case Full(ids) => MessageDB.findAllOfUsersById(ids)
      case _ => Nil
    }

    messages = messages ++ (messagesForIds diff messages)

    if(User.currentUser.isDefined)
    {
      val followings =  Follow.findAllFollowing
      val users: List[User] = User.currentUser.open_! :: followings

      val dbMessages = MessageDB.findAllOfUsers(users)

      messages = messages ++ (dbMessages diff messages)
    }

    reRender
  }

  private def idList: Box[List[String]] = name match {
    case Full(nameInner) => Full(nameInner.split(",").toList)
    case _ => Empty
  }

  override protected def registerWith = ChatterServer

  private var messages = Vector[Message]()
}
