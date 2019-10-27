package com.task1.clinic;

import java.io.IOException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
            ObservableList<UserBean> mm= FXCollections.observableArrayList(
                    new UserBean("mamaaaaa","mamaaaaa"),
                    new UserBean("mamaaaaa","mamaaaaa"),
                    new UserBean("mamaaaaa","mamaaaaa"),
                    new UserBean("mamaaaaa","mamaaaaa"));

            TableColumn nameCol = new TableColumn("Patient first name");
            nameCol.setCellValueFactory(
                    new PropertyValueFactory<UserBean,String>("patName")
            );
            TableColumn surnameCol = new TableColumn("Patient last name");
            surnameCol.setCellValueFactory(
                    new PropertyValueFactory<UserBean,String>("patSurname")
            );
            TableView tableView=(TableView)App.scene.lookup("#tableView");
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableView.setItems(mm);
            tableView.getColumns().addAll(nameCol, surnameCol);

        }
    }
}