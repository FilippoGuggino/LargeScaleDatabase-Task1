package com.task1.clinic;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EmployeeScheduleScreenController {

    @FXML
    private void switchToInitial() throws IOException {
        App.setRoot("initialScreen");
    }
    @FXML
    private void addMedical() throws IOException {
        TextField patFirstNameInput=(TextField) App.scene.lookup("#patFirstnameInput");
        TextField patLastNameInput=(TextField) App.scene.lookup("#patLastnameInput");
        TextField docFirstNameInput=(TextField) App.scene.lookup("#docFirstnameInput");
        TextField docLastNameInput=(TextField) App.scene.lookup("#docLastnameInput");
        DatePicker selectedDate=(DatePicker)App.scene.lookup("#selectedDate");
        Label errorLabel=(Label) App.scene.lookup("#errorLabel");
        if(selectedDate.getValue()==null|| patFirstNameInput.getText().equals("")||patLastNameInput.getText().equals("") ||docFirstNameInput.getText().equals("")||docLastNameInput.getText().equals("")){
            errorLabel.setText("All fields must be filled!");
            errorLabel.setVisible(true);
        }
        else{
            //TODO addmedical
      /*      errorLabel.setVisible(false);
            patFirstNameInput.setText("");
            patLastNameInput.setText("");
            docFirstNameInput.setText("");
            docLastNameInput.setText("");
            selectedDate.set*/
        }
    }
}
