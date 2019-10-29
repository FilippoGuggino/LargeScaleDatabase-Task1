package com.task1.clinic;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employee", uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class Employee extends User{

    public Employee(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Employee() {
        super();
    }

    public boolean addMedical(Medical m){
        Manager man = Manager.getInstance();
        man.create(m);
        return true;
    }

    public boolean dropMedical(Medical m){
        Manager man = Manager.getInstance();
        man.delete(m);
        return true;
    }

    public boolean handleCreateRequest(Medical med, boolean approved){
        Manager man = Manager.getInstance();
        med.setApproved(approved);
        man.update(med);
        return true;
    }

    public boolean handleDeleteRequest(DeleteRequest del, boolean approved){
        Manager man = Manager.getInstance();
        if(approved){
            String query = "SELECT d.medical\n" +
                    "FROM DeleteRequest d\n" +
                    "INNER JOIN d.medical\n";
            TypedQuery<Medical> preparedQuery = man.readMedicals(query);
            List<Medical> result = preparedQuery.getResultList();
            man.delete(result.get(0));
        }
        else{
            man.delete(del);
        }
        return true;
    }

    public boolean handleMoveRequest(MoveRequest req, boolean approved){
        Manager man = Manager.getInstance();
        if(approved) {
            String query = "SELECT d.medical\n" +
                    "FROM DeleteRequest d\n" +
                    "INNER JOIN d.medical\n";
            TypedQuery<Medical> preparedQuery = man.readMedicals(query);
            List<Medical> result = preparedQuery.getResultList();
            Medical tmp = result.get(0);
            tmp.setDate(req.getNewDate());
            man.update(tmp);
        }
        man.delete(req);
        return true;
    }

    public List<Medical> getSchedule(Patient patient, Doctor doctor, Date byDate){
        Manager man = Manager.getInstance();
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
        }
        if(byDate != null){
            if(needAnd)
                query += " and";
            query += " and m.date = :date";
        }
        TypedQuery<Medical> preparedQuery = man.readMedicals(query);
        if(patient!=null)
            preparedQuery.setParameter("idPatient", patient);
        if(doctor!=null)
            preparedQuery.setParameter("idDoctor", doctor);
        if(byDate!=null)
            preparedQuery.setParameter("date", byDate);
        return  preparedQuery.getResultList();
    }
}
