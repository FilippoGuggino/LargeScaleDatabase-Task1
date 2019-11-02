package com.task1.clinic;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "move_request")
public class MoveRequest implements Serializable {
    @Id
    int id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "medicalFK", referencedColumnName = "idCode")
    public Medical medical;

    @Column(name = "newDate")
    @Temporal(TemporalType.DATE)
    public Date newDate;

    public MoveRequest(Medical medical, Date newDate) {
        this.medical = medical;
        this.newDate = newDate;
    }

    public MoveRequest() {

    }

    public Medical getMedical() {
        return medical;
    }

    public void setMedical(Medical medical) {
        this.medical = medical;
    }

    public Date getNewDate() {
        return newDate;
    }

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

    public MedicalBean toBean() {return new MedicalBean(medical.getDoctor(),medical.getPatient(),medical.getDate(),newDate);}
}
