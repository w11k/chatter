/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.comet

import com.weiglewilczek.chatter.lib.{ ChatterMessage, ChatterServer, Logging }
import com.weiglewilczek.chatter.lib.DateHelpers._
import com.weiglewilczek.chatter.lib.LocaleHelpers._
import com.weiglewilczek.chatter.model.User
import com.weiglewilczek.chatter.model.UserFollowingUser._

import net.liftweb.common.{ Box, Empty }
import net.liftweb.http.{ CometActor, CometListener }
import scala.xml.NodeSeq

class ChatterMessages extends CometActor with CometListener with Logging {

  private var messages = List[ChatterMessage]()

  private var user: User = _

  override def render = {
    def bindMessages(template: NodeSeq): NodeSeq = messages flatMap { m =>
      bind("message", template,
           "name" -> m.name,
           "date" -> format(m.date, boxedSessionLocale),
           "text" -> m.text)
    }
    logger debug "Rendering messages: %s".format(messages)
    bind("messages", defaultXml, "list" -> bindMessages _)
  }

  override def lowPriority = {
    case message: ChatterMessage => {
      logger debug "Received Chatter message: %s".format(message)
      messages ::= message
      reRender(false)
    }
  }

  override def registerWith = ChatterServer

  override def shouldUpdate = {
    case ChatterMessage(userId, _, _, _) =>
      if (user.id.is == userId) true else following_?(user, userId)
  }

  override def localSetup() {
    user = User.currentUser.open_! // We may do this, because we require a logged-in user.
    super.localSetup()
  }
}
