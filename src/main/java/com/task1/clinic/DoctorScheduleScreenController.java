package com.task1.clinic;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DoctorScheduleScreenController {

    @FXML
    private void switchToInitial() throws IOException {
            App.setRoot("initialScreen");
    }
    @FXML
    private void updateSchedule() throws IOException {
        DatePicker selectedDate=(DatePicker)App.scene.lookup("#selectedDate");
        Label errorLabel=(Label) App.scene.lookup("#errorLabel");
        if(selectedDate.getValue()==null){
            errorLabel.setText("Select a date!");
            errorLabel.setVisible(true);
        }
        else{
            errorLabel.setVisible(false);
            //TODO updateTable
        }
    }
}