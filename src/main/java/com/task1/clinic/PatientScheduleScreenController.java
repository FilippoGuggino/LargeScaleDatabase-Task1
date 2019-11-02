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
    @FXML
    private TextField firstNameInput;
    @FXML
    private TextField lastNameInput;
    @FXML
    private Label errorLabel;
    @FXML
    DatePicker selectedDate;
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
                System.out.println(tableView.getSelectionModel().getSelectedIndex());
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
        if(selectedDate.getValue()==null|| firstNameInput.getText().equals("")||lastNameInput.getText().equals("")){
            errorLabel.setText("All fields must be filled!");
            errorLabel.setVisible(true);
        }
        else{
            errorLabel.setVisible(false);
            Doctor d=new Doctor(firstNameInput.getText(),lastNameInput.getText());
            Patient p=(Patient)App.user;
            Date date = java.sql.Date.valueOf(selectedDate.getValue());
            p.createMedicalRequest(d,date);
            firstNameInput.setText("");
            lastNameInput.setText("");
        }
    }
    @FXML
    private void addDeleteRequest()throws IOException{
        int selectedIndex= tableView.getSelectionModel().getSelectedIndex();
        System.out.println(selectedIndex);
        Medical medicalToDelete=medicalRows.get(selectedIndex);
        Patient p=(Patient)App.user;
        p.deleteRequest(medicalToDelete);
    }
    @FXML
    private void addMoveRequest()throws IOException{
        int selectedIndex= tableView.getSelectionModel().getSelectedIndex();
        DatePicker newDate=(DatePicker)App.scene.lookup("#newDate");
        Label errorLabel2=(Label)App.scene.lookup("#errorLabel2");
        if(newDate.getValue()==null){
            errorLabel2.setText("fill all the fields!");
            errorLabel2.setVisible(false);
            return;
        }
        Medical medicalToUpdate=medicalRows.get(selectedIndex);
        Patient p=(Patient)App.user;
        Date date = java.sql.Date.valueOf(newDate.getValue());
        p.moveRequest(medicalToUpdate,date);
    }
}