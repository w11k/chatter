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
import net.liftweb.common.Loggable
import net.liftweb.http.{ CometActor, CometListener }
import net.liftweb.util.ClearClearable

class ChatterClient extends CometActor with CometListener with Loggable {

  override def lowPriority = {
    case ms: Vector[Message] => {
      logger.debug("Received messages: %s".format(ms))
      messages = (ms diff messages) ++ messages
      reRender()
    }
  }

  override def render = {
    def renderMessage(message: Message) =
      ".sender *" #> message.sender &
          ".date *" #> (DateFormat.getDateTimeInstance format message.date) &
          ".text *" #> message.text
    "#message" #> (messages map renderMessage) &
        ClearClearable
  }

  override protected def registerWith = ChatterServer

  private var messages = Vector[Message]()
}
