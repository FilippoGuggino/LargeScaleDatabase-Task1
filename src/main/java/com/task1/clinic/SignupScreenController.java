package com.task1.clinic;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SignupScreenController {

    @FXML
    private void signup() throws IOException {
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
        PersistenceManager man = PersistenceManager.getInstance();
        switch(roleInput.getValue().toString()){
            case "Doctor":
                App.setUser(new Doctor(firstName, lastName));
                if(App.user == null){
                    man.create(App.user);
                    App.setRoot("doctorScheduleScreen");
                }
                break;
            case "Patient":
                App.setUser(new Patient(firstName, lastName));
                if(App.user == null){
                    man.create(App.user);
                    App.setRoot("patientScheduleScreen");
                }
                break;
            case "Employee":
                App.setUser(new Employee(firstName, lastName));
                if(App.user == null){
                    man.create(App.user);
                    App.setRoot("employeeMenuScreen");
                }
                break;
        }
        firstNameInput.setText("");
        lastNameInput.setText("");
        roleInput.setValue(null);
        errorLabel.setText("The selected user already exists");
        errorLabel.setVisible(true);
    }
}