/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.snippet

import com.weiglewilczek.chatter.lib.Logging

import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import scala.xml.NodeSeq

class ChatterInput extends Logging {

  def render(xhtml: NodeSeq) = {
    def handleUpdate(text: String) { logger debug "Sending message: %s".format(text) }
    bind("chatter", xhtml,
         "text" -> textarea("", handleUpdate _),
         "submit" -> submit("Update", () => ()))
  }
}
