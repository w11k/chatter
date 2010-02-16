/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.model

import net.liftweb.common.Empty
import net.liftweb.mapper.{ MegaProtoUser, MetaMegaProtoUser }
import scala.xml.NodeSeq

object User extends User with MetaMegaProtoUser[User] {

  def currentUserName = User.currentUser map { _.shortName } openOr ""

  override def signupFields = firstName :: email :: password :: Nil

  override def skipEmailValidation = true

  override def lostPasswordMenuLoc = Empty

  override def loginXhtml = surround(super.loginXhtml)

  override def signupXhtml(user: User) = surround(super.signupXhtml(user))

  override def passwordResetXhtml = surround(super.passwordResetXhtml)

  override def editXhtml(user: User) = surround(super.editXhtml(user))

  override def changePasswordXhtml = surround(super.changePasswordXhtml)

  private def surround(xhtml: => NodeSeq) =
    <lift:surround with="default" at="content">{ xhtml }</lift:surround>
}

class User extends MegaProtoUser[User] {

  override def firstNameDisplayName = "Name"

  override def getSingleton = User
}
