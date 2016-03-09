
enablePlugins(PlayScala)

enablePlugins(BuildInfoPlugin)

name := """hipchat-sns-bridge"""

organization := "com.teambytes"

version := "git describe --tags --dirty --always".!!.stripPrefix("v").trim

scalaVersion := "2.11.7"

dockerExposedPorts in Docker := Seq(9000)

defaultLinuxInstallLocation in Docker := "/opt/hipchat-sns-bridge"

dockerExposedVolumes := Seq("/opt/hipchat-sns-bridge/logs")

dockerBaseImage := "java:8-jre"

buildInfoPackage := "buildinfo"

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

buildInfoOptions += BuildInfoOption.BuildTime

bashScriptConfigLocation := Some("${app_home}/../conf/application.${LAUNCH_ENVIRONMENT}.ini")

bashScriptExtraDefines ++= Seq(s"""export APP_VERSION="${version.value}"""")

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  play.sbt.PlayImport.ws withSources(),
  play.sbt.PlayImport.cache withSources(),
  play.sbt.PlayImport.jdbc withSources(),
  play.sbt.PlayImport.filters withSources(),
  "com.typesafe.play" %% "anorm" % "2.4.0",
  "com.h2database" % "h2" % "1.4.191",
  "org.postgresql" % "postgresql" % "9.4-1204-jdbc42",
  "net.codingwell" %% "scala-guice" % "4.0.0" withSources(),
  "com.imadethatcow" %% "hipchat-scala" % "1.0" withSources(),
  "org.scalatest" %% "scalatest" % "2.2.6" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0-RC1" % Test
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.bintrayRepo("scalaz", "releases"),
  Resolver.bintrayRepo("etaty", "maven"),
  Resolver.bintrayRepo("dwhjames", "maven")
)
