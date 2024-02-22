ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.0"

lazy val root = (project in file("."))
  .settings(
    name := "LoanCalculator",
    coverageEnabled := true
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % "test"
libraryDependencies += "org.scalatestplus" %% "mockito-5-10" % "3.2.18.0" % "test"
