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

public class EmployeeRequestsScreenController implements Initializable {

    private ArrayList<Medical> medicalRows=new ArrayList<>(); //List that stores the medicals for each row.

    @FXML
    private Button approveButton;
    @FXML
    private Button rejectButton;
    @FXML
    private TableView tableView;
    @FXML
    private Label requestTypeLabel;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources){
        switch(App.requestType){
            case 0:
                requestTypeLabel.setText("New medical requests");
                break;
            case 1:
                requestTypeLabel.setText("Move requests");
                break;
            case 2:
                requestTypeLabel.setText("Delete requests");
                break;
        }
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
        tableView.getColumns().addAll(patNameCol, patSurnameCol,docNameCol, docSurnameCol,medicalDateCol);
        if(App.requestType==2){
            TableColumn medicalNewDateCol = new TableColumn("New date");
            medicalDateCol.setCellValueFactory(
                    new PropertyValueFactory<MedicalBean,String>("newDate")
            );
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
      /*          System.out.println(tableView.getSelectionModel().getSelectedIndex());
                deleteButton.setDisable(false);
                moveButton.setDisable(false);   */
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
                    medicalRows.add(m.getMedical());
                }
                break;
            case 2:
                for(MoveRequest m:e.getMoveRequests()){
                    observableMedicals.add(m.toBean());
                    medicalRows.add(m.getMedical());
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


}
