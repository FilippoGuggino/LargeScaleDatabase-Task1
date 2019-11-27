package com.task1.clinic;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

/**
 * An entity that represents a medical.
 */
@Entity
@Table(name = "medical", uniqueConstraints = {@UniqueConstraint(columnNames = {"doctorFK", "patientFK", "medicalDate"})})
public class Medical {
    @Id
    @Column(name = "idCode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctorFK", referencedColumnName = "idCode")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patientFK", referencedColumnName = "idCode")
    private Patient patient;

    @Column(name = "medicalDate")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "approved")
    private boolean approved;

    @OneToOne(mappedBy = "medical", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DeleteRequest delRequest;

    @OneToOne(mappedBy = "medical", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MoveRequest moveRequest;

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
     * Get the idCode of the medical
     * @return integer idCode
     */

    public int getIdCode() {
        return idCode;
    }

    /**
     * Set the idCode of the medical through the specified one
     * @param idCode idCode to be set to the user
     */

    public void setIdCode(int idCode) {
        this.idCode = idCode;
    }

    /**
     * Get the doctor of the medical
     * @return the doctor of the medical
     */

    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Get the move request for the medical.
     * @return the move request if it's present, otherwise returns <code>null</code>.
     */

    public MoveRequest getMoveRequest() {
        return moveRequest;
    }

    /**
     * Set the delete request for the medical.
     * @param moveRequest the delete request to be set
     */

    public void setMoveRequest(MoveRequest moveRequest) {
        if(moveRequest == null) {
            if(this.moveRequest != null)
                this.moveRequest.setMedical(null);
        }
        else {
            moveRequest.setMedical(this);
        }
        this.moveRequest = moveRequest;
    }

    /**
     * Get the delete request for the medical.
     * @return the delete request if it's present, otherwise returns <code>null</code>.
     */

    public DeleteRequest getDelRequest() {
        return delRequest;
    }

    /**
     * Set the delete request for the medical.
     * @param delRequest the delete request to be set
     */

    public void setDelRequest(DeleteRequest delRequest) {
        if(delRequest == null) {
            if(this.delRequest != null)
                this.delRequest.setMedical(null);
        }
        else {
            delRequest.setMedical(this);
        }
        this.delRequest = delRequest;
    }

    /**
     * Set the doctor of the medical.
     * @param doctor the doctor to be set for the medical
     */

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * Get the patient of the medical.
     * @return the patient of the medical
     */

    public Patient getPatient() {
        return patient;
    }

    /**
     * Set the patient of the medical.
     * @param patient the patient to be set for the medical
     */

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * Get the date of the medical
     * @return the date of the medical
     */

    public Date getDate() {
        return date;
    }

    /**
     * Set the date of the medical
     * @param date the date to be set for the medical
     */

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get the approved status of the medical.
     * @return the boolean status of the medical.
     */

    public boolean isApproved() { return approved; }

    /**
     * Set the approved status of the medical.
     * @param approved the status to be set for the medical
     */

    public void setApproved(boolean approved) { this.approved = approved; }

    @Override
    public String toString() {
        return "Doctor: " + doctor.getIdCode() + ", Patient: " + patient.getIdCode() +
                ", Date: " + date.toString();
    }

    /**
     * Transform the object Medical into an object MedicalBean.
     * @return an object MedicalBean
     */

    public MedicalBean toBean(){
        return new MedicalBean(getDoctor(),getPatient(),getDate(),isApproved());
    }


    /**
     * Attach a medical to the database by returning a new attached instance eagerly fetched.
     * It's necessary especially because today medicals extracted from cache are not attached
     * to the relational database in any way.
     * @return the attached instance of Medical
     */
    public Medical connect() {
        PersistenceManager man = PersistenceManager.getInstance();
        EntityManager em = man.getEm();
        /*
        The find method is necessary instead of getReference because it
        always returns the entity with attributes eagerly fetched.
         */
        Medical med = em.find(Medical.class, this.idCode);
        em.close();
        return med;
    }

}
