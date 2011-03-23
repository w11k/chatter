import sbt._

class ChatterProject(info: ProjectInfo) extends DefaultWebProject(info) {

  // Versions
  val LiftVersion = "2.2"

  // Dependencies (compile)
  val liftWebkit = "net.liftweb" %% "lift-webkit" % LiftVersion withSources
  val liftMapper = "net.liftweb" %% "lift-mapper" % LiftVersion withSources
  val liftActor = "net.liftweb" %% "lift-actor" % LiftVersion withSources // Just in order to download the sources
  val liftCommon = "net.liftweb" %% "lift-common" % LiftVersion withSources // Just in order to download the sources
  val liftJson = "net.liftweb" %% "lift-json" % LiftVersion withSources // Just in order to download the sources
  val liftProto = "net.liftweb" %% "lift-proto" % LiftVersion withSources // Just in order to download the sources
  val liftUtil = "net.liftweb" %% "lift-util" % LiftVersion withSources // Just in order to download the sources
  val logback = "ch.qos.logback" % "logback-classic" % "0.9.27"

  // Dependencies (provided)
  val servletAPI = "javax.servlet" % "servlet-api" % "2.5" % "provided" withSources

  // Dependencies (test)
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test" withSources
  val mockito = "org.mockito" % "mockito-all"% "1.8.5" % "test"
  val jettyWebapp = "org.eclipse.jetty" % "jetty-webapp" % "7.0.2.v20100331" % "test"

  // Dependencies (runtime)
  val postgresql = "postgresql" % "postgresql" % "9.0-801.jdbc4" % "runtime"

  // Webapp settings
  override def jettyWebappPath = webappPath
  override def scanDirectories = Nil

//  System.setProperty("run.mode", "test")
}
