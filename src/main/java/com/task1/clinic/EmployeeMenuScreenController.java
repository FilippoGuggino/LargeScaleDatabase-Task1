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
        //Setting static int requestType=0 means that the visualized requests regard new medicals.
        App.requestType=0;
        App.setRoot("employeeRequestsScreen");
    }
    @FXML
    private void switchToDeleteRequestsScreen() throws IOException {
        App.requestType=1;
        App.setRoot("employeeRequestsScreen");
    }
    @FXML
    private void switchToMoveRequestsScreen() throws IOException {
        App.requestType=2;
        App.setRoot("employeeRequestsScreen");
    }

}
