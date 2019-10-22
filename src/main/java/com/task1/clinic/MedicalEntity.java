package com.task1.clinic;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "medical")
public class MedicalEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int idCode;

    @ManyToOne
    @JoinColumn(name = "doc_id")
    DoctorEntity docMedical;

    @ManyToOne
    @JoinColumn(name = "pat_id")
    PatientEntity patMedical;

    @Column(name = "medical_date")
    String date;

    @Column(name = "approved")
    boolean approved;

    public MedicalEntity(DoctorEntity docMedical, PatientEntity patMedical, String date) {
        this.docMedical = docMedical;
        this.patMedical = patMedical;
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

    public DoctorEntity getDocMedical() {
        return docMedical;
    }

    public void setDocMedical(DoctorEntity docMedical) {
        this.docMedical = docMedical;
    }

    public PatientEntity getPatMedical() {
        return patMedical;
    }

    public void setPatMedical(PatientEntity patMedical) {
        this.patMedical = patMedical;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
