/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.lib

import java.util.Date
import net.liftweb.actor.LiftActor
import net.liftweb.common.Box
import net.liftweb.http.ListenerManager

object ChatterServer extends LiftActor with ListenerManager with Logging {

  private var message: ChatterMessage = _

  override def lowPriority = {
    case message @ ChatterMessage(_, _, _, text) if !text.isEmpty => {
      logger debug "Received Chatter message: %s".format(message)
      this.message = message
      updateListeners()
    }
  }

  override protected def createUpdate = message
}

case class ChatterMessage(userId: Long, name: String, date: Date, text: String)
