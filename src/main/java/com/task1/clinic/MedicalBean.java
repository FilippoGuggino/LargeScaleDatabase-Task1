package com.task1.clinic;

import com.task1.clinic.Doctor;
import com.task1.clinic.Patient;
import javafx.beans.property.SimpleStringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MedicalBean {
    private SimpleStringProperty doctor;
    private SimpleStringProperty patient;
    private SimpleStringProperty medicalDate;
    private SimpleStringProperty newMedicalDate;

    public MedicalBean(Doctor doctor, Patient patient, Date date, Date newDate){
        this(doctor,patient,date);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String newDate_str = df.format(newDate);
        this.newMedicalDate=new SimpleStringProperty(newDate_str);
    }
    public MedicalBean(Doctor doctor, Patient patient, Date date){
        this.doctor=new SimpleStringProperty(doctor.getFirstName()+" "+doctor.getLastName());
        this.patient=new SimpleStringProperty(patient.getFirstName()+" "+patient.getLastName());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date_str = df.format(date);
        this.medicalDate=new SimpleStringProperty(date_str);
    }
    public String getDoctor(){
        return doctor.get();
    }
    public String getPatient(){
        return patient.get();
    }
    public String getMedicalDate(){
        return medicalDate.get();
    }
    public String getNewMedicalDate(){ return newMedicalDate.get(); }

}
