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
    private int id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "medicalFK", referencedColumnName = "idCode")
    private Medical medical;

    /**
     * constructor which creates a new delete request for the specified medical
     * @param medical the medical to be deleted
     */

    public DeleteRequest(Medical medical) {
        this.medical = medical;
    }

    /**
     * default constructor
     */

    public DeleteRequest() {

    }

    /**
     * function that return the medical
     * @return an object Medical
     */

    public Medical getMedical() {
        return medical;
    }

    /**
     * function that set a new medical for the delete request
     * @param medical the medical request to be attached to the delete request
     */

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


    /**
     * function that transform the object DeleteRequest to an object MedicalBean
     * @return an object MedicalBean
     */

    public MedicalBean toBean(){ return new MedicalBean(medical.getDoctor(),medical.getPatient(),medical.getDate());}
}
