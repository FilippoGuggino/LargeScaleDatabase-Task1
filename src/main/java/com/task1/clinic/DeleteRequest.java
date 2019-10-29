package com.task1.clinic;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "delete_request")
public class DeleteRequest implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "medicalFK", referencedColumnName = "idCode")
    private Medical medical;

    public DeleteRequest(Medical medical) {
        this.medical = medical;
    }

    public DeleteRequest() {

    }

    public Medical getMedical() {
        return medical;
    }

    public void setMedical(Medical medical) {
        this.medical = medical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteRequest)) return false;
        DeleteRequest that = (DeleteRequest) o;
        return Objects.equals(medical, that.medical);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medical);
    }
}
