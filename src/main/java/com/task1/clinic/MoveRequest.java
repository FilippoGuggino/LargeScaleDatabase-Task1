package com.task1.clinic;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "move_request")
public class MoveRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @MapsId
    @OneToOne( cascade = CascadeType.MERGE)
    @JoinColumn(name = "medicalFK", referencedColumnName = "idCode")
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
        medical.setMoveRequest(this);
    }

    /**
     * default constructor
     */

    public MoveRequest() {

    }

    /**
     * function that return the medical
     * @return an object Medical
     */

    public Medical getMedical() {
        return medical;
    }

    /**
     * function that set a new medical for the move request
     * @param medical the medical request to be attached to the move request
     */

    public void setMedical(Medical medical) {
        this.medical = medical;
    }

    /**
     * function that return the new date of the move request for the medical
     * @return an object Date
     */

    public Date getNewDate() {
        return newDate;
    }

    /**
     * function that return the delete request for the medical
     * @param  newDate the date to which the medical is requested to be moved
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * function that transform the object MoveRequest to an object MedicalBean
     * @return an object MedicalBean
     */


    public MedicalBean toBean() {return new MedicalBean(medical.getDoctor(),medical.getPatient(),medical.getDate(),newDate);}
}
