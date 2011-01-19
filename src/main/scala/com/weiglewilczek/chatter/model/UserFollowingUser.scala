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
package model

import net.liftweb.common.Loggable
import net.liftweb.mapper.{ By, IdPK, In, LongKeyedMapper, LongKeyedMetaMapper, MappedLongForeignKey }

object UserFollowingUser extends UserFollowingUser with LongKeyedMetaMapper[UserFollowingUser] with Loggable {

  def following_?(u: User, userId: Long) = find(By(user, u), By(following, userId)).isDefined

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

class UserFollowingUser extends LongKeyedMapper[UserFollowingUser] with IdPK {

  val user = new MappedLongForeignKey(this, User) {}

  val following = new MappedLongForeignKey(this, User) {}

  override def getSingleton = UserFollowingUser
}
