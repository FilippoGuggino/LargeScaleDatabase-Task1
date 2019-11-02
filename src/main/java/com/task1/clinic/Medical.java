package com.task1.clinic;

import javafx.beans.property.SimpleStringProperty;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(name = "doctorFK", referencedColumnName = "idCode")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patientFK", referencedColumnName = "idCode")
    private Patient patient;

    @Column(name = "medicalDate")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "approved")
    private boolean approved;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(mappedBy = "medical")
    private DeleteRequest delRequest;

    /**
     * constructor which creates a new instance of a medical request for the specified patient
     * with a specified doctor on a specified date
     * @param doctor doctor that is involved in the medical
     * @param patient patient that is involved in the medical
     * @param date date of the medical
     */

    public Medical(Doctor doctor, Patient patient, Date date) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.approved = false;
    }

    public Medical() {

    }

    /**
     * function that gets the idCode of the medical
     * @return integer idCode
     */

    public int getIdCode() {
        return idCode;
    }

    /**
     * function that set the idCode of the medical through the specified one
     * @param idCode idCode to be set to the user
     */

    public void setIdCode(int idCode) {
        this.idCode = idCode;
    }

    /**
     * function that return the doctor of the medical
     * @return an object Doctor
     */

    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * function that return the delete request for the medical
     * @return an object DeleteRequest
     */

    public DeleteRequest getDelRequest() {
        return delRequest;
    }

    /**
     * function that set the delete request for the medical
     * @param delRequest the delete request to be set
     */

    public void setDelRequest(DeleteRequest delRequest) {
        this.delRequest = delRequest;
    }

    /**
     * function that set the doctor of the medical
     * @param doctor the doctor to be set for the medical
     */

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * function that return the delete patient of the medical
     * @return an object Patient
     */

    public Patient getPatient() {
        return patient;
    }

    /**
     * function that set the patient of the medical
     * @param patient the patient to be set for the medical
     */

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * function that return the date of the medical
     * @return an object Date
     */

    public Date getDate() {
        return date;
    }

    /**
     * function that set the date of the medical
     * @param date the date to be set for the medical
     */

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * function that return the approved status of the medical
     * @return a boolean status of the medical
     */

    public boolean isApproved() { return approved; }

    /**
     * function that set the approved status of the medical
     * @param approved the status to be set for the medical
     */

    public void setApproved(boolean approved) { this.approved = approved; }

    @Override
    public String toString() {
        return "Doctor: " + doctor.getIdCode() + ", Patient: " + patient.getIdCode() +
                ", Date: " + date.toString();
    }

    /**
     * function that transform the object Medical to an object MedicalBean
     * @return an object MedicalBean
     */

    public MedicalBean toBean(){
        return new MedicalBean(getDoctor(),getPatient(),getDate());
    }
}
