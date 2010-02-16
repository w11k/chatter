/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.lib

import net.liftweb.http.S._
import net.liftweb.common.{ Empty, Full }

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
