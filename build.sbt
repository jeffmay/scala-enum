
name := "scala-enum"

organization := "me.jeffmay"

version := "1.0.1"

crossScalaVersions := Seq("2.11.6", "2.10.4")

scalacOptions := {
  // the deprecation:false flag is only supported by scala >= 2.11.3, but needed for scala >= 2.11.0 to avoid warnings
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMinor)) if scalaMinor >= 11 =>
      // For scala versions >= 2.11.3
      Seq("-Xfatal-warnings", "-deprecation:false")
    case Some((2, scalaMinor)) if scalaMinor < 11 =>
      // For scala versions 2.10.x
      Seq("-Xfatal-warnings")
  }
} ++ Seq(
  "-feature",
  "-Xlint",
  "-Ywarn-dead-code",
  "-encoding", "UTF-8"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

// disable compilation of ScalaDocs, since this always breaks on links and isn't as helpful as source
sources in(Compile, doc) := Seq.empty

// disable publishing empty ScalaDocs
publishArtifact in (Compile, packageDoc) := false

bintraySettings ++ bintrayPublishSettings

licenses += ("Apache-2.0", url("http://opensource.org/licenses/apache-2.0"))

