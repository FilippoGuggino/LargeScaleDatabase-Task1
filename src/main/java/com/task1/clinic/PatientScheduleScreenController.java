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
import javafx.stage.Popup;

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
        TableColumn docCol = new TableColumn("Doctor");
        docCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("doctor")
        );
        docCol.setStyle("-fx-alignment:CENTER");

        TableColumn medicalDateCol = new TableColumn("Date");
        medicalDateCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("medicalDate")
        );
        medicalDateCol.setStyle("-fx-alignment:CENTER");

        TableColumn approvedCol = new TableColumn("Approved");
        approvedCol.setCellValueFactory(
                new PropertyValueFactory<MedicalBean,String>("approved")
        );
        approvedCol.setStyle("-fx-alignment:CENTER");

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(docCol,medicalDateCol,approvedCol);
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
            Date today = new Date();
            if(date.getTime() < today.getTime()) {
                errorLabel.setText("You must select a date in the future");
                errorLabel.setVisible(true);
                return;
            }
            if (p.createMedicalRequest(d, date) == null) {
                errorLabel.setVisible(true);
                errorLabel.setText("Medical already exists");
            }
            else {
                errorLabel.setVisible(false);
                AlertBox.display("The request has been sent");
                updateTable();

            }
            firstnameInput.setText("");
            lastnameInput.setText("");
            

        }
    }
    @FXML
    private void addDeleteRequest()throws IOException{
        errorLabel.setVisible(false);
        Medical medicalToDelete=medicalRows.get(selectedIndex);
        medicalToDelete = medicalToDelete.connect();
        Patient p=(Patient)App.user;
        if(p.deleteRequest(medicalToDelete)==null){
            errorLabel.setText("The request has already been sent!");
            errorLabel.setVisible(true);
        }
        else{
            AlertBox.display("The request has been sent");
        }
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
        medicalToUpdate = medicalToUpdate.connect();
        Patient p=(Patient)App.user;
        Date date = java.sql.Date.valueOf(newDate.getValue());
        Date today = new Date();
        if(date.getTime() < today.getTime()) {
            errorLabel.setText("You must select a date in the future");
            errorLabel.setVisible(true);
            return;
        }
        if(p.moveRequest(medicalToUpdate,date)==null){
            errorLabel.setText("The request has already been sent!");
            errorLabel.setVisible(true);
        }
        else
            AlertBox.display("The request has been sent");

    }
}