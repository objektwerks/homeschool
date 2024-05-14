package hs

import com.typesafe.config.Config

import scala.jdk.CollectionConverters.*

import scalafx.scene.image.{Image, ImageView}

final class Context(config: Config):
  val started = config.getString("started")
  val completed = config.getString("completed")
  val save = config.getString("save")
  val name = config.getString("name")

  val assignmentChartMonths = config.getString("assignment-chart-months")
  val assignmentChartScores = config.getString("assignment-chart-scores")
  val assignmentChartScore = config.getString("assignment-chart-score")
  val assignmentChart = config.getString("assignment-chart")
  val assignmentScores = config.getString("assignment-scores")
  val assignment = config.getString("assignment")
  val saveAssignement = config.getString("save-assignment")
  val minScore = config.getString("min-score")
  val maxScore = config.getString("max-score")
  val score = config.getString("score")
  val task = config.getString("task")
  val assigned = config.getString("assigned")

  val courseChartCourses = config.getString("course-chart-courses")
  val courseChartScores = config.getString("course-chart-scores")
  val courseChart = config.getString("course-chart")
  val courseScores = config.getString("course-scores")
  val course = config.getString("course")
  val saveCourse = config.getString("save-course")

  val year = config.getString("year")
  val grade = config.getString("grade")
  val saveGrade = config.getString("save-grade")

  def appImage = Image( Images.getClass.getResourceAsStream("/images/homeschool.png") )

  def addImageView = loadImageView("/images/add.png")

  def editImageView = loadImageView("/images/edit.png")

  def barChartImageView = loadImageView("/images/bar.chart.png")

  def lineChartImageView = loadImageView("/images/line.chart.png")

  def loadImageView(path: String): ImageView = new ImageView:
    image = Image( Images.getClass.getResourceAsStream(path) )
    fitHeight = 25
    fitWidth = 25
    preserveRatio = true
    smooth = true