package com.task1.clinic;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "medical")
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

    @Column(name = "medical_date")
    private String date;

    @Column(name = "approved")
    private boolean approved;

    public MedicalEntity(DoctorEntity doctor, PatientEntity patient, String date) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
