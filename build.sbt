name := "homeschool"
organization := "objektwerks"
version := "1.5-SNAPSHOT"
scalaVersion := "2.13.10"
libraryDependencies ++= {
  val slickVersion = "3.4.1" // Can't yet upgrade to Scala 3!
  Seq(
    "org.scalafx" %% "scalafx" % "19.0.0-R30",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "2.1.214",
    "ch.qos.logback" % "logback-classic" % "1.4.4",
    "org.scalatest" %% "scalatest" % "3.2.14" % Test
  )
}
