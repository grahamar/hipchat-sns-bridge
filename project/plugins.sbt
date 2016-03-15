resolvers := Seq(
  DefaultMavenRepository,
  Resolver.typesafeRepo("releases"),
  Classpaths.sbtPluginReleases
)

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.5.0")
