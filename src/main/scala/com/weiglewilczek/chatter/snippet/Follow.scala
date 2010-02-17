/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.snippet

import com.weiglewilczek.chatter.lib.ImgHelpers._
import com.weiglewilczek.chatter.model.{ User, UserFollowingUser }

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
    val selectableUsers = UserFollowingUser.findAllNotFollowing sort {
      _.shortName < _.shortName
    } map { 
      u => (u.id.is.toString, nameAndEmail(u))
    }
    if (selectableUsers.isEmpty) NodeSeq.Empty
    else {
      var selectedUserId = ""
      def handleSubmit() { UserFollowingUser follow selectedUserId.toLong }
      bind("follow", xhtml,
           "users" -> select(selectableUsers, Empty, selectedUserId = _),
           "submit" -> submit("Add", handleSubmit _))
    }
  }

  private def nameAndEmail(user: User) = "%s (%s)".format(user.shortName, user.email.is)
}
