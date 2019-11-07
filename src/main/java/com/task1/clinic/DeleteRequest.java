package com.task1.clinic;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * An entity that represents a delete request made by a patient for a specific medical.
 */
@Entity
@Table(name = "delete_request")
public class DeleteRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
        medical.setDelRequest(this);
    }

    /**
     * default constructor
     */

    public DeleteRequest() {

    }

    /**
     * Get the medical attached to this request
     * @return the medical related with this request
     */

    public Medical getMedical() {
        return medical;
    }

    /**
     * Set the medical for the delete request
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
     * Transform the object DeleteRequest into an object MedicalBean
     * @return an object MedicalBean
     */

    public MedicalBean toBean(){ return new MedicalBean(medical.getDoctor(),medical.getPatient(),medical.getDate());}
}
