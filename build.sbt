name := "homeschool"
organization := "objektwerks"
version := "1.3-SNAPSHOT"
scalaVersion := "2.13.7"
libraryDependencies ++= {
  val slickVersion = "3.3.3"
  Seq(
    "org.scalafx" %% "scalafx" % "16.0.0-R25",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "1.4.200",
    "ch.qos.logback" % "logback-classic" % "1.2.6",
    "org.scalatest" %% "scalatest" % "3.2.10" % Test
  )
}
