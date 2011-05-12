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
package lib

import java.util.Date
import model.{User, MessageDB}
import net.liftweb.util.ClearClearable
import java.text.DateFormat
import net.liftweb.util.Helpers._

object Message {

  implicit def toMessageDB(message: Message) : MessageDB = {
    val mDB = MessageDB.create.userId(message.sender.id.is)
    mDB.date.set(message.date)
    mDB.text.set(message.text)
    mDB
  }

  def renderMessages(messages: List[Message], userName: String = "asasas") = {
    def renderMessage(message: Message) =
      ".sender *" #> message.sender.shortName &
          ".date *" #> (DateFormat.getDateTimeInstance format message.date) &
          ".text *" #> message.text
    "#message *" #> (messages map renderMessage) &
        ClearClearable
  }
}

case class Message(sender: User, date: Date, text: String) {
  require(sender != null, "sender must not be null!")
  require(date != null, "date must not be null!")
  require(text != null, "text must not be null!")

  override def toString = text
}
