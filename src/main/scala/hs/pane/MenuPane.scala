package hs.pane

import com.typesafe.config.Config

import scalafx.application.Platform
import scalafx.scene.control.{Alert, Menu, MenuBar, MenuItem, SeparatorMenuItem}
import scalafx.scene.control.Alert.AlertType

import hs.App

class MenuPane(conf: Config) extends MenuBar:
  val aboutDialog = new Alert(AlertType.Information):
    initOwner(App.stage)
    title = conf.getString("about")
    headerText = conf.getString("developer")
    contentText = s"${conf.getString("app")} ${conf.getString("license")}"

  val aboutMenuItem = new MenuItem(conf.getString("about")):
    onAction = { _ => aboutDialog.showAndWait() }

  val separator = SeparatorMenuItem()

  val exitMenuItem = new MenuItem(conf.getString("exit")):
    onAction = { _ => Platform.exit() }
  
  val menu = new Menu(conf.getString("menu")):
    items = List(aboutMenuItem, separator, exitMenuItem)

  menus = List(menu)
  useSystemMenuBar = false