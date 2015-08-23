resolvers += Classpaths.sbtPluginReleases

resolvers += Resolver.url(
  "scoverage-bintray",
  url("https://dl.bintray.com/sksamuel/sbt-plugins/"))(
    Resolver.ivyStylePatterns)

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.2.1")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")

addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.0.0")

