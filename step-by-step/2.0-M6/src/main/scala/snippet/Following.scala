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

import model.User
import net.liftweb.common.Loggable
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds
import net.liftweb.util.{ ClearClearable, Helpers }
import scala.xml.NodeSeq

object Following extends Loggable {

  def currently = {
    def unfollow(user: User)() = {
      logger.debug("About to unfollow user with id: %s".format(user.id.is))
      JsCmds.Replace(user.id.is.toString, NodeSeq.Empty)
    }
    import Helpers._
    def renderUser(user: User) =
      "#user [id]" #> user.id.is.toString &
          ".unfollow [onclick]" #> SHtml.ajaxInvoke(unfollow(user) _)._2.toJsCmd &
          ".name *" #> user.shortName &
          ".email *" #> user.email.is
    "#user" #> (User.findAll map renderUser) &
        ClearClearable
  }

  def start = {
    def renderUser(user: User) =
      <option value={ user.id.is.toString }>{ "%s (%s)".format(user.shortName, user.email.is) }</option>
    def handleSubmit(s: String) {
      logger.debug("About to follow user with id: %s".format(s))
    }
    import Helpers._
    "#startFollowing" #> (SHtml onSubmit handleSubmit) &
        "#startFollowing *" #> (User.findAll map renderUser)
  }
}
