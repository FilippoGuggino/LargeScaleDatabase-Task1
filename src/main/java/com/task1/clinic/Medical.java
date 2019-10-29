package com.task1.clinic;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "medical")
public class Medical {
    @Id
    @Column(name = "idCode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCode;

    @ManyToOne
    @JoinColumn(name = "doctorFK")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patientFK")
    private Patient patient;

    @Column(name = "medicalDate")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "approved")
    private boolean approved;

    public Medical(Doctor doctor, Patient patient, Date date) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.approved = false;
    }

    public Medical() {

    }

    public int getIdCode() {
        return idCode;
    }

    public void setIdCode(int idCode) {
        this.idCode = idCode;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isApproved() { return approved; }

    public void setApproved(boolean approved) { this.approved = approved; }

    @Override
    public String toString() {
        return "Doctor: " + doctor.getIdCode() + ", Patient: " + patient.getIdCode() +
                ", Date: " + date.toString();
    }

    public MedicalBean toBean(){
        return new MedicalBean(doctor,patient,date);
    }
}
