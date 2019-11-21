package com.task1.clinic;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * An entity that represents a move request made by a patient for a specific medical.
 */
@Entity
@Table(name = "move_request")
public class MoveRequest implements Serializable {
    @Id
    private int id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Medical medical;

    @Column(name = "newDate")
    @Temporal(TemporalType.DATE)
    public Date newDate;

    /**
     * constructor which creates a new move request for the specified medical
     * @param medical the medical to be moved
     * @param newDate the new date for the medical
     */

    public MoveRequest(Medical medical, Date newDate) {
        this.newDate = newDate;
        this.setMedical(medical);
    }

    /**
     * default constructor
     */

    public MoveRequest() {

    }

    /**
     * Get the medical associated to this request.
     * @return the medical associated to this request
     */

    public Medical getMedical() {
        return medical;
    }

    /**
     * Set a medical for the move request.
     * @param medical the medical request to be attached to the move request
     */

    public void setMedical(Medical medical) {
        this.medical = medical;
    }

    /**
     * Get the new date of the move request for the medical
     * @return the date the patient wants to move the medical to
     */

    public Date getNewDate() {
        return newDate;
    }

    /**
     * Set the new date of the move request for the medical.
     * @param  newDate the date the patient wants to move the medical to
     */

    public void setNewDate(Date newDate) {
        this.newDate = newDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveRequest)) return false;
        MoveRequest that = (MoveRequest) o;
        return Objects.equals(medical, that.medical) &&
                Objects.equals(newDate, that.newDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medical, newDate);
    }

    /**
     * Get the id code of the request.
     * @return the id code of the request
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id code of the request.
     * @param id the id code of the request
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Transform the object MoveRequest into an object MedicalBean
     * @return an object MedicalBean
     */


    public MedicalBean toBean() {return new MedicalBean(medical.getDoctor(),medical.getPatient(),medical.getDate(),newDate);}
}
