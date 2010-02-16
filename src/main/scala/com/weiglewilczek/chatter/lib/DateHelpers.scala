/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.lib

import java.text.DateFormat._
import java.util.{ Date, Locale }
import net.liftweb.common.{ Box, Empty, Failure, Full }

/**
 * We use this object to access date utility functions.
 */
object DateHelpers extends DateHelpers

/**
 * We mix-in this trait to add date utility functions.
 */
trait DateHelpers extends Logging {

  /**
   * Format the given date for the given or default (if given is Empty) Locale in SHORT style.
   */
  def format(date: Date, locale: Box[Locale]): String = {
    require(date != null, "Illegal argument: The date must not be null!")
    require(locale != null, "Illegal argument: The locale must not be null!")
    shortDateFormat(locale) format date
  }

  /**
   * Parse the given date string for the given or default (if given is Empty) Locale in SHORT style.
   */
  def parse(date: String, locale: Box[Locale]): Box[Date] = {
    require(date != null, "Illegal argument: The date string must not be null!")
    require(locale != null, "Illegal argument: The locale must not be null!")
    try {
      Full(shortDateFormat(locale) parse date)
    } catch { case e =>
      val msg = """Cannot parse date "%s"!""".format(date)
      logger error (msg, e)
      Failure(msg, Full(e), Empty)
    }
  }

  private def shortDateFormat(locale: Box[Locale]) = getDateInstance(SHORT, locale openOr Locale.getDefault)
}
