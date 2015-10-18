name := "Testing Scala"
version := "1.0"
scalaVersion := "2.11.6"
libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "1.6.2" withSources() withJavadoc(),
  "org.scalatest" %% "scalatest" % "2.2.4" % "test" withSources() withJavadoc(),
  "junit" % "junit" % "4.10" % "test" withSources() withJavadoc(),
  "org.testng" % "testng" % "6.1.1" % "test" withSources() withJavadoc()
)
