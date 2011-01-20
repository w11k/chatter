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

import net.liftweb.common.Loggable
import net.liftweb.http.{ Bootable, Html5Properties, LiftRules, Req }
import net.liftweb.http.js.jquery.JQuery14Artifacts

class Boot extends Bootable with Loggable {

  def boot() {
    logger.info("Chatter is booting, please hold on ...")

    // Where do we look for Lift packages?
    LiftRules.addToPackages(getClass.getPackage)

    // SiteMap

    // Other configuration stuff
    LiftRules.early.append { _ setCharacterEncoding "UTF-8" }
    LiftRules.htmlProperties.default.set { req: Req => new Html5Properties(req.userAgent) }
    LiftRules.jsArtifacts = JQuery14Artifacts

    logger.info("Successfully booted Chatter. Have fun!")
  }
}
