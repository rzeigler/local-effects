import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val cats = "org.typelevel" %% "cats-core" % "1.5.0"
  lazy val catsEffect =  "org.typelevel" %% "cats-effect" % "1.1.0"
  lazy val catsMtl =  "org.typelevel" %% "cats-mtl-core" % "0.4.0"
  lazy val meowMtl = "com.olegpy" %% "meow-mtl" % "0.2.0"
  lazy val console4Cats = "com.github.gvolpe" %% "console4cats" % "0.5"
}
