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
