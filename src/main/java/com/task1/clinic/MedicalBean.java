package com.task1.clinic;

import com.task1.clinic.Doctor;
import com.task1.clinic.Patient;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class MedicalBean {
    private SimpleStringProperty doctorFirstName;
    private SimpleStringProperty doctorLastName;
    private SimpleStringProperty patientFirstName;
    private SimpleStringProperty patientLastName;
    private SimpleStringProperty medicalDate;
    private SimpleStringProperty newMedicalDate;

    public MedicalBean(Doctor doctor, Patient patient, Date date, Date newDate){
        this(doctor,patient,date);
        this.newMedicalDate=new SimpleStringProperty(newDate.toString());
    }
    public MedicalBean(Doctor doctor, Patient patient, Date date){
        this.doctorFirstName=new SimpleStringProperty(doctor.getFirstName());
        this.doctorLastName=new SimpleStringProperty(doctor.getLastName());
        this.patientFirstName=new SimpleStringProperty(patient.getFirstName());
        this.patientLastName=new SimpleStringProperty(patient.getLastName());
        this.medicalDate=new SimpleStringProperty(date.toString());
    }
    public String getDoctorFirstName(){
        return doctorFirstName.get();
    }
    public String getDoctorLastName(){
        return doctorLastName.get();
    }
    public String getPatientFirstName(){
        return patientFirstName.get();
    }
    public String getPatientLastName(){
        return patientLastName.get();
    }
    public String getMedicalDate(){
        return medicalDate.get();
    }
    public String getNewMedicalDate(){ return newMedicalDate.get(); }

}
