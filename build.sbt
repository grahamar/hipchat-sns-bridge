
enablePlugins(PlayScala)

name := """hipchat-sns-bridge"""

organization := "com.teambytes"

version := "git describe --tags --dirty --always".!!.stripPrefix("v").trim

scalaVersion := "2.11.7"

dockerRepository := Some("giltcommon")

dockerExposedPorts in Docker := Seq(9000)

dockerExposedVolumes := Seq("/opt/docker/logs")

dockerBaseImage := "java:8-jre"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.bintrayRepo("scalaz", "releases"),
  Resolver.bintrayRepo("etaty", "maven"),
  Resolver.bintrayRepo("dwhjames", "maven"),
  "Zaneli Repository" at "http://www.zaneli.com/repositories"
)

libraryDependencies ++= Seq(
  "com.zaneli" %% "escalade-hipchat" % "0.0.1"
)

routesGenerator := InjectedRoutesGenerator

