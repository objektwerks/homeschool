package hs.dialog

import com.typesafe.config.Config

import hs.App
import hs.Grade
import hs.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.Region

class GradeDialog(conf: Config, grade: Grade) extends Dialog[Grade] {
  val yearTextField = new TextField {
    text = grade.year
  }
  val startedDatePicker = new DatePicker {
    value = grade.started
  }
  val completedDatePicker = new DatePicker {
    value = grade.completed
  }
  val controls = List[(String, Region)](
    conf.getString("year") -> yearTextField,
    conf.getString("started") -> startedDatePicker,
    conf.getString("completed") -> completedDatePicker)
  val controlGridPane = new ControlGridPane(controls)


  val dialog = dialogPane()
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  saveButton.disable = yearTextField.text.value.trim.isEmpty
  yearTextField.text.onChange { (_, _, newValue) =>
    saveButton.disable = newValue.trim.isEmpty
  }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      grade.copy(
        year = yearTextField.text.value,
        started = startedDatePicker.value.value,
        completed = completedDatePicker.value.value)
    else null
  }

  initOwner(App.stage)
  title = conf.getString("grade")
  headerText = conf.getString("save-grade")
}