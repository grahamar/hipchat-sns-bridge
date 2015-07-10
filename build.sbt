name := """hipchat-sns-bridge"""

organization := "com.teambytes"

version := "git describe --tags --dirty --always".!!.stripPrefix("v").trim

enablePlugins(PlayScala)

scalaVersion := "2.11.7"

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

