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

import net.liftweb.common.{ Empty, Full }
import net.liftweb.http.S._

/**
 * We use this object to access Locale utility functions.
 */
object LocaleHelpers extends LocaleHelpers

/**
 * We mix-in this trait to add Locale utility functions.
 */
trait LocaleHelpers {

  /**
   * Wrap the session's Locale in a Box: Full, if not null, else Empty.
   */
  def boxedSessionLocale = {
    val l = locale
    if (l != null) Full(l) else Empty
  }
}
