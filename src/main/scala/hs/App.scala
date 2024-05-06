package hs

import com.typesafe.config.ConfigFactory

import scalafx.application.JFXApp3

import slick.basic.DatabaseConfig
import slick.jdbc.{H2Profile, JdbcProfile}

object App extends JFXApp3:
  val config = DatabaseConfig.forConfig[JdbcProfile]("repository", ConfigFactory.load("repository.conf"))
  val repository = Repository(config, H2Profile)
  val model = Model(repository)

  val resources = ConfigFactory.load("resources.conf")
  val view = View(resources, model)

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      scene = view.sceneGraph
      title = resources.getString("title")
      minHeight = resources.getInt("height").toDouble
      minWidth = resources.getInt("width").toDouble
      icons.add(Images.appImage)
  
  sys.addShutdownHook:
    repository.close()