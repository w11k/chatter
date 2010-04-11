/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter
package lib

/**
 * We use this object to access image utility functions.
 */
object ImgHelpers extends ImgHelpers

/**
 * We mix-in this trait to add image utility functions.
 */
trait ImgHelpers {

  /**
   * Render an image element with an onclick handler for unfollowing.
   */
  def unfollowImg = img("/img/unfollow.png", "Unfollow")

  private def img(src: String, title: => String) = <img src={ src } title={ title } alt={ title }/>
}
