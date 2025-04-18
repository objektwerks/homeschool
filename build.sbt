name := "homeschool"
organization := "objektwerks"
version := "7.2-SNAPSHOT"
scalaVersion := "3.6.4" // Scala 3.7.0-RC3 breaks ScalaFx!
libraryDependencies ++= {
  val slickVersion = "3.5.1"
  Seq(
    "org.scalafx" %% "scalafx" % "23.0.1-R34",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "2.3.232",
    "ch.qos.logback" % "logback-classic" % "1.5.18",
    "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )
}
scalacOptions ++= Seq(
  "-Wunused:all",
  "-unchecked", "-deprecation"
)
