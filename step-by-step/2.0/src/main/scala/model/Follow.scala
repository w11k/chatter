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
package model

import net.liftweb.common.Loggable
import net.liftweb.mapper.{ By, IdPK, In, LongKeyedMapper, LongKeyedMetaMapper, MappedLongForeignKey }

object Follow extends Follow with LongKeyedMetaMapper[Follow] with Loggable {

  def findAllFollowing: List[User] =
    User.currentUser map { currentUser =>
      User findAll In(User.id, followingId, By(userId, currentUser))
    } openOr Nil

  def findAllNotFollowing: List[User] =
    User.currentUser map { currentUser =>
      val dontFollow = currentUser :: findAllFollowing
      User.findAll filter { user => !(dontFollow contains user) }
    } openOr Nil

  def findAllFollowers: List[User] =
    User.currentUser map { currentUser =>
      User findAll In(User.id, userId, By(followingId, currentUser))
    } openOr Nil

  def follow(userId: Long) {
    for (currentUser <- User.currentUser) {
      Follow.create.userId(currentUser.id.is).followingId(userId).save
      logger.info("%s is following %s now.".format(currentUser.shortName, User findByKey userId map { _.shortName } openOr ""))
    }
  }

  def unfollow(user: User) {
    for (currentUser <- User.currentUser;
         following <- Follow.find(By(userId, currentUser), By(followingId, user))) {
      following.delete_!
      logger.info("%s is no longer following %s now.".format(currentUser.shortName, user.shortName))
    }
  }
}

class Follow extends LongKeyedMapper[Follow] with IdPK {

  val userId = new MappedLongForeignKey(this, User) {}

  val followingId = new MappedLongForeignKey(this, User) {}

  override def getSingleton = Follow
}
