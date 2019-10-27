package com.task1.clinic;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EmployeeMenuScreenController {

    @FXML
    private void switchToInitial() throws IOException {
        App.setRoot("initialScreen");
    }
    @FXML
    private void switchToScheduleScreen() throws IOException {
        App.setRoot("employeeScheduleScreen");
    }
    @FXML
    private void switchToNewMedRequestsScreen() throws IOException {
        App.setRoot("employeeRequestsScreen");
        Label requestTypeLabel=(Label)App.scene.lookup("#requestTypeLabel");
        requestTypeLabel.setText("New medical requests");
    }
    @FXML
    private void switchToDeleteRequestsScreen() throws IOException {
        App.setRoot("employeeRequestsScreen");
        Label requestTypeLabel=(Label)App.scene.lookup("#requestTypeLabel");
        requestTypeLabel.setText("Delete requests");
    }
    @FXML
    private void switchToMoveRequestsScreen() throws IOException {
        App.setRoot("employeeRequestsScreen");
        Label requestTypeLabel=(Label)App.scene.lookup("#requestTypeLabel");
        requestTypeLabel.setText("Move requests");
    }

}
