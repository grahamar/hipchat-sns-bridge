
enablePlugins(PlayScala)

name := """hipchat-sns-bridge"""

organization := "com.teambytes"

version := "git describe --tags --dirty --always".!!.stripPrefix("v").trim

scalaVersion := "2.11.7"

dockerRepository := Some("giltcommon")

dockerExposedPorts in Docker := Seq(9000)

dockerExposedVolumes := Seq("/opt/docker/logs")

dockerBaseImage := "java:8-jre"

bashScriptExtraDefines ++= Seq(
  s"""addJava "-Dservice.version=${version.value}"""",
  """addJava "-Dfile.encoding=UTF8"""",
  """addJava "-Duser.timezone=GMT""""
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.bintrayRepo("scalaz", "releases"),
  Resolver.bintrayRepo("etaty", "maven"),
  Resolver.bintrayRepo("dwhjames", "maven")
)

libraryDependencies ++= Seq(
  ws,
  "io.evanwong.oss" % "hipchat-java" % "0.4.0"
)

routesGenerator := InjectedRoutesGenerator

