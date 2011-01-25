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

import model.{ User, Follow }
import net.liftweb.util.{ ClearClearable, Helpers }

object Followers {

  def render = {
    import Helpers._
    def renderUser(user: User) =
      ".name *" #> user.shortName &
          ".email *" #> user.email.is
    val followers = Follow.findAllFollowers
    ".count" #> followers.size &
    "#user" #> (followers map renderUser) &
        ClearClearable
  }
}
