package com.task1.clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class EmployeeScheduleScreenController implements Initializable {

    @FXML
    private TableView tableView;
    private ArrayList<Medical> medicalRows=new ArrayList<>(); //List that stores the medicals for each row.

    @FXML
    private TextField patFirstnameFilter;
    @FXML
    private TextField patLastnameFilter;
    @FXML
    private TextField docFirstnameFilter;
    @FXML
    private TextField docLastnameFilter;
    @FXML
    private DatePicker selectedDateFilter;
    @FXML
    private TextField patFirstnameInput;
    @FXML
    private TextField patLastnameInput;
    @FXML
    private TextField docFirstnameInput;
    @FXML
    private TextField docLastnameInput;
    @FXML
    private DatePicker selectedDate;
    @FXML
    private Label errorLabel;
    @FXML
    private Button applyButton;
    @FXML
    private Button deleteButton;
    private int selectedIndex;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources){
        TableColumn doctorCol = new TableColumn("Doctor");
        doctorCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("doctor")
        );
        doctorCol.setStyle("-fx-alignment:CENTER");

        TableColumn patientCol = new TableColumn("Patient");
        patientCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("patient")
        );
        patientCol.setStyle("-fx-alignment:CENTER");

        TableColumn medicalDateCol = new TableColumn("Date");
        medicalDateCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("medicalDate")
        );
        medicalDateCol.setStyle("-fx-alignment:CENTER");

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(patientCol,doctorCol,medicalDateCol);
        try{
            updateTable(null,null,null);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                selectedIndex=tableView.getSelectionModel().getSelectedIndex();
                //Verify if a row is selected
                if(selectedIndex<0)
                    return;
                deleteButton.setDisable(false);
            }
        });
    }
    @FXML
    private void switchToInitial() throws IOException {
        App.setRoot("initialScreen");
    }
    @FXML
    private void switchToEmployeeMenuScreen() throws IOException {
        App.setRoot("employeeMenuScreen");
    }
    @FXML
    private void updateTable(Patient p,Doctor d,Date date) throws IOException {
        medicalRows.clear();
        ObservableList<MedicalBean> observableMedicals= FXCollections.observableArrayList();
        Employee e=(Employee)App.user;
        if(p==null && d==null && date==null){ //Initializing tableView
            for(Medical m:e.getSchedule()){
                observableMedicals.add(m.toBean());
                medicalRows.add(m);
            }
        }
        else
            for(Medical m:e.getSchedule(p,d,date)){ //Applying filters
                observableMedicals.add(m.toBean());
                medicalRows.add(m);
            }
        tableView.setItems(observableMedicals);
    }
    @FXML
    private void applyFilters()throws IOException{
        //check if the user selects at least one field
        if(selectedDateFilter.getValue()==null && patFirstnameFilter.getText().equals("")&&patLastnameFilter.getText().equals("") &&docFirstnameFilter.getText().equals("")&&docLastnameFilter.getText().equals("")){
            errorLabel.setText("Select at least one filter!");
            errorLabel.setVisible(true);
            return;
        }
        Date date;
        Patient p=Patient.logIn(patFirstnameFilter.getText(),patLastnameFilter.getText());
        Doctor d=Doctor.logIn(docFirstnameFilter.getText(),docLastnameFilter.getText());
        //check if the user selects a non existing patient
        if(p==null && (!patFirstnameFilter.getText().equals("") || !patLastnameFilter.getText().equals(""))){
            errorLabel.setText("Selected patient doesn't exist.");
            errorLabel.setVisible(true);
            return;
        }
        //check if the user selects a non existing doctor
        if(d==null && (!docFirstnameFilter.getText().equals("") || !docLastnameFilter.getText().equals(""))){
            errorLabel.setText("Selected doctor doesn't exist.");
            errorLabel.setVisible(true);
            return;
        }
        errorLabel.setVisible(false);
        if(selectedDateFilter.getValue()==null)
            date=null;
        else
            date = java.sql.Date.valueOf(selectedDateFilter.getValue());
        updateTable(p,d,date);
    }
    @FXML
    private void addMedical() throws IOException {

        if(selectedDate.getValue()==null|| patFirstnameInput.getText().equals("")||patLastnameInput.getText().equals("") ||docFirstnameInput.getText().equals("")||docLastnameInput.getText().equals("")){
            errorLabel.setText("All fields must be filled!");
            errorLabel.setVisible(true);
        }
        else{
            Date date;
            Patient p=Patient.logIn(patFirstnameInput.getText(),patLastnameInput.getText());
            Doctor d=Doctor.logIn(docFirstnameInput.getText(),docLastnameInput.getText());
            //check if the user selects a non existing patient
            if(p==null){
                errorLabel.setText("Selected patient doesn't exist.");
                errorLabel.setVisible(true);
                return;
            }
            //check if the user selects a non existing doctor
            if(d==null){
                errorLabel.setText("Selected doctor doesn't exist.");
                errorLabel.setVisible(true);
                return;
            }
            date = java.sql.Date.valueOf(selectedDate.getValue());
            Employee e=(Employee)App.user;
            Medical m=new Medical(d,p,date);
            if(e.addMedical(m)){
                errorLabel.setVisible(false);
            }
            else{
                errorLabel.setVisible(true);
                errorLabel.setText("Medical already exists");
            }
            patFirstnameInput.setText("");
            patLastnameInput.setText("");
            docFirstnameInput.setText("");
            docLastnameInput.setText("");
        }
    }
    @FXML
    public void dropMedical(){
        Employee e=(Employee)App.user;
        Medical medicalToDelete=medicalRows.get(selectedIndex);

        //if medical is taken from cache it must be attached to database
        medicalToDelete = medicalToDelete.connect();

        e.dropMedical(medicalToDelete);
        //TODO update the table
    }
}
