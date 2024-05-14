package hs

import com.typesafe.config.Config

import scala.jdk.CollectionConverters.*

import scalafx.scene.image.{Image, ImageView}

final class Context(config: Config):
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