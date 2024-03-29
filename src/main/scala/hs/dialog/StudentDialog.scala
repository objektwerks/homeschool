package hs.dialog

import com.typesafe.config.Config

import hs.App
import hs.Student
import hs.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.Region

class StudentDialog(conf: Config, student: Student) extends Dialog[Student] {
  val nameTextField = new TextField {
    text = student.name
  }
  val bornDatePicker = new DatePicker {
    value = student.born
  }
  val controls = List[(String, Region)](conf.getString("name") -> nameTextField, conf.getString("born") -> bornDatePicker)
  val controlGridPane = new ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  saveButton.disable = nameTextField.text.value.trim.isEmpty
  nameTextField.text.onChange { (_, _, newValue) =>
    saveButton.disable = newValue.trim.isEmpty
  }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      student.copy(name = nameTextField.text.value, born = bornDatePicker.value.value)
    else null
  }

  initOwner(App.stage)
  title = conf.getString("student")
  headerText = conf.getString("save-student")
}