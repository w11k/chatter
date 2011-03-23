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

import model.User
import net.liftweb.common.Loggable
import net.liftweb.http.{ Bootable, Html5Properties, LiftRules, RedirectResponse, Req }
import net.liftweb.http.js.jquery.JQuery14Artifacts
import net.liftweb.mapper.{ DB, DBLogEntry, DefaultConnectionIdentifier, Schemifier, StandardDBVendor }
import net.liftweb.sitemap.{ Menu, SiteMap }
import net.liftweb.sitemap.Loc.If
import net.liftweb.util.Props

class Boot extends Bootable with Loggable {

  def boot() {
    logger.info("Chatter is booting, please hold on ...")

    // Where do we look for Lift packages?
    LiftRules.addToPackages(getClass.getPackage)

    // SiteMap
    val ifLoggedIn =
      If(() => User.loggedIn_?, () => RedirectResponse(User.loginPageURL))
    val homeMenu = Menu("Home") / "index" >> ifLoggedIn
    val menus = homeMenu :: User.menus
    LiftRules.setSiteMap(SiteMap(menus: _*))

    // DB configuration
    val dbVendor =
      new StandardDBVendor(
        Props get "db.driver" openOr "org.postgresql.Driver",
        Props get "db.url" openOr "jdbc:postgresql://localhost/chatter",
        Props get "db.user",
        Props get "db.password")
    DB.defineConnectionManager(DefaultConnectionIdentifier, dbVendor)
    Schemifier.schemify(true, Schemifier.infoF _, User)

    // Other configuration stuff
    LiftRules.early.append { _ setCharacterEncoding "UTF-8" }
    LiftRules.htmlProperties.default.set { req: Req => new Html5Properties(req.userAgent) }
    LiftRules.jsArtifacts = JQuery14Artifacts

    logger.info("Successfully booted Chatter. Have fun!")
  }
}
