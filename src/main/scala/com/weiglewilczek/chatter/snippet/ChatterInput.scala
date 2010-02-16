/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.snippet

import com.weiglewilczek.chatter.lib.{ ChatterMessage, ChatterServer, Logging }
import com.weiglewilczek.chatter.model.User._

import net.liftweb.http.SHtml._
import net.liftweb.http.js.JsCmds.After
import net.liftweb.http.js.jquery.JqJsCmds.SetValueAndFocus
import net.liftweb.util.Helpers._
import scala.xml.NodeSeq

class ChatterInput extends Logging {

  private lazy val messageId = nextFuncName

  def render(xhtml: NodeSeq) = {
    def handleUpdate(text: String) {
      val message = ChatterMessage(currentUserId, currentUserName, now, text.trim)
      logger debug "Sending Chatter message to server: %s".format(message)
      ChatterServer ! message
    }
    ajaxForm(After(100, SetValueAndFocus(messageId, "")),
             bind("chatter", xhtml,
                  "name" -> (currentUserName openOr ""),
                  "text" -> textarea("", handleUpdate _, "id" -> messageId),
                  "submit" -> submit("Update", () => ())))
  }
}
