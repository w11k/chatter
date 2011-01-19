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

import java.text.DateFormat._
import java.util.{ Date, Locale }
import net.liftweb.common.{ Box, Empty, Failure, Full, Loggable }

/**
 * We use this object to access date utility functions.
 */
object DateHelpers extends DateHelpers

/**
 * We mix-in this trait to add date utility functions.
 */
trait DateHelpers extends Loggable {

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
