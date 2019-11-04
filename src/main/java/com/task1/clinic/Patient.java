package com.task1.clinic;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "patient", uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class Patient extends User{

    @OneToMany(mappedBy = "patient")
    private Set<Medical> medicals;

    /**
     * constructor which takes and initializes all attributes of the class using
     * the constructor of superclass User.
     * @param firstName first name of the doctor
     * @param lastName last name of the doctor
     */

    public Patient(String firstName, String lastName) {
        super(firstName, lastName);
        medicals = new HashSet<Medical>();
    }

    /**
     * default constructor
     */

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

    /**
     * functions that returns all the medicals of the specified date
     * where the patient is involved
     * @returns a list of object Medical
     */

    public List<Medical>  getSchedule() {
        PersistenceManager man = PersistenceManager.getInstance();
        String query = "SELECT m\n" +
                       "FROM Medical m\n" +
                       "WHERE m.patient.idCode = :idCode\n" +
                       "AND m.approved = true\n" +
                       "ORDER BY m.date DESC";
        TypedQuery<Medical> preparedQuery = man.readMedicals(query);
        preparedQuery.setParameter("idCode", this.getIdCode());
        List<Medical> result = preparedQuery.getResultList();
        return result;
    }

    /**
     * Create a new medical request with the specified doctor on the specified date
     * @param doctor the requested doctor for the medical
     * @param date the requested date of the medical
     * @returns the Medical object if everything worked fine, null if an error occurred.
     */

    public Medical createMedicalRequest(Doctor doctor, Date date) {
        PersistenceManager man = PersistenceManager.getInstance();
        Medical newMed = new Medical(doctor, this, date);
        String checkQuery = "SELECT m FROM Medical m\n" +
                            "WHERE m.doctor.idCode = :docId AND m.patient.idCode = :patId\n" +
                            "AND m.date = :date";
        TypedQuery<Medical> prepQuery = man.readMedicals(checkQuery);
        prepQuery.setParameter("docId", doctor.idCode);
        prepQuery.setParameter("patId", this.idCode);
        prepQuery.setParameter("date", date);
        List<Medical> res = prepQuery.getResultList();
        if(!res.isEmpty()) {
            //medical already existed -> return null
            return null;
        }
        man.create(newMed);
        return newMed;
    }

    /**
     * Create a new delete request for the specified medical
     * @param med the medical request that wants to be deleted
     * @returns an object DeleteRequest
     */

    public DeleteRequest deleteRequest(Medical med) {
        PersistenceManager man = PersistenceManager.getInstance();
        DeleteRequest del = new DeleteRequest(med);
        man.create(del);
        return del;
    }

    /**
     * functions that create a new move request for the specified medical
     * @param med the medical request that wants to be moved
     * @param newDate the date to which the medical request wants to be moved
     * @returns the MoveRequest if everything worked fine, null if an error occurred.
     */

    public MoveRequest moveRequest(Medical med, Date newDate) {
        PersistenceManager man = PersistenceManager.getInstance();
        MoveRequest mov = new MoveRequest(med, newDate);
        String checkQuery = "SELECT m FROM Medical m\n" +
                "WHERE m.doctor.idCode = :docId AND m.patient.idCode = :patId\n" +
                "AND m.date = :date";
        TypedQuery<Medical> prepQuery = man.readMedicals(checkQuery);
        prepQuery.setParameter("docId", med.getDoctor().idCode);
        prepQuery.setParameter("patId", this.idCode);
        prepQuery.setParameter("date", newDate);
        List<Medical> res = prepQuery.getResultList();
        if(!res.isEmpty()) {
            //medical already existed -> return null
            return null;
        }
        man.create(mov);
        return mov;
    }

    /**
     * functions that checks the credentials of a patient that wants to access to the app
     * @param firstName the first name of the patient
     * @param lastName the last name of the patient
     * @returns null if the specified credentials are invalid, an object Patient if they are correct
     */

    public static Patient logIn(String firstName, String lastName) {
        PersistenceManager man = PersistenceManager.getInstance();
        String logQuery = "SELECT p FROM Patient p WHERE p.firstName = :fn AND p.lastName = :ln";
        Query queryRes = man.read(logQuery);
        queryRes.setParameter("fn", firstName);
        queryRes.setParameter("ln", lastName);
        List result = queryRes.getResultList();
        if(result.isEmpty())
            return null;
        return (Patient) result.get(0);
    }

}
