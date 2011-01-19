/*
 * Copyright 2010-2011 Weigle Wilczek GmbH
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
package snippet

import lib.{ ChatterMessage, ChatterServer }
import model.User

import net.liftweb.common.Loggable
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JsCmds.{ After, SetValueAndFocus }
import net.liftweb.util.Helpers._
import scala.xml.NodeSeq

class ChatterInput extends Loggable {

  def render(xhtml: NodeSeq) = {
    val user = User.currentUser.open_! // We may do this, because we require a logged-in user.
    def handleUpdate(text: String) {
      val message = ChatterMessage(user.id.is, user.shortName, now, text.trim)
      logger debug "Sending Chatter message to server: %s".format(message)
      ChatterServer ! message
    }
    ajaxForm(After(100, SetValueAndFocus(messageId, "")),
             bind("chatter", xhtml,
                  "name" -> user.shortName,
                  "text" -> textarea("", handleUpdate _, "id" -> messageId),
                  "submit" -> submit("Update", () => ())))
  }

  private lazy val messageId = nextFuncName
}
