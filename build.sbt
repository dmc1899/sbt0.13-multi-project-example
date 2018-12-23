import sbtrelease.Version

name := "master-app"
organization in ThisBuild := "uk.co.keshroad"
scalaVersion in ThisBuild := "2.11.12"

// PROJECTS

lazy val global = project
  .in(file("."))
  .settings(releaseSettings, settings)
  .aggregate(
    common,
    multi1,
    multi2
  )

lazy val common = project
  .settings(
    name := "common",
    //  version := "0.0.1",
    settings,
    libraryDependencies ++= commonDependencies
  )

lazy val multi1 = project
  .settings(
    name := "multi1",
    //  version := "0.0.2",
    settings,
    assemblySettings,
    releaseSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.monocleCore,
      dependencies.monocleMacro
    )
  )
  .dependsOn(
    common
  )

lazy val multi2 = project
  .settings(
    name := "multi2",
    //   version := "0.0.3",
    settings,
    assemblySettings,
    releaseSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.pureconfig
    )
  )
  .dependsOn(
    common
  )

// DEPENDENCIES

lazy val dependencies =
  new {
    val logbackV        = "1.2.3"
    val logstashV       = "4.11"
    val scalaLoggingV   = "3.7.2"
    val slf4jV          = "1.7.25"
    val typesafeConfigV = "1.3.1"
    val pureconfigV     = "0.8.0"
    val monocleV        = "1.4.0"
    val akkaV           = "2.5.6"
    val scalatestV      = "3.0.4"
    val scalacheckV     = "1.13.5"

    val logback        = "ch.qos.logback"             % "logback-classic"          % logbackV
    val logstash       = "net.logstash.logback"       % "logstash-logback-encoder" % logstashV
    val scalaLogging   = "com.typesafe.scala-logging" %% "scala-logging"           % scalaLoggingV
    val slf4j          = "org.slf4j"                  % "jcl-over-slf4j"           % slf4jV
    val typesafeConfig = "com.typesafe"               % "config"                   % typesafeConfigV
    val akka           = "com.typesafe.akka"          %% "akka-stream"             % akkaV
    val monocleCore    = "com.github.julien-truffaut" %% "monocle-core"            % monocleV
    val monocleMacro   = "com.github.julien-truffaut" %% "monocle-macro"           % monocleV
    val pureconfig     = "com.github.pureconfig"      %% "pureconfig"              % pureconfigV
    val scalatest      = "org.scalatest"              %% "scalatest"               % scalatestV
    val scalacheck     = "org.scalacheck"             %% "scalacheck"              % scalacheckV
  }

lazy val commonDependencies = Seq(
  dependencies.logback,
  dependencies.logstash,
  dependencies.scalaLogging,
  dependencies.slf4j,
  dependencies.typesafeConfig,
  dependencies.akka,
  dependencies.scalatest  % "test",
  dependencies.scalacheck % "test"
)

// SETTINGS

lazy val settings =
commonSettings ++
wartremoverSettings ++
scalafmtSettings

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val wartremoverSettings = Seq(
  wartremoverWarnings in (Compile, compile) ++= Warts.allBut(Wart.Throw)
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
    scalafmtTestOnCompile := true,
    scalafmtVersion := "1.2.0"
  )

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case _                             => MergeStrategy.first
  }
)

lazy val releaseSettings =
  Seq(
    releaseUseGlobalVersion := false,
    releaseVersionFile := file(name.value + "/version.sbt"),
    calcGitflowReleaseBranchName := { version: Version =>
      s"release/rc-v${version.withoutQualifier}"
    },
    releaseCalcTag := { v =>
      val versionValue = version.value; (s"${name.value}-v${v}", s"Release $v")
    }
//    releaseCalcTag := { v =>
//      val versionInThisBuild = (version in ThisBuild).value
//      val versionValue = version.value
//      s"${name.value}-v${if (releaseUseGlobalVersion.value) versionInThisBuild else versionValue}"
//    }
  ) /*,
  releaseTagName := {
    val versionInThisBuild = (version in ThisBuild).value
    val versionValue = version.value
    s"${name.value}-v${if (releaseUseGlobalVersion.value) versionInThisBuild
    else versionValue}"
  }/*,
  releaseProcess := newdayReleaseProcess*/
)
 */
/*
import ReleaseTransformations._

val newdayReleaseProcess = Seq[ReleaseStep](
  checkSnapshotDependencies, // : ReleaseStep
  inquireVersions, // : ReleaseStep
  runClean, // : ReleaseStep
  runTest, // : ReleaseStep
  setReleaseVersion, // : ReleaseStep
  commitReleaseVersion, // : ReleaseStep, performs the initial git checks
  tagRelease, // : ReleaseStep
  //publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion, // : ReleaseStep
  commitNextVersion, // : ReleaseStep
  pushChanges // : ReleaseStep, also checks that an upstream branch is properly configured
)*/
