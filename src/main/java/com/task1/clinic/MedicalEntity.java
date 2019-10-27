package com.task1.clinic;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "medical")
public class MedicalEntity {
    @Id
    @Column(name = "idCode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCode;

    @ManyToOne
    @JoinColumn(name = "doctorFK")
    private DoctorEntity doctor;

    @ManyToOne
    @JoinColumn(name = "patientFK")
    private PatientEntity patient;

    @Column(name = "medicalDate")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "approved")
    private boolean approved;

    public MedicalEntity(DoctorEntity doctor, PatientEntity patient, Date date) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.approved = false;
    }

    public MedicalEntity() {

    }

    public int getIdCode() {
        return idCode;
    }

    public void setIdCode(int idCode) {
        this.idCode = idCode;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
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

}
