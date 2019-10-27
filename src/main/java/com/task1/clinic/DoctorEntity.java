package com.task1.clinic;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "doctor", uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class DoctorEntity extends User{

    @OneToMany(mappedBy = "doctor")
    private Set<MedicalEntity> medicals;

    public DoctorEntity(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public DoctorEntity() {
        super();
     }

    public Set<MedicalEntity> getMedicals() {
        return medicals;
    }

    public void setMedicals(Set<MedicalEntity> medicals) {
        this.medicals = medicals;
    }

}
