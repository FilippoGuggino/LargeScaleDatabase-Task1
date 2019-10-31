package com.task1.clinic;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
public class DoctorScheduleScreenController implements Initializable  {

    @FXML
    private TableView tableView;
    @FXML
    private DatePicker selectedDate;
    @FXML
    private Label errorLabel;
    @FXML
    private Label labelScheduleDate;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources){
        selectedDate.setValue(LocalDate.now());
        TableColumn nameCol = new TableColumn("Patient first name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<UserBean,String>("firstName")
        );
        TableColumn surnameCol = new TableColumn("Patient last name");
        surnameCol.setCellValueFactory(
                new PropertyValueFactory<UserBean,String>("lastName")
        );
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(nameCol, surnameCol);
        try{updateTable();}
        catch(IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    private void switchToInitial() throws IOException {
        App.setRoot("initialScreen");
    }
    @FXML
    private void updateTable() throws IOException {
        if(selectedDate.getValue()==null){
            errorLabel.setText("Select a date!");
            errorLabel.setVisible(true);
        }
        else{
            errorLabel.setVisible(false);
            labelScheduleDate.setText(selectedDate.getValue()+" SCHEDULE: ");
            ObservableList<UserBean> observablePatients=FXCollections.observableArrayList();
            Doctor d=(Doctor)App.user;
            Date date = java.sql.Date.valueOf(selectedDate.getValue());
            for(Medical m:d.getSchedule(date))
                observablePatients.add(m.getPatient().toBean());
            tableView.setItems(observablePatients);
        }
    }

}