lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "com.example",
      scalaVersion := "2.13.3"
    )
  ),
  name := "in-memory-file-system"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % Test
trapExit := false
