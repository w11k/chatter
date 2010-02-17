/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.model

import com.weiglewilczek.chatter.lib.Logging

import net.liftweb.mapper.{ By, IdPK, In, LongKeyedMapper, LongKeyedMetaMapper, MappedLongForeignKey }

object UserFollowingUser extends UserFollowingUser with LongKeyedMetaMapper[UserFollowingUser] {

  def findAllFollowers = User.currentUser map { u =>
    User findAll In(User.id, user, By(following, u))
  } openOr Nil

  def findAllFollowing = User.currentUser map { u =>
    User findAll In(User.id, following, By(user, u))
  } openOr Nil

  def findAllNotFollowing = User.currentUser map { u =>
    val dontFollow = u :: findAllFollowing
    User.findAll filter { u => !(dontFollow contains u) }
  } openOr Nil

  def follow(userId: Long) {
    for { u <- User.currentUser } {
      create.user(u.id.is).following(userId).save
      logger info "%s is following %s now.".format(u.shortName, User findByKey userId map { _.shortName } openOr "")
    }
  }

  def unfollow(userToUnfollow: User) {
    for { u <- User.currentUser } {
      find(By(user, u), By(following, userToUnfollow)) foreach { _.delete_! }
      logger info "%s is no longer following %s now.".format(u.shortName, userToUnfollow.shortName)
    }
  }
}

class UserFollowingUser extends LongKeyedMapper[UserFollowingUser] with IdPK with Logging {

  val user = new MappedLongForeignKey(this, User) {}

  val following = new MappedLongForeignKey(this, User) {}

  override def getSingleton = UserFollowingUser
}
