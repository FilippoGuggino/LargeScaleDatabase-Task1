package com.task1.clinic;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SignupScreenController {

    @FXML
    private void switchToInitial() throws IOException {
        App.setRoot("initialScreen");
    }
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
                //preventive login to check if user already exists
                App.setUser(Doctor.logIn(firstName,lastName));
                if(App.user == null){
                    //user didn't exist, proceeding with sign up:
                    App.setUser(new Doctor(firstName, lastName));
                    man.create(App.user);
                    App.setRoot("doctorScheduleScreen");
                    return;
                }
                break;
            case "Patient":
                //preventive login to check if user already exists
                App.setUser(Patient.logIn(firstName,lastName));
                if(App.user == null){
                    //user didn't exist, proceeding with sign up:
                    App.setUser(new Patient(firstName, lastName));
                    man.create(App.user);
                    App.setRoot("patientScheduleScreen");
                    return;
                }
                break;
            case "Employee":
                //preventive login to check if user already exists
                App.setUser(Employee.logIn(firstName,lastName));
                if(App.user == null){
                    //user didn't exist, proceeding with sign up:
                    App.setUser(new Employee(firstName, lastName));
                    man.create(App.user);
                    App.setRoot("employeeMenuScreen");
                    return;
                }
                break;
        }

        //user did exist, cleaning and showing error message:
        App.setUser(null);
        firstNameInput.setText("");
        lastNameInput.setText("");
        roleInput.setValue(null);
        errorLabel.setText("The selected user already exists");
        errorLabel.setVisible(true);
    }
}