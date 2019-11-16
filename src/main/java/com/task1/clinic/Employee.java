package com.task1.clinic;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * An entity that represents an employee of the clinic.
 */
@Entity
@Table(name = "employee", uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class Employee extends User{

    /**
     * constructor which takes and initializes all attributes of the class using
     * the constructor of superclass User.
     * @param firstName first name of the doctor
     * @param lastName last name of the doctor
     */

    public Employee(String firstName, String lastName) {
        super(firstName, lastName);
    }

    /**
     * constructor which takes and initializes all attributes of the class using
     * the constructor of superclass User.
     */

    public Employee() {
        super();
    }

    /**
     * Add the specified object Medical to the database.
     * @param m object Medical to be added to the database
     * @return true if the object has been inserted correctly
     */

    public boolean addMedical(Medical m){
        m.setApproved(true);
        PersistenceManager man = PersistenceManager.getInstance();
        String checkQuery = "SELECT m FROM Medical m\n" +
                "WHERE m.doctor.idCode = :docId AND m.patient.idCode = :patId\n" +
                "AND m.date = :date";
        TypedQuery<Medical> prepQuery = man.readMedicals(checkQuery);
        prepQuery.setParameter("docId", m.getDoctor().getIdCode());
        prepQuery.setParameter("patId", m.getPatient().getIdCode());
        prepQuery.setParameter("date", m.getDate());
        List<Medical> res = prepQuery.getResultList();
        if(!res.isEmpty()) {
            return false;
        }
        man.create(m);
        Date today = new Date();
        Date mDate = m.getDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today_str = df.format(today);
        String mDate_str = df.format(mDate);
        if(mDate_str.compareTo(today_str)==0) {
            //if medical is scheduled today
            //TODO: test cache
            man.addToCache(m);
        }
        return true;
    }

    /**
     * Delete the specified object Medical from the database.
     * @param m object Medical to be dropped
     * @return true if the object has been correctly removed
     */

    public boolean dropMedical(Medical m){
        PersistenceManager man = PersistenceManager.getInstance();
        String checkQuery = "SELECT m FROM Medical m\n" +
                "WHERE m.doctor.idCode = :docId AND m.patient.idCode = :patId\n" +
                "AND m.date = :date";
        TypedQuery<Medical> prepQuery = man.readMedicals(checkQuery);
        prepQuery.setParameter("docId", m.getDoctor().getIdCode());
        prepQuery.setParameter("patId", m.getPatient().getIdCode());
        prepQuery.setParameter("date", m.getDate());
        List<Medical> res = prepQuery.getResultList();
        if(res.isEmpty()) {
            return false;
        }
        man.delete(m);
        Date today = new Date();
        Date mDate = m.getDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today_str = df.format(today);
        String mDate_str = df.format(mDate);
        if(mDate_str.compareTo(today_str)==0) {
            //if medical was scheduled today
            //TODO: test cache
            man.removeFromCache(m);
        }
        return true;
    }

    /**
     * Handle a Create request for a medical depending on the value of <code>approved</code>,
     * if it's true then the new medical is created, otherwise it's rejected.
     * @param med medical not approved yet
     * @param approved indicates if the request is approved (true) or rejected (false)
     * @return true if the request has been correctly handled
     */

    public boolean handleCreateRequest(Medical med, boolean approved){
        PersistenceManager man = PersistenceManager.getInstance();
        if(approved) {
            med.setApproved(true);
            man.update(med);
            Date today = new Date();
            Date mDate = med.getDate();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today_str = df.format(today);
            String mDate_str = df.format(mDate);
            if(mDate_str.compareTo(today_str)==0) {
                //if medical is scheduled today
                //TODO: test cache
                man.addToCache(med);
            }
            return true;
        }
        man.delete(med);
        return true;
    }

    /**
     * Handle a Delete Request for a medical depending on the value of <code>approved</code>,
     * if it's true then the medical is dropped, otherwise the request is rejected.
     * @param del delete request of the medical
     * @param approved indicates if the request is approved (true) or rejected (false)
     * @return true if the request has been correctly handled
     */

    public boolean handleDeleteRequest(DeleteRequest del, boolean approved){
        PersistenceManager man = PersistenceManager.getInstance();
        Medical tmp = del.getMedical();
        if(approved){
            man.delete(tmp);
            Date today = new Date();
            Date dDate = del.getMedical().getDate();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today_str = df.format(today);
            String dDate_str = df.format(dDate);
            //TODO: test cache
            if(dDate_str.compareTo(today_str) == 0) {
                //if medical was scheduled today
                man.removeFromCache(del.getMedical());
            }
        }
        else{
            man.delete(del);
            tmp.removeDelRequest();
        }
        return true;
    }

    /**
     * Handle a Move Request for a medical depending on the value of <code>approved</code>,
     * if it's true then the medical is moved, otherwise the request is rejected.
     * @param req move request of the medical
     * @param approved indicates if the request is approved (true) or rejected (false)
     * @return true if the request has been correctly handled
     */

    public boolean handleMoveRequest(MoveRequest req, boolean approved){
        PersistenceManager man = PersistenceManager.getInstance();
        Medical tmp = req.getMedical();
        if(approved) {
            tmp.setDate(req.getNewDate());
            man.update(tmp);
            Date today = new Date();
            Date reqDate = req.getMedical().getDate();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today_str = df.format(today);
            String reqDate_str = df.format(reqDate);
            //TODO: test cache
            if(reqDate_str.compareTo(today_str) == 0) {
                //if medical was scheduled today and it's been moved
                man.removeFromCache(req.getMedical());

            }
            //TODO: test cache
            Date req_newDate = req.getNewDate();
            String req_newDate_str = df.format(req_newDate);
            if(req_newDate_str.compareTo(today_str) == 0) {
                //if medical is moved to today
                man.addToCache(req.getMedical());
            }
        }
        man.delete(tmp.getMoveRequest());
        tmp.removeMoveRequest();
        return true;
    }

    /**
     * Retrieve the schedule of medicals for the specified patient, doctor and date.
     * If any of the parameters is set to null then it's not used to filter the result set.
     * @param patient object Patient that is involved in the medicals
     * @param doctor object Doctor that is involved in the medicals
     * @param byDate object Date used to filter the medicals
     * @return the list of medicals
     */

    public List<Medical> getSchedule(Patient patient, Doctor doctor, Date byDate){
        PersistenceManager man = PersistenceManager.getInstance();


        //TODO: test cache
        Date today = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today_str = df.format(today);
        String byDate_str = df.format(byDate);
        if(byDate_str.compareTo(today_str)==0) {
            return man.getTodayMedicals(doctor, patient);
        }



        boolean needAnd = false;
        String query = "SELECT m FROM Medical m WHERE";
        if(patient != null){
            query += " m.patient = :idPatient";
            needAnd = true;
        }
        if(doctor!=null){
            if(needAnd)
                query += " and";
            query += " m.doctor = :idDoctor";
            needAnd = true;
        }
        if(byDate != null){
            if(needAnd)
                query += " and";
            query += " m.date = :date";
        }
        query += " and";
        query += " m.approved = true";

        TypedQuery<Medical> preparedQuery = man.readMedicals(query);
        if(patient!=null)
            preparedQuery.setParameter("idPatient", patient);
        if(doctor!=null)
            preparedQuery.setParameter("idDoctor", doctor);
        if(byDate!=null)
            preparedQuery.setParameter("date", byDate);
        return  preparedQuery.getResultList();
    }

    /**
     * Retrieve the schedule of all medicals for the current date from the database.
     * @return the list of medicals
     */

    public List<Medical> getSchedule(){
        Date date = java.sql.Date.valueOf(LocalDate.now());
        return getSchedule(null, null,date);
    }

    /**
     * Check the credentials of an employee that wants to access to the app.
     * @param firstName the first name of the employee
     * @param lastName the last name of the employee
     * @return null if the specified credentials are invalid, an object Employee if they are correct
     */

    public static Employee logIn(String firstName, String lastName) {
        PersistenceManager man = PersistenceManager.getInstance();
        String logQuery = "SELECT e FROM Employee e WHERE e.firstName = :fn AND e.lastName = :ln";
        Query queryRes = man.read(logQuery);
        queryRes.setParameter("fn", firstName);
        queryRes.setParameter("ln", lastName);
        List result = queryRes.getResultList();
        if(result.isEmpty())
            return null;
        return (Employee) result.get(0);
    }

    /**
     * Retrieve all the Create Medical requests from the database.
     * @return the list of not yet approved medicals
     */

    public List<Medical> getCreateRequests() {
        PersistenceManager man = PersistenceManager.getInstance();
        String query = "SELECT m\n"
                + "FROM Medical m\n"
                + "WHERE m.approved = false\n"
                + "ORDER BY m.patient";
        TypedQuery<Medical> preparedQuery = man.readMedicals(query);
        return  preparedQuery.getResultList();
    }

    /**
     * Retrieve all Delete Requests from the database.
     * @return the list of delete requests
     */

    public List<DeleteRequest> getDeleteRequests() {
        PersistenceManager man = PersistenceManager.getInstance();
        String query = "SELECT dr\nFROM DeleteRequest dr";
        Query preparedQuery = man.read(query);
//        System.out.println(preparedQuery.getResultList().size());
        return  (List<DeleteRequest>) preparedQuery.getResultList();
    }

    /**
     * Retrieve all Move Requests from the database.
     * @return the list of move requests
     */

    public List<MoveRequest> getMoveRequests() {
        PersistenceManager man = PersistenceManager.getInstance();
        String query = "SELECT dr\nFROM MoveRequest dr";
        Query preparedQuery = man.read(query);
        return  (List<MoveRequest>) preparedQuery.getResultList();
    }


}