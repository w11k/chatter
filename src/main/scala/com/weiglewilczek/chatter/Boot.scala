/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter

import lib.DateHelpers._
import lib.LocaleHelpers._
import lib.Logging

import java.util.ResourceBundle
import net.liftweb.http.{ Bootable, LiftRules }
import net.liftweb.util.Helpers._

/**
 * Here we configure our Lift application.
 */
class
Boot extends Bootable with Logging {

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

    // DB configuration
//    val dbVendor =
//      new StandardDBVendor(Props get "db.driver" openOr "org.h2.Driver",
//                           Props get "db.url" openOr "jdbc:h2:XXX",
//                           Props get "db.user",
//                           Props get "db.password") {
//      override def maxPoolSize = Props getInt "db.pool.size" openOr 3
//    }
//    DB.defineConnectionManager(DefaultConnectionIdentifier, dbVendor)
//    DB addLogFunc { (query, _) =>
//      query.allEntries foreach {
//        case DBLogEntry(stmt, duration) => logger debug (stmt + " took " + duration + "ms.")
//      }
//    }
//    Schemifier.schemify(true, Log.infoF _, XXX)

    // Mailer configuration
//    val user = Props get "mail.user" openOr "XXX"
//    val pwd = Props get "mail.pwd" openOr "XXX"
//    Mailer.authenticator = Full(new Authenticator {
//      override def getPasswordAuthentication = new PasswordAuthentication(user , pwd)
//    })

    logger info "Successfully booted Chatter. Have fun!"
  }
}
