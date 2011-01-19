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
