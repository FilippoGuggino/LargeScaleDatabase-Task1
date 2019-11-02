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
    /**
     * constructor which takes and initializes all attributes of the class using
     * the constructor of superclass User.
     * @param firstName first name of the doctor
     * @param lastName last name of the doctor
     */

    public Doctor(String firstName, String lastName) {
        super(firstName, lastName);
    }
    /**
     * default constructor which takes and initializes all attributes of the class using
     * the constructor of superclass User.
     */
    public Doctor() {
        super();
     }

    /**
     * function that retrieve all the medical
     * @returns a set of Medical
     */

    public Set<Medical> getMedicals() {
        return medicals;
    }

    /**
     * function that set the medicals
     */

    public void setMedicals(Set<Medical> medicals) {
        this.medicals = medicals;
    }

    /**
     * functions that returns all the medicals of the specified date
     * where the doctor is involved
     * @param byDate the date of the medicals that are requested to be shown
     * @returns a list of object Medical
     */

    public List<Medical> getSchedule(Date byDate) {
        System.out.println(byDate.toString());
        PersistenceManager Man = PersistenceManager.getInstance();
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
    /**
     * functions that returns all the medicals of the actual date where the doctor is involved
     * @returns a list of object Medical
     */

    public List<Medical> getSchedule() {
        return getSchedule(new Date());
    }

    /**
     * functions that checks the credentials of a doctor that wants to access to the app
     * @param firstName the first name of the doctor
     * @param lastName the last name of the doctor
     * @returns null if the specified credentials are invalid, an object Doctor if they are correct
     */

    public static Doctor logIn(String firstName, String lastName) {
        PersistenceManager man = PersistenceManager.getInstance();
        String logQuery = "SELECT d FROM Doctor d WHERE d.firstName = :fn AND d.lastName = :ln";
        Query queryRes = man.read(logQuery);
        queryRes.setParameter("fn", firstName);
        queryRes.setParameter("ln", lastName);
        List result = queryRes.getResultList();
        if(result.isEmpty())
            return null;
        return (Doctor) result.get(0);
    }

}
