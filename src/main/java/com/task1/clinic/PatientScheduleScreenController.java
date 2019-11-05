package com.task1.clinic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PatientScheduleScreenController implements Initializable {

    @FXML
    private TableView tableView;
    private ArrayList<Medical> medicalRows=new ArrayList<>();
    private int selectedIndex;
    @FXML
    private TextField firstnameInput;
    @FXML
    private TextField lastnameInput;
    @FXML
    private DatePicker selectedDate;
    @FXML
    private DatePicker newDate;
    @FXML
    private Label errorLabel;
    @FXML
    private Button deleteButton;
    @FXML
    private Button moveButton;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources){
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
        tableView.getColumns().addAll(nameCol, surnameCol,medicalDateCol);
        try{
            updateTable();
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
                moveButton.setDisable(false);
            }
        });
    }
    @FXML
    private void switchToInitial() throws IOException {
        App.setRoot("initialScreen");
    }
    @FXML
    private void updateTable() throws IOException {
        medicalRows.clear();
        ObservableList<MedicalBean> observableMedicals= FXCollections.observableArrayList();
        for(Medical m:App.user.getSchedule()) {
            observableMedicals.add(m.toBean());
            medicalRows.add(m);
        }
        tableView.setItems(observableMedicals);
    }
    @FXML
    private void addMedicalRequest()throws IOException{
        if(selectedDate.getValue()==null|| firstnameInput.getText().equals("")||lastnameInput.getText().equals("")){
            errorLabel.setText("All fields must be filled!");
            errorLabel.setVisible(true);
        }
        else {
            Doctor d = Doctor.logIn(firstnameInput.getText(), lastnameInput.getText());
            if (d == null) {
                errorLabel.setVisible(true);
                errorLabel.setText("Selected doctor doesn't exist!");
                return;
            }
            errorLabel.setVisible(false);
            Patient p = (Patient) App.user;
            Date date = java.sql.Date.valueOf(selectedDate.getValue());
            if (p.createMedicalRequest(d, date) == null) {
                errorLabel.setVisible(true);
                errorLabel.setText("Medical already exists");
            } else {
                errorLabel.setVisible(false);
            }
            firstnameInput.setText("");
            lastnameInput.setText("");
        }
    }
    @FXML
    private void addDeleteRequest()throws IOException{
        Medical medicalToDelete=medicalRows.get(selectedIndex);
        Patient p=(Patient)App.user;
        p.deleteRequest(medicalToDelete);
        errorLabel.setVisible(false);
    }
    @FXML
    private void addMoveRequest()throws IOException{
        if(newDate.getValue()==null){
            errorLabel.setText("fill all the fields!");
            errorLabel.setVisible(true);
            return;
        }
        errorLabel.setVisible(false);
        Medical medicalToUpdate=medicalRows.get(selectedIndex);
        Patient p=(Patient)App.user;
        Date date = java.sql.Date.valueOf(newDate.getValue());
        p.moveRequest(medicalToUpdate,date);
    }
}