package com.task1.clinic;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "move_request")
public class MoveRequestEntity implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "fk_medical", referencedColumnName = "id")
    MedicalEntity medical;

    @Column(name = "new_date")
    String newDate;

    public MoveRequestEntity(MedicalEntity medical, String newDate) {
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

    public String getNewDate() {
        return newDate;
    }

    public void setNewDate(String newDate) {
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
