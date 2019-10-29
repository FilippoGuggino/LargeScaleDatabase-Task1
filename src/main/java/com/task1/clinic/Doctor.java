package com.task1.clinic;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
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

    public List<Medical> getSchedule(Date byDate) {
        Manager Man = Manager.getInstance();
        String query = "SELECT m\n" +
                "FROM Medical m\n" +
                "WHERE m.doctor.idCode = :idCode AND m.date = :byDate \n" +
                "ORDER BY m.date";
        TypedQuery<Medical> preparedQuery = Man.readMedicals(query);
        preparedQuery.setParameter("idCode", this.getIdCode());
        preparedQuery.setParameter("byDate", byDate);
        List<Medical> result = preparedQuery.getResultList();
        return result;
    }

}