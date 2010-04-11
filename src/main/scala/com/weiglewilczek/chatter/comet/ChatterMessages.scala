/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter
package comet

import lib.{ ChatterMessage, ChatterServer }
import lib.DateHelpers._
import lib.LocaleHelpers._
import model.User
import model.UserFollowingUser._

import net.liftweb.common.Loggable
import net.liftweb.http.{ CometActor, CometListener }
import scala.xml.NodeSeq

class ChatterMessages extends CometActor with CometListener with Loggable {

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

  private var messages = List[ChatterMessage]()

  private var user: User = _
}
