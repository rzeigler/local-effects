import Dependencies._

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8")

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.rzeigler",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "local-effects",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += cats,
    libraryDependencies += catsMtl,
    libraryDependencies += catsEffect,
    libraryDependencies += meowMtl,
    libraryDependencies += console4Cats
  )
