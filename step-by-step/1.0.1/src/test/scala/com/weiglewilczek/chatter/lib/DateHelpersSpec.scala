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

import java.util.{ Date, Locale }
import Locale._
import net.liftweb.common.{ Empty, Full }
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DateHelpersSpec extends WordSpec with ShouldMatchers {

  "Calling DateHelpers.format" when {

    "the given date is null" should {
      "throw an IllegalArgumentException" in {
        evaluating { DateHelpers.format(null, Empty) } should produce [IllegalArgumentException]
      }
    }

    "the given Locale is null" should {
      "throw an IllegalArgumentException" in {
        evaluating { DateHelpers.format(new Date, null) } should produce [IllegalArgumentException]
      }
    }

    "the given date is 1262300400000 and the Locale is GERMAN" should {
      "return '01.01.10'" in {
        DateHelpers.format(new Date(1262300400000L), Full(GERMAN)) should be ("01.01.10")
      }
    }

    "the given date is 1262300400000 and no Locale is given" should {
      "return '01.01.10'" in {
        val default = Locale.getDefault
        Locale setDefault GERMAN
        DateHelpers.format(new Date(1262300400000L), Empty) should be ("01.01.10")
        Locale setDefault default
      }
    }
  }

  "Calling DateHelpers.parse" when {

    "the given date String is null" should {
      "throw an IllegalArgumentException" in {
        evaluating { DateHelpers.parse(null, Empty) } should produce [IllegalArgumentException]
      }
    }

    "the given Locale is null" should {
      "throw an IllegalArgumentException" in {
        evaluating { DateHelpers.parse("", null) } should produce [IllegalArgumentException]
      }
    }

    "the given date String is '01.01.10' and the Locale is GERMAN" should {
      "return a Date equivalent to 1262300400000" in {
        DateHelpers.parse("01.01.10", Full(GERMAN)) should be (new Date(1262300400000L))
      }
    }

    "the given date String is '01.01.10' and no Locale is given" should {
      "return a Date equivalent to 1262300400000" in {
        val default = Locale.getDefault
        Locale setDefault GERMAN
        DateHelpers.parse("01.01.10", Empty) should be (new Date(1262300400000L))
        Locale setDefault default
      }
    }
  }
}
