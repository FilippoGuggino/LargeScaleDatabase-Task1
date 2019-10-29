package com.task1.clinic;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PatientScheduleScreenController implements Initializable {

    @FXML
    private TableView tableView;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources){
        try{updateMedicalTable();}
        catch(IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    private void switchToInitial() throws IOException {
        App.setRoot("initialScreen");
    }
    @FXML
    private void updateMedicalTable() throws IOException {
            ObservableList<MedicalBean> mm= FXCollections.observableArrayList(
                   );
            TableColumn nameCol = new TableColumn("Doc_Name");
            nameCol.setCellValueFactory(
                    new PropertyValueFactory<MedicalBean,String>("doctorFirstName")
            );
            TableColumn surnameCol = new TableColumn("Doc_Surname");
            surnameCol.setCellValueFactory(
                    new PropertyValueFactory<MedicalBean,String>("doctorLastName")
            );
            TableColumn medicalDateCol = new TableColumn("Date");
            medicalDateCol.setCellValueFactory(
                    new PropertyValueFactory<MedicalBean,String>("medicalDate")
            );
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableView.setItems(mm);
            tableView.getColumns().addAll(nameCol, surnameCol,medicalDateCol);
        }
    @FXML
    private void addMedical()throws IOException{
        TextField firstNameInput=(TextField) App.scene.lookup("#firstnameInput");
        TextField lastNameInput=(TextField) App.scene.lookup("#lastnameInput");
        DatePicker selectedDate=(DatePicker)App.scene.lookup("#selectedDate");
        Label errorLabel=(Label) App.scene.lookup("#errorLabel");
        if(selectedDate.getValue()==null|| firstNameInput.getText().equals("")||lastNameInput.getText().equals("")){
            errorLabel.setText("All fields must be filled!");
            errorLabel.setVisible(true);
        }
        else{
            errorLabel.setVisible(false);
            updateMedicalTable();
        }
    }
}