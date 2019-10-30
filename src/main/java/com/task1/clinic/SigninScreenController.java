package com.task1.clinic;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SigninScreenController {

    @FXML
    private void signin() throws IOException {
        TextField firstNameInput=(TextField) App.scene.lookup("#firstnameInput");
        TextField lastNameInput=(TextField) App.scene.lookup("#lastnameInput");
        ChoiceBox roleInput=(ChoiceBox) App.scene.lookup("#roleInput");
        Label errorLabel=(Label) App.scene.lookup("#errorLabel");
        if(firstNameInput.getText().equals("") ||lastNameInput.getText().equals("") ||roleInput.getValue()==null){
            errorLabel.setText("All fields must be filled!");
            errorLabel.setVisible(true);
            return;
        }
        String firstName=firstNameInput.getText();
        String lastName=lastNameInput.getText();
        switch(roleInput.getValue().toString()){
            case "Doctor":
                App.setUser(Doctor.logIn(firstName,lastName));
                App.setRoot("doctorScheduleScreen");
                break;
            case "Patient":
                App.setUser(Patient.logIn(firstName,lastName));
                App.setRoot("patientScheduleScreen");
                break;
            case "Employee":
                App.setUser(Employee.logIn(firstName,lastName));
                App.setRoot("employeeMenuScreen");
                break;
        }
        //if(App.user == null) then the user doesn't exist
        // else ok
    }
}