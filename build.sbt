// Summary of changes related to build and dependencies:
//
// Moved to using build.sbt from Scala build file.  Removed dependency on Akka and changed
// libraryDependencies to not use := since it eliminates Scala itself.  Also changed to
// use log4j2 and not SLF4j.  Changed repositories to be SF artifactory setup.

lazy val root = Project("RedisClient", file(".")) settings(coreSettings : _*)

lazy val commonSettings: Seq[Setting[_]] = Seq(
  organization := "net.debasishg",
  version := "3.3.0",
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.10.5", "2.11.8"),

  scalacOptions in Compile ++= Seq( "-unchecked", "-feature", "-language:postfixOps", "-deprecation" ),

  resolvers += "StitchFix snapshots" at "http://artifactory.vertigo.stitchfix.com/artifactory/snapshots",
  resolvers += "StitchFix releases" at "http://artifactory.vertigo.stitchfix.com/artifactory/releases"
)

// Removed Akka and commented out code that requires it (we can handle that separately where it's needed):
//   "com.typesafe.akka" %% "akka-actor"              % "2.3.6",
lazy val coreSettings = commonSettings ++ Seq(
  name := "RedisClient",
  libraryDependencies ++= Seq(
    "commons-pool"      %  "commons-pool"            % "1.6",
    "org.apache.logging.log4j" % "log4j-api" % "2.8",
    "org.apache.logging.log4j" % "log4j-core" % "2.8" % "provided",
    "junit"             %  "junit"                   % "4.8.1"      % "test",
    "org.scalatest"     %%  "scalatest"              % "2.1.3" % "test"),

  parallelExecution in Test := false,
  publishTo <<= version { (v: String) =>
    val artifactory = "http://artifactory.vertigo.stitchfix.com/artifactory/"
    if (v.trim.endsWith("SNAPSHOT")) Some("StitchFix Snapshots" at artifactory + "snapshots")
    else Some("StitchFix Releases" at artifactory + "releases")
  },
  credentials += Credentials("Artifactory Realm", "artifactory.vertigo.stitchfix.com", "admin", "password"),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { repo => false },
  pomExtra := (
    <url>https://github.com/debasishg/scala-redis</url>
      <licenses>
        <license>
          <name>Apache 2.0 License</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:debasishg/scala-redis.git</url>
        <connection>scm:git:git@github.com:debasishg/scala-redis.git</connection>
      </scm>
      <developers>
        <developer>
          <id>debasishg</id>
          <name>Debasish Ghosh</name>
          <url>http://debasishg.blogspot.com</url>
        </developer>
      </developers>),
  unmanagedResources in Compile <+= baseDirectory map { _ / "LICENSE" }
)
