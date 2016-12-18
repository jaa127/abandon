lazy val commonSettings = Seq(
  // don't define "name" here, because it will cause
  // circular dependencies with sub-projects

  organization := "fi.sn127",
  version := "1.0.0",
  scalaVersion := "2.12.1",
  // see scala -opt:help for full list of optimizations with scala 2.12
  scalacOptions := List("-deprecation", "-opt:l:default"),
  wartremoverWarnings ++= Warts.allBut(Wart.ToString, Wart.Throw),

  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }

)

lazy val tack = (project in file(".")).
  aggregate(base, cli).
  dependsOn(base, cli).
  settings(commonSettings: _*).
  settings(
    name := "tack",
    fork in run := true
  )

lazy val base = (project in file("base")).
  settings(commonSettings: _*).
  enablePlugins(BuildInfoPlugin).
  settings(
    name := "tack-base",
    fork in run := true,
    test in assembly := {},
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "co.uproot.abandon"
  )


lazy val cli = (project in file("cli")).
  dependsOn(base).
  settings(commonSettings: _*).
  settings(
    name := "tack-cli",
    fork in run := true,
    assemblyJarName in assembly := "tack-cli" + "-" + version.value + ".jar",
    test in assembly := {}
  )

