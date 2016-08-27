import sbt._
import Keys._

/**
  * Stitchfix changes:
  *  - Fixed cross scala versions and scalaVersion to match what SFS3 uses
  *  - Changed publishTo and Credentials to be artifactory
  *  - Made version 3.2.2
  */
object ScalaRedisProject extends Build
{
  import Resolvers._
  lazy val root = Project("RedisClient", file(".")) settings(coreSettings : _*)

  lazy val commonSettings: Seq[Setting[_]] = Seq(
    organization := "net.debasishg",
    version := "3.2.1",
    scalaVersion := "2.11.8",
    crossScalaVersions := Seq("2.10.5", "2.11.8"),

    scalacOptions in Compile ++= Seq( "-unchecked", "-feature", "-language:postfixOps", "-deprecation" ),

    resolvers ++= Seq(akkaRepo)
  )

  lazy val coreSettings = commonSettings ++ Seq(
    name := "RedisClient",
    libraryDependencies := Seq(
      "commons-pool"      %  "commons-pool"            % "1.6",
      "org.slf4j"         %  "slf4j-api"               % "1.7.2",
      "org.slf4j"         %  "slf4j-log4j12"           % "1.7.2"      % "provided",
      "log4j"             %  "log4j"                   % "1.2.16"     % "provided",
      "com.typesafe.akka" %% "akka-actor"              % "2.3.6",
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
}

object Resolvers {
  val akkaRepo = "typesafe repo" at "http://repo.typesafe.com/typesafe/releases/"
}
