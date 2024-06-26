package hs.dialog

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, Dialog, DatePicker, TextField}
import scalafx.scene.layout.Region

import hs.{App, Context, Entity, Grade}
import hs.pane.ControlGridPane

class GradeDialog(context: Context, grade: Grade) extends Dialog[Grade]:
  val yearTextField = new TextField:
    text = grade.year

  val startedDatePicker = new DatePicker:
    value = Entity.toLocalDate(grade.started)

  val completedDatePicker = new DatePicker:
    value = Entity.toLocalDate(grade.completed)

  val controls = List[(String, Region)](
    context.year -> yearTextField,
    context.started -> startedDatePicker,
    context.completed -> completedDatePicker)
  val controlGridPane = ControlGridPane(controls)


  val dialog = dialogPane()
  val saveButtonType = ButtonType(context.save, ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  saveButton.disable = yearTextField.text.value.trim.isEmpty
  yearTextField.text.onChange { (_, _, newValue) =>
    saveButton.disable = newValue.trim.isEmpty
  }

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      grade.copy(
        year = yearTextField.text.value,
        started = startedDatePicker.value.value.toString,
        completed = completedDatePicker.value.value.toString
      )
    else null

  initOwner(App.stage)
  title = context.grade
  headerText = context.saveGrade