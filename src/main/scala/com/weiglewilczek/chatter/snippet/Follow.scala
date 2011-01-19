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
package snippet

import lib.ImgHelpers._
import model.{ User, UserFollowingUser }

import net.liftweb.common.Empty
import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import xml.NodeSeq

class Follow {

  def findAllFollowers(xhtml: NodeSeq) = {
    val followers = UserFollowingUser.findAllFollowers
    def bindUsers(users: => List[User])(template: NodeSeq): NodeSeq = users flatMap { u =>
      bind("follow", template, "name" -> nameAndEmail(u))
    }
    bind("follow", xhtml,
         "size" -> followers.size,
         "list" -> bindUsers(followers) _)
  }

  def findAllFollowing(xhtml: NodeSeq) = {
    val following = UserFollowingUser.findAllFollowing
    def bindUsers(users: => List[User])(template: NodeSeq): NodeSeq = users flatMap { u =>
      bind("follow", template,
           "name" -> nameAndEmail(u),
           "unfollow" -> link("/following", () => { UserFollowingUser unfollow u }, unfollowImg))
    }
    bind("follow", xhtml,
         "size" -> following.size,
         "list" -> bindUsers(following) _)
  }

  def follow(xhtml: NodeSeq) = {
    val selectableUsers = UserFollowingUser.findAllNotFollowing sortWith {
      _.shortName < _.shortName
    } map { 
      u => (u.id.is.toString, nameAndEmail(u))
    }
    if (selectableUsers.isEmpty) NodeSeq.Empty
    else {
      var selectedUserId = ""
      def handleSubmit() { UserFollowingUser follow selectedUserId.toLong }
      bind("follow", xhtml,
           "users" -> select(selectableUsers, Empty, s => selectedUserId = s),
           "submit" -> submit("Add", handleSubmit _))
    }
  }

  private def nameAndEmail(user: User) = "%s (%s)".format(user.shortName, user.email.is)
}
