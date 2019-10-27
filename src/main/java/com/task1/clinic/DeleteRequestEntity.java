package com.task1.clinic;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "delete_request")
public class DeleteRequestEntity implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "medicalFK", referencedColumnName = "idCode")
    private MedicalEntity medical;

    public DeleteRequestEntity(MedicalEntity medical) {
        this.medical = medical;
    }

    public DeleteRequestEntity() {

    }

    public MedicalEntity getMedical() {
        return medical;
    }

    public void setMedical(MedicalEntity medical) {
        this.medical = medical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteRequestEntity)) return false;
        DeleteRequestEntity that = (DeleteRequestEntity) o;
        return Objects.equals(medical, that.medical);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medical);
    }
}
