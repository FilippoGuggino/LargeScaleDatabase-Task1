package com.task1.clinic;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An entity that represents a doctor of the clinic.
 */
@Entity
@Table(name = "doctor", uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class Doctor extends User{

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Medical> medicals;
    /**
     * constructor which takes and initializes all attributes of the class using
     * the constructor of superclass User.
     * @param firstName first name of the doctor
     * @param lastName last name of the doctor
     */

    public Doctor(String firstName, String lastName) {
        super(firstName, lastName);
        medicals = new HashSet<>();
    }
    /**
     * default constructor which takes and initializes all attributes of the class using
     * the constructor of superclass User.
     */
    public Doctor() {
        super();
     }

    /**
     * Get all the medicals with this doctor.
     * @return The set of medicals.
     */

    public Set<Medical> getMedicals() {
        return medicals;
    }

    /**
     * Set the medicals that this doctor will hold.
     * @param medicals  the medicals involving the doctor
     */

    public void setMedicals(Set<Medical> medicals) {
        this.medicals = medicals;
    }

    /**
     * Get all the medicals of the specified date
     * where the doctor is involved.
     * @param byDate the date of the medicals that are requested to be shown
     * @return the list of requested medicals.
     */

    public List<Medical> getSchedule(Date byDate) {
        PersistenceManager man = PersistenceManager.getInstance();

        //check if results must be taken from cache
        if(isToday(byDate)) {
            return man.getTodayMedicals(this, null);
        }

        String query = "SELECT m\n" +
                "FROM Medical m\n" +
                "JOIN FETCH m.doctor JOIN FETCH m.patient\n" +
                "WHERE m.doctor.idCode = :idCode AND m.date = :byDate AND m.approved=true\n" +
                "ORDER BY m.date";
        TypedQuery<Medical> preparedQuery = man.prepareMedicalQuery(query);
        preparedQuery.setParameter("idCode", this.getIdCode());
        preparedQuery.setParameter("byDate", byDate);
        return man.executeTypedQuery(preparedQuery);
    }
    /**
     * Get all the medicals of the current date where the doctor is involved.
     * @return the list of requested medicals.
     */

    public List<Medical> getSchedule() {
        PersistenceManager man = PersistenceManager.getInstance();
        return man.getTodayMedicals(this, null);
    }

    public void addMedical(Medical med) {
        this.medicals.add(med);
        med.setDoctor(this);
    }

    public void removeMedical(Medical med) {
        this.medicals.remove(med);
        med.setDoctor(null);
    }

    /**
     * Check the credentials of an employee that wants to access to the app.
     * @param firstName the first name of the doctor
     * @param lastName the last name of the doctor
     * @return null if the specified credentials are invalid, an object Doctor if they are correct
     */

    public static Doctor logIn(String firstName, String lastName) {
        PersistenceManager man = PersistenceManager.getInstance();
        String logQuery = "SELECT d FROM Doctor d WHERE d.firstName = :fn AND d.lastName = :ln";
        Query queryRes = man.prepareQuery(logQuery);
        queryRes.setParameter("fn", firstName);
        queryRes.setParameter("ln", lastName);
        List result = man.executeQuery(queryRes);
        if(result.isEmpty())
            return null;
        return (Doctor) result.get(0);
    }

}
