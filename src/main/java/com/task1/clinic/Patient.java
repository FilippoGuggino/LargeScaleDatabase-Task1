package com.task1.clinic;

import javax.persistence.*;
import java.util.*;

/**
 * An entity that represents a patient of the clinic.
 */
@Entity
@Table(name = "patient", uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class Patient extends User{

    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL, orphanRemoval = true)
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
     * Get all the medicals where the patient is involved.
     * @return the list of requested medicals
     */

    public List<Medical>  getSchedule() {
        PersistenceManager man = PersistenceManager.getInstance();
        String query = "SELECT m\n" +
                       "FROM Medical m\n" +
                       "JOIN FETCH m.doctor JOIN FETCH m.patient\n" +
                       "WHERE m.patient.idCode = :idCode\n" +
                       "ORDER BY m.date DESC";
        TypedQuery<Medical> preparedQuery = man.prepareMedicalQuery(query);
        preparedQuery.setParameter("idCode", this.getIdCode());
        List<Medical> result = man.executeTypedQuery(preparedQuery);
        return result;
    }

    /**
     * Create a new medical request with the specified doctor on the specified date
     * @param doctor the requested doctor for the medical
     * @param date the requested date of the medical
     * @return the Medical object if everything worked fine, null if an error occurred.
     */

    public Medical createMedicalRequest(Doctor doctor, Date date) {
        PersistenceManager man = PersistenceManager.getInstance();
        Medical newMed = new Medical();
        String checkQuery = "SELECT m FROM Medical m\n" +
                            "WHERE m.doctor.idCode = :docId AND m.patient.idCode = :patId\n" +
                            "AND m.date = :date";
        TypedQuery<Medical> prepQuery = man.prepareMedicalQuery(checkQuery);
        prepQuery.setParameter("docId", doctor.idCode);
        prepQuery.setParameter("patId", this.idCode);
        prepQuery.setParameter("date", date);
        List<Medical> res = man.executeTypedQuery(prepQuery);
        if(!res.isEmpty()) {
            //medical already existed -> return null
            return null;
        }
        newMed.setDate(date);
        doctor.addMedical(newMed);
        this.addMedical(newMed);
        man.create(newMed);
        return newMed;
    }

    /**
     * Create a new delete request for the specified medical.
     * @param med the medical to be deleted
     * @return an object DeleteRequest
     */

    public DeleteRequest deleteRequest(Medical med) {
        PersistenceManager man = PersistenceManager.getInstance();
        String checkQuery = "SELECT dr\n" +
                "FROM DeleteRequest dr\n" +
                "WHERE dr.medical.idCode = :idCode";
        Query prepQuery = man.prepareQuery(checkQuery);
        prepQuery.setParameter("idCode", med.getIdCode());
        List<DeleteRequest> res = (List<DeleteRequest>)man.executeQuery(prepQuery);
        if(!res.isEmpty()) {
            //deleteRequest already existed -> return null
            return null;
        }
        med = med.connect();
        DeleteRequest del = new DeleteRequest();
        med.setDelRequest(del);
        man.update(med);
        return del;
    }

    /**
     * Create a new move request for the specified medical.
     * @param med the medical to be moved
     * @param newDate the date to which the medical has to be moved
     * @return the MoveRequest if everything worked fine, null if an error occurred.
     */

    public MoveRequest moveRequest(Medical med, Date newDate) {
        PersistenceManager man = PersistenceManager.getInstance();
        String checkQuery = "SELECT mr\n" +
                "FROM MoveRequest mr\n" +
                "WHERE mr.medical.idCode = :idCode";
        Query prepQuery = man.prepareQuery(checkQuery);
        prepQuery.setParameter("idCode", med.getIdCode());
        List<MoveRequest> res = (List<MoveRequest>)man.executeQuery(prepQuery);
        if(!res.isEmpty()) {
            //moveRequest already existed -> return null
            return null;
        }
        MoveRequest mov = new MoveRequest();
        med = med.connect();
        mov.setNewDate(newDate);
        med.setMoveRequest(mov);
        man.create(mov);
        return mov;
    }

    public void addMedical(Medical med) {
        this.medicals.add(med);
        med.setPatient(this);
    }

    public void removeMedical(Medical med) {
        this.medicals.remove(med);
        med.setPatient(null);
    }

    /**
     * Check the credentials of a patient that wants to access to the app
     * @param firstName the first name of the patient
     * @param lastName the last name of the patient
     * @return null if the specified credentials are invalid, an object Patient if they are correct
     */

    public static Patient logIn(String firstName, String lastName) {
        PersistenceManager man = PersistenceManager.getInstance();
        String logQuery = "SELECT p FROM Patient p WHERE p.firstName = :fn AND p.lastName = :ln";
        Query queryRes = man.prepareQuery(logQuery);
        queryRes.setParameter("fn", firstName);
        queryRes.setParameter("ln", lastName);
        List result = man.executeQuery(queryRes);
        if(result.isEmpty())
            return null;
        return (Patient) result.get(0);
    }

}
