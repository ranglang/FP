name := "EffLiftApp"

scalaVersion := "2.12.6"

libraryDependencies += "org.atnos" % "eff_2.12" % "5.3.0"
libraryDependencies += "org.typelevel" %% "cats-core" % "1.2.0"
// https://mvnrepository.com/artifact/org.atnos/eff-cats-effect
libraryDependencies += "org.atnos" %% "eff-cats-effect" % "5.3.0"


scalacOptions += "-Ypartial-unification"
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
