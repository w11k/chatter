/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.model

import net.liftweb.mapper.{ By, IdPK, In, LongKeyedMapper, LongKeyedMetaMapper, MappedLongForeignKey }

object UserFollowingUser extends UserFollowingUser with LongKeyedMetaMapper[UserFollowingUser] {

  def findAllFollowers = User.currentUser map { u =>
    User findAll In(User.id, UserFollowingUser.user, By(UserFollowingUser.following, u.id))
  } openOr Nil

  def findAllFollowing = User.currentUser map { u =>
    User findAll In(User.id, UserFollowingUser.following, By(UserFollowingUser.user, u.id))
  } openOr Nil
}

class UserFollowingUser extends LongKeyedMapper[UserFollowingUser] with IdPK {

  val user = new MappedLongForeignKey(this, User) {}

  val following = new MappedLongForeignKey(this, User) {}

  override def getSingleton = UserFollowingUser
}
