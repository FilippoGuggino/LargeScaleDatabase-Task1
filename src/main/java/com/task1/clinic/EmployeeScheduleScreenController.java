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
    private ArrayList<Medical> medicalRows=new ArrayList<>();

    @FXML
    private TextField patFirstNameFilter;
    @FXML
    private TextField patLastNameFilter;
    @FXML
    private TextField docFirstNameFilter;
    @FXML
    private TextField docLastNameFilter;
    @FXML
    private DatePicker selectedDateFilter;
    @FXML
    private Label errorLabel;
    @FXML
    private Button applyButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button moveButton;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources){
        TableColumn docNameCol = new TableColumn("Doc_Name");
        docNameCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("doctorFirstName")
        );
        TableColumn docSurnameCol = new TableColumn("Doc_Surname");
        docSurnameCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("doctorLastName")
        );
        TableColumn patNameCol = new TableColumn("Pat_Name");
        patNameCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("patientFirstName")
        );
        TableColumn patSurnameCol = new TableColumn("Pat_Surname");
        patSurnameCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("patientLastName")
        );
        TableColumn medicalDateCol = new TableColumn("Date");
        medicalDateCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("medicalDate")
        );
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(docNameCol, docSurnameCol,patNameCol, patSurnameCol,medicalDateCol);
        try{
            updateTable();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
      /*          System.out.println(tableView.getSelectionModel().getSelectedIndex());
                deleteButton.setDisable(false);
                moveButton.setDisable(false);   */
            }
        });
    }
    @FXML
    private void switchToInitial() throws IOException {
        App.setRoot("initialScreen");
    }
    @FXML
    private void updateTable() throws IOException {
        medicalRows= (ArrayList<Medical>) App.user.getSchedule();
        ObservableList<MedicalBean> observableMedicals= FXCollections.observableArrayList();
        for(Medical m:App.user.getSchedule())
            observableMedicals.add(m.toBean());
        tableView.setItems(observableMedicals);
    }
    @FXML
    private void applyFilters(){
        if(selectedDateFilter.getValue()==null && patFirstNameFilter.getText().equals("")&&patLastNameFilter.getText().equals("") &&docFirstNameFilter.getText().equals("")&&docLastNameFilter.getText().equals("")){
            errorLabel.setText("Select at least one filter!");
            errorLabel.setVisible(true);
            return;
        }
        Patient p;
        Doctor d;
        if(patFirstNameFilter.getText().equals(""))
            p=null;
        else
            p=new Patient(patFirstNameFilter.getText(),patLastNameFilter.getText());
        if(docFirstNameFilter.getText().equals(""))
            d=null;
        else
            d=new Doctor(docFirstNameFilter.getText(),docLastNameFilter.getText());
        ObservableList<MedicalBean> observableMedicals= FXCollections.observableArrayList();
        Employee e=(Employee)App.user;
        Date date = java.sql.Date.valueOf(selectedDateFilter.getValue());
        for(Medical m:e.getSchedule(p,d,date))
            observableMedicals.add(m.toBean());
    }
    @FXML
    private void addMedical() throws IOException {
        TextField patFirstNameInput=(TextField) App.scene.lookup("#patFirstnameInput");
        TextField patLastNameInput=(TextField) App.scene.lookup("#patLastnameInput");
        TextField docFirstNameInput=(TextField) App.scene.lookup("#docFirstnameInput");
        TextField docLastNameInput=(TextField) App.scene.lookup("#docLastnameInput");
        DatePicker selectedDate=(DatePicker)App.scene.lookup("#selectedDate");
        Label errorLabel=(Label) App.scene.lookup("#errorLabel");
        if(selectedDate.getValue()==null|| patFirstNameInput.getText().equals("")||patLastNameInput.getText().equals("") ||docFirstNameInput.getText().equals("")||docLastNameInput.getText().equals("")){
            errorLabel.setText("All fields must be filled!");
            errorLabel.setVisible(true);
        }
        else{
            //TODO addmedical
      /*      errorLabel.setVisible(false);
            patFirstNameInput.setText("");
            patLastNameInput.setText("");
            docFirstNameInput.setText("");
            docLastNameInput.setText("");
            selectedDate.set*/
        }
    }
}
