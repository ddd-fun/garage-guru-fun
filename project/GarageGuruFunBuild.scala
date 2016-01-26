import sbt._
import Keys._

object GarageGuruFunProject extends Build
{
  import Resolvers._
  lazy val root = Project("GarageGuru", file(".")) settings(coreSettings : _*)

  lazy val commonSettings: Seq[Setting[_]] = Seq(
    version := "0.01",
    scalaVersion := "2.11.7",
    crossScalaVersions := Seq("2.11.7"),

    scalacOptions in Compile ++= Seq( "-unchecked", "-feature", "-language:postfixOps", "-deprecation" )
  )

  val akkaVersion = "2.3.11"

  lazy val coreSettings = commonSettings ++ Seq(
    name := "GarageGuru",
    libraryDependencies := Seq(
      "org.scalaz"                   %% "scalaz-core"                   % "7.1.0",
      "org.scalaz"                   %% "scalaz-concurrent"             % "7.1.0",
      "joda-time"                    %  "joda-time"                     % "2.9.1",
      "org.joda"                     %  "joda-convert"                  % "1.8.1",
      "io.spray"                     %% "spray-json"                    % "1.3.2",
      "com.typesafe.akka"            %% "akka-actor"                    % akkaVersion,
      "com.typesafe.akka"            %% "akka-persistence-experimental" % akkaVersion,
      "com.typesafe.akka"            %  "akka-stream-experimental_2.11" % "1.0-RC4",
      "com.typesafe.scala-logging"   %% "scala-logging-slf4j"           % "2.1.2",
      "com.typesafe.slick"           %% "slick"                         % "3.0.0",
      "com.h2database"                % "h2"                            % "1.4.187",
      "com.zaxxer"                    % "HikariCP-java6"                % "2.3.8",
      "ch.qos.logback"               %  "logback-classic"               % "1.1.3",
      "org.scalacheck"               %%  "scalacheck"                   % "1.11.5"       % "test"
    ),
    parallelExecution in Test := false,
    publishTo <<= version { (v: String) => 
      val nexus = "https://oss.sonatype.org/" 
      if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
      else Some("releases" at nexus + "service/local/staging/deploy/maven2") 
    },
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype.credentials"),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { repo => false },
    unmanagedResources in Compile <+= baseDirectory map { _ / "LICENSE" }
  )
}
