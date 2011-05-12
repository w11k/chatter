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
import net.liftweb.mapper._
import lib.Message

object MessageDB extends MessageDB with LongKeyedMetaMapper[MessageDB] with Loggable {
  override def fieldOrder = List(userId, date, text)

   //TODO add implicit Conversion that works
 /* implicit def toMessage(messageDB: MessageDB) : Message = {

    val message = Message(User.find(messageDB.userId.is).open_!,date.get,text.get)
    message
  }*/

  //TODO not nice
  def findAllOfUser(user: User): List[Message] = findAll(By(userId, user.id.is)) map {_.toMessage}
  def findAllOfUserById(id: String): List[Message] = findAll(By(userId, id.toLong)) map {_.toMessage}

  def findAllOfUsers(users: List[User]): List[Message] = users flatMap findAllOfUser _
  def findAllOfUsersById(ids: List[String]): List[Message] = ids flatMap findAllOfUserById _
}

class MessageDB extends LongKeyedMapper[MessageDB] with IdPK {

  object userId extends MappedLongForeignKey(this, User)
  object date extends MappedDateTime(this)
  object text extends MappedString(this, 160)

  override def getSingleton = MessageDB

  def toMessage() : Message = {
    val message = Message(User.find(userId.is).open_!,date.get,text.get)
    message
  }
}
