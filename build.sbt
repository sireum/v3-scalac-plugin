val scalaVer = "2.12.4"

val pluginVersion = "3.1.3-SNAPSHOT"

val metaVersion = "2.1.2"

lazy val `scalac-plugin-assembly` = (project in file(".")).settings(Seq(
  organization := "org.sireum",
  name := "scalac-plugin-assembly",
  scalaVersion := scalaVer,
  version := pluginVersion,
  scalacOptions := Seq("-target:jvm-1.8", "-deprecation",
    "-Ydelambdafy:method", "-feature", "-unchecked", "-Xfatal-warnings"),
  assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),
  artifact in(Compile, assembly) := {
    val art = (artifact in(Compile, assembly)).value
    art.withClassifier(Some("all"))
  },
  assemblyShadeRules in assembly := Seq(
    ShadeRule.rename("com.**" -> "sh4d3.com.@1").inAll,
    ShadeRule.rename("fastparse.**" -> "sh4d3.fastparse.@1").inAll,
    ShadeRule.rename("google.**" -> "sh4d3.google.@1").inAll,
    ShadeRule.rename("org.langmeta.**" -> "sh4d3.org.langmeta.@1").inAll,
    ShadeRule.rename("org.scalameta.**" -> "sh4d3.org.scalameta.@1").inAll,
    ShadeRule.rename("scala.meta.**" -> "sh4d3.scala.meta.@1").inAll,
    ShadeRule.rename("scalapb.**" -> "sh4d3.scalapb.@1").inAll,
    ShadeRule.rename("sourcecode.**" -> "sh4d3.sourcecode.@1").inAll
  ),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % scalaVer,
    "org.scalameta" %% "scalameta" % metaVersion
  ),
  skip in publish := true
)).settings(addArtifact(artifact in(Compile, assembly), assembly).settings: _*)

lazy val `scalac-plugin` = project.settings(
  organization := "org.sireum",
  name := "scalac-plugin",
  version := pluginVersion,
  packageBin in Compile := (assembly in(`scalac-plugin-assembly`, Compile)).value,
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  pomExtra :=
    <url>https://github.com/sireum/v3-scalac-plugin/</url>
      <licenses>
        <license>
          <name>Simplified BSD License</name>
          <url>https://github.com/sireum/v3-scalac-plugin/blob/master/license.md</url>
        </license>
      </licenses>
      <scm>
        <url>https://github.com/sireum/v3-scalac-plugin.git</url>
        <connection>scm:git:https://github.com/sireum/v3-scalac-plugin.git</connection>
      </scm>
      <developers>
        <developer>
          <id>robby-phd</id>
          <name>Robby</name>
          <url>http://cs.ksu.edu/~robby</url>
        </developer>
      </developers>
)
