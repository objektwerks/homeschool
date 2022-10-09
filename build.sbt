name := "homeschool"
organization := "objektwerks"
version := "1.5-SNAPSHOT"
scalaVersion := "2.13.9"
libraryDependencies ++= {
  val slickVersion = "3.3.3" // Can't yet upgrade to Scala 3!
  Seq(
    "org.scalafx" %% "scalafx" % "18.0.2-R29",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "2.1.214",
    "ch.qos.logback" % "logback-classic" % "1.4.3",
    "org.scalatest" %% "scalatest" % "3.2.14" % Test
  )
}
