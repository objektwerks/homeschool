package hs

import com.typesafe.config.ConfigFactory
import hs.model.Model
import hs.pane._
import hs.repository.Repository

import scalafx.application.JFXApp
import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

object App extends JFXApp {
  val conf = ConfigFactory.load("app.conf")
  val repository = Repository.newInstance("repository.conf")
  val model = new Model(repository)

  val menuPane = new MenuPane()
  val studentPane = new StudentPane(model)
  val gradePane = new GradePane(model)
  val coursePane = new CoursePane(model)
  val assignmentPane = new AssignmentPane(model)

  val northPane = new HBox { spacing = 6; children = List(studentPane, new Separator { orientation = Orientation.Vertical }, gradePane) }
  val southPane = new HBox { spacing = 6; children = List(coursePane, assignmentPane) }
  val contentPane = new VBox { spacing = 6; padding = Insets(6); children = List(menuPane, northPane, new Separator(), southPane) }
  val sceneGraph = new Scene { root = contentPane }
  stage = new JFXApp.PrimaryStage { scene = sceneGraph; title = conf.getString("title"); minHeight = 516; maxHeight = 516; minWidth = 684; maxWidth = 684 }

  sys.addShutdownHook { repository.close() }
}