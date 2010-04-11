/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
