package com.task1.clinic;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "doctor", uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class Doctor extends User{

    @OneToMany(mappedBy = "doctor")
    private Set<Medical> medicals;

    public Doctor(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Doctor() {
        super();
     }

    public Set<Medical> getMedicals() {
        return medicals;
    }

    public void setMedicals(Set<Medical> medicals) {
        this.medicals = medicals;
    }

}
