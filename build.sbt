name := """playJavaAngularSample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.hamcrest" % "hamcrest-all" % "1.3",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.3"
)
