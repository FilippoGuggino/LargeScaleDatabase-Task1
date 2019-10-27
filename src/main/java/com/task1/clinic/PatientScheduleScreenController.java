package com.task1.clinic;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PatientScheduleScreenController {

    @FXML
    private void switchToInitial() throws IOException {
        App.setRoot("initialScreen");
    }
    @FXML
    private void addMedical() throws IOException {
        TextField firstNameInput=(TextField) App.scene.lookup("#firstnameInput");
        TextField lastNameInput=(TextField) App.scene.lookup("#lastnameInput");
        DatePicker selectedDate=(DatePicker)App.scene.lookup("#selectedDate");
        Label errorLabel=(Label) App.scene.lookup("#errorLabel");
        if(selectedDate.getValue()==null|| firstNameInput.getText().equals("")||lastNameInput.getText().equals("")){
            errorLabel.setText("All fields must be filled!");
            errorLabel.setVisible(true);
        }
        else{
            //TODO addmedical
            errorLabel.setVisible(false);
        }
    }
}