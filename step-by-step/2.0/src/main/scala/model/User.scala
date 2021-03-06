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

import net.liftweb.common.Empty
import net.liftweb.mapper.{ MegaProtoUser, MetaMegaProtoUser }
import scala.xml.NodeSeq

object User extends User with MetaMegaProtoUser[User] {

  def currentUserName = User.currentUser map { _.shortName } openOr error("No current user!")

  override def signupFields = firstName :: email :: password :: Nil

  override def menus =
    List(loginMenuLoc, createUserMenuLoc, changePasswordMenuLoc, logoutMenuLoc).flatten

  override def skipEmailValidation = true

  override def loginXhtml = surround(super.loginXhtml)

  override def signupXhtml(user: User) = surround(super.signupXhtml(user))

  override def changePasswordXhtml = surround(super.changePasswordXhtml)

  private def surround(xhtml: => NodeSeq) =
    <lift:surround with="default" at="content">{ xhtml }</lift:surround>
}

class User extends MegaProtoUser[User] {

  override def firstNameDisplayName = "Name"

  override def getSingleton = User
}
