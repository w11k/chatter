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

import lib.DateHelpers._
import lib.LocaleHelpers._
import model.{ User, UserFollowingUser }

import java.util.ResourceBundle
import net.liftweb.common.{ Logger, Loggable }
import net.liftweb.http.{ Bootable, LiftRules, RedirectResponse }
import net.liftweb.mapper.{ DB, DBLogEntry, DefaultConnectionIdentifier, Schemifier, StandardDBVendor }
import net.liftweb.sitemap.{ Loc, Menu, SiteMap }
import net.liftweb.sitemap.Loc._
import net.liftweb.util.Props
import net.liftweb.util.Helpers._

/**
 * Here we configure our Lift application.
 */
class
Boot extends Bootable with Loggable {

  override def boot() {
    logger info "Booting Chatter, please stand by ..."

    // i18n and formatting stuff
    LiftRules.early append { _ setCharacterEncoding "UTF-8" }
    LiftRules.resourceBundleFactories prepend {
      case (name, locale) =>
        tryo { ResourceBundle.getBundle("i18n." + name, locale) } openOr null
    }
    LiftRules.resourceNames = "messages" :: Nil
    LiftRules.formatDate = date => format(date, boxedSessionLocale)
    LiftRules.parseDate = date => parse(date, boxedSessionLocale)

    // Where do we look for Lift packages?
    LiftRules addToPackages getClass.getPackage

    // SiteMap
    val ifLoggedIn = If(() => User.loggedIn_?, () => RedirectResponse(User.loginPageURL))
    val homeMenu = Menu(Loc("home", List("index"), "Home", ifLoggedIn))
    val followersMenu = Menu(Loc("followers", List("followers"), "Followers", ifLoggedIn))
    val followingMenu = Menu(Loc("following", List("following"), "Following", ifLoggedIn))
    val menus = homeMenu :: followersMenu :: followingMenu :: User.menus
    LiftRules setSiteMap SiteMap(menus: _*)

    // DB configuration
    val dbVendor =
      new StandardDBVendor(Props get "db.driver" openOr "org.h2.Driver",
                           Props get "db.url" openOr "jdbc:h2:chatter",
                           Props get "db.user",
                           Props get "db.password") {
      override def maxPoolSize = Props getInt "db.pool.size" openOr 3
    }
    DB.defineConnectionManager(DefaultConnectionIdentifier, dbVendor)
    val dbLogger = Logger("com.weiglewilczek.chatter.DB")
    DB addLogFunc { (query, _) =>
      query.allEntries foreach {
        case DBLogEntry(stmt, duration) => dbLogger debug (stmt + " took " + duration + "ms.")
      }
    }
    Schemifier.schemify(true, (dbLogger info _): (=> AnyRef) => Unit, User, UserFollowingUser)

    // Mailer configuration
//    val user = Props get "mail.user" openOr "XXX"
//    val pwd = Props get "mail.pwd" openOr "XXX"
//    Mailer.authenticator = Full(new Authenticator {
//      override def getPasswordAuthentication = new PasswordAuthentication(user , pwd)
//    })

    logger info "Successfully booted Chatter. Have fun!"
  }
}
