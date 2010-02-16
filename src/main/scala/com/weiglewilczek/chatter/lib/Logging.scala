/**
 * Copyright (c) 2010 WeigleWilczek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.chatter.lib

import net.liftweb.util.LogBoot._

/**
 * We mix this trait into whatever type we want to be able to log.
 */
trait Logging {

  /**
   * Logger for the current class.
   */
  lazy val logger = loggerByName(this.getClass.getName)
}
