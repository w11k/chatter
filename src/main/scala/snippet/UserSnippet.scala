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
package snippet

import model.{MessageDB, User}
import lib.Message
import net.liftweb.common.Loggable
import net.liftweb.util.Helpers._
import net.liftweb.http.{S, SHtml}
import lib.ChatterServer
import net.liftweb.common.{Full, Failure}
import comet.ChatterClient

object UserSnippet extends Loggable {

  def render = {

    val userBox = for (
    idString <- S.param("userId") ?~! "No userid given ";
    user <- User.find(idString) ?~! "User with id %s not found".format(idString)
    ) yield user

    val render = userBox match {
      case Full(user) =>
      case Failure(message,_,_) => S.error(message)
      case _ => S.error("Unknown Error")
    }

    "#comet" #> ChatterClient.clientNodeSeq(S.param("userId").open_! :: Nil)
  }
}
