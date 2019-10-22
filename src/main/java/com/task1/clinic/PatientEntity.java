package com.task1.clinic;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity(name = "patient")
public class PatientEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idCode;

    @Column(name = "firstName")
    String firstName;

    @Column(name = "lastName")
    String lastName;

    @OneToMany(mappedBy = "patMedical")
    Set<MedicalEntity> patMedicals;

    public PatientEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PatientEntity() {

    }

    public int getIdCode() {
        return idCode;
    }

    public void setIdCode(int idCode) {
        this.idCode = idCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<MedicalEntity> getPatMedicals() {
        return patMedicals;
    }

    public void setPatMedicals(Set<MedicalEntity> patMedicals) {
        this.patMedicals = patMedicals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientEntity)) return false;
        PatientEntity that = (PatientEntity) o;
        return getIdCode() == that.getIdCode() &&
                getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                Objects.equals(getPatMedicals(), that.getPatMedicals());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCode(), getFirstName(), getLastName(), getPatMedicals());
    }
}
