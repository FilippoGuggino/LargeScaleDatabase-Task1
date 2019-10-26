package com.task1.clinic;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity(name = "patient")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class PatientEntity extends User{

    @OneToMany(mappedBy = "patient")
    private Set<MedicalEntity> medicals;

    public PatientEntity(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public PatientEntity() {
        super();
    }

    public Set<MedicalEntity> getMedicals() {
        return medicals;
    }

    public void setMedicals(Set<MedicalEntity> medicals) {
        this.medicals = medicals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientEntity)) return false;
        PatientEntity that = (PatientEntity) o;
        return getIdCode() == that.getIdCode() &&
                getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                Objects.equals(getMedicals(), that.getMedicals());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCode(), getFirstName(), getLastName(), getMedicals());
    }
}
