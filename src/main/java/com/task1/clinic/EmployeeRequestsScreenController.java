package com.task1.clinic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EmployeeRequestsScreenController implements Initializable {

    private ArrayList<Object> medicalRows=new ArrayList<>(); //List that stores the medicals for each row.

    @FXML
    private Button approveButton;
    @FXML
    private Button rejectButton;
    @FXML
    private TableView tableView;
    @FXML
    private Label requestTypeLabel;
    private int selectedIndex;
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources){
        switch(App.requestType){
            case 0:
                requestTypeLabel.setText("New medical requests");
                break;
            case 1:
                requestTypeLabel.setText("Delete requests");
                break;
            case 2:
                requestTypeLabel.setText("Move requests");
                break;
        }

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
        tableView.getColumns().addAll(doctorCol, patientCol,medicalDateCol);
        if(App.requestType==2){
            TableColumn medicalNewDateCol = new TableColumn("New date");
            medicalNewDateCol.setCellValueFactory(
                    new PropertyValueFactory<MedicalBean,String>("newMedicalDate")
            );
            medicalNewDateCol.setStyle("-fx-alignment:CENTER");
            tableView.getColumns().addAll(medicalNewDateCol);

        }

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
                approveButton.setDisable(false);
                rejectButton.setDisable(false);
            }
        });
    }
    @FXML
    private void updateTable() throws IOException {
        medicalRows.clear();
        ObservableList<MedicalBean> observableMedicals= FXCollections.observableArrayList();
        Employee e=(Employee)App.user;
        switch(App.requestType){
            case 0:
                for(Medical m:e.getCreateRequests()){
                    observableMedicals.add(m.toBean());
                    medicalRows.add(m);
                }
                break;
            case 1:
                for(DeleteRequest m:e.getDeleteRequests()){
                    observableMedicals.add(m.toBean());
                    medicalRows.add(m);
                }
                break;
            case 2:
                for(MoveRequest m:e.getMoveRequests()){
                    observableMedicals.add(m.toBean());
                    medicalRows.add(m);
                }
                break;
        }
        tableView.setItems(observableMedicals);

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
    private void handleRequest(Boolean outcome) throws IOException{
        Employee e=(Employee)App.user;
        switch(App.requestType){
            case 0:
                Medical medicalToDelete=(Medical)medicalRows.get(selectedIndex);
                e.handleCreateRequest(medicalToDelete,outcome);
                break;
            case 1:
                DeleteRequest deleteRequest=(DeleteRequest)medicalRows.get(selectedIndex);
                e.handleDeleteRequest(deleteRequest,outcome);
                break;
            case 2:
                MoveRequest moveRequest=(MoveRequest)medicalRows.get(selectedIndex);
                e.handleMoveRequest(moveRequest,outcome);
                break;
        }
        updateTable();
    }
    @FXML
    private void rejectRequest() throws IOException{
        handleRequest(false);
        AlertBox.display("The request has been rejected");

    }
    @FXML
    private void approveRequest() throws IOException{
        handleRequest(true);
        AlertBox.display("The request has been approved");

    }

}
