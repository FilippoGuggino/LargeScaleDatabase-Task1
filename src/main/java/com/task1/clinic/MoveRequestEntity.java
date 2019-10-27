package com.task1.clinic;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(name = "move_request")
public class MoveRequestEntity implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "medicalFK", referencedColumnName = "idCode")
    public MedicalEntity medical;

    @Column(name = "newDate")
    @Temporal(TemporalType.DATE)
    public Date newDate;

    public MoveRequestEntity(MedicalEntity medical, Date newDate) {
        this.medical = medical;
        this.newDate = newDate;
    }

    public MoveRequestEntity() {

    }

    public MedicalEntity getMedical() {
        return medical;
    }

    public void setMedical(MedicalEntity medical) {
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
        if (!(o instanceof MoveRequestEntity)) return false;
        MoveRequestEntity that = (MoveRequestEntity) o;
        return Objects.equals(medical, that.medical) &&
                Objects.equals(newDate, that.newDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medical, newDate);
    }
}
