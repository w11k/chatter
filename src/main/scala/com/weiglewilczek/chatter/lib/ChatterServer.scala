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
package lib

import java.util.Date
import net.liftweb.actor.LiftActor
import net.liftweb.common.Loggable
import net.liftweb.http.ListenerManager

object ChatterServer extends LiftActor with ListenerManager with Loggable {

  override def lowPriority = {
    case message @ ChatterMessage(_, _, _, text) if !text.isEmpty => {
      logger debug "Received Chatter message: %s".format(message)
      this.message = message
      updateListeners()
    }
  }

  override protected def createUpdate = message

  private var message: ChatterMessage = _
}

case class ChatterMessage(userId: Long, name: String, date: Date, text: String)
