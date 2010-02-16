/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.snippet

import com.weiglewilczek.chatter.model.{ User, UserFollowingUser }

import net.liftweb.common.Empty
import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import scala.xml.NodeSeq

class Follow {

  def findAllFollowers(xhtml: NodeSeq) = {
    val followers = UserFollowingUser.findAllFollowers
    bind("follow", xhtml,
         "size" -> followers.size,
         "list" -> bindUsers(followers) _)
  }

  def findAllFollowing(xhtml: NodeSeq) = {
    val following = UserFollowingUser.findAllFollowing
    bind("follow", xhtml,
         "size" -> following.size,
         "list" -> bindUsers(following) _)
  }

  def follow(xhtml: NodeSeq) = {
    val selectableUsers = User.findAll map { u => (u.id.is.toString, u.shortName) }
    var selectedUserId = ""
    def handleSubmit() {
      for { user <- User.currentUser } {
        UserFollowingUser.create.user(user.id.is).following(selectedUserId.toLong).save
      }
    }
    bind("follow", xhtml,
      "users" -> select(selectableUsers, Empty, selectedUserId = _),
      "submit" -> submit("Add", handleSubmit _))
  }

  private def bindUsers(users: => List[User])(template: NodeSeq): NodeSeq = users flatMap { u =>
    bind("follow", template, "name" -> u.shortName)
  }
}
