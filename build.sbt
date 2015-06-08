
lazy val commonRootSettings = Seq(
  version := "1.1.0",
  scalaVersion := "2.11.6",
  crossScalaVersions := Seq("2.11.6", "2.10.4"),
  organization := "me.jeffmay",
  organizationName := "Jeff May"
) ++ bintraySettings ++ bintrayPublishSettings

commonRootSettings

lazy val common = commonRootSettings ++ Seq(

  scalacOptions := {
    // the deprecation:false flag is only supported by scala >= 2.11.3, but needed for scala >= 2.11.0 to avoid warnings
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, scalaMinor)) if scalaMinor >= 11 =>
        // For scala versions >= 2.11.3
        Seq("-Xfatal-warnings", "-deprecation:false")
      case Some((2, scalaMinor)) if scalaMinor < 11 =>
        // For scala versions 2.10.x
        Seq("-Xfatal-warnings", "-deprecation")
    }
  } ++ Seq(
    "-feature",
    "-Xlint",
    "-Ywarn-dead-code",
    "-encoding", "UTF-8"
  ),

  ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },

  // disable compilation of ScalaDocs, since this always breaks on links
  sources in(Compile, doc) := Seq.empty,

  // disable publishing empty ScalaDocs
  publishArtifact in (Compile, packageDoc) := false,

  licenses += ("Apache-2.0", url("http://opensource.org/licenses/apache-2.0"))

)

lazy val scalaEnum = project in file("scalaEnum") settings(common: _*) settings (

  name := "scala-enum",

  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )
)

lazy val scalaEnumJson = project in file("scalaEnumJson") settings(common: _*) settings (

  name := "scala-enum-json",

  libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.3.7",
    "org.scalacheck" %% "scalacheck" % "1.12.2" % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "me.jeffmay" %% "play-json-tests" % "1.1.1" % "test"
  )
) dependsOn scalaEnum

// don't publish the surrounding multi-project build
publish := {}

