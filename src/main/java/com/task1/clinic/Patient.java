package com.task1.clinic;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "patient", uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class Patient extends User{

    @OneToMany(mappedBy = "patient")
    private Set<Medical> medicals;

    public Patient(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Patient() {
        super();
    }

    public Set<Medical> getMedicals() {
        return medicals;
    }

    public void setMedicals(Set<Medical> medicals) {
        this.medicals = medicals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient that = (Patient) o;
        return getIdCode() == that.getIdCode() &&
                getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                Objects.equals(getMedicals(), that.getMedicals());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCode(), getFirstName(), getLastName(), getMedicals());
    }

    public List<Medical>  getSchedule() {
        Manager man = Manager.getInstance();
        String query = "SELECT m\n" +
                       "FROM Medical m\n" +
                       "WHERE m.patient.idCode = :idCode\n" +
                       "ORDER BY m.date";
        TypedQuery<Medical> preparedQuery = man.readMedicals(query);
        preparedQuery.setParameter("idCode", this.getIdCode());
        List<Medical> result = preparedQuery.getResultList();
        return result;
    }

    public Medical createMedicalRequest(Doctor doctor, Date date) {
        Manager man = Manager.getInstance();
        Medical newMed = new Medical(doctor, this, date);
        man.create(newMed);
        return newMed;
    }

    public DeleteRequest deleteRequest(Medical med) {
        Manager man = Manager.getInstance();
        DeleteRequest del = new DeleteRequest(med);
        man.create(del);
        return del;
    }

    public MoveRequest moveRequest(Medical med, Date newDate) {
        Manager man = Manager.getInstance();
        MoveRequest mov = new MoveRequest(med, newDate);
        man.create(mov);
        return mov;
    }


}
