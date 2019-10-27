package com.task1.clinic;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity(name = "patient")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class PatientEntity extends User{

    @OneToMany(mappedBy = "patient")
    private Set<MedicalEntity> medicals;

    public PatientEntity(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public PatientEntity() {
        super();
    }

    public Set<MedicalEntity> getMedicals() {
        return medicals;
    }

    public void setMedicals(Set<MedicalEntity> medicals) {
        this.medicals = medicals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientEntity)) return false;
        PatientEntity that = (PatientEntity) o;
        return getIdCode() == that.getIdCode() &&
                getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                Objects.equals(getMedicals(), that.getMedicals());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCode(), getFirstName(), getLastName(), getMedicals());
    }

    public List<MedicalEntity>  getSchedule() {
        Manager man = Manager.getInstance();
        String query = "SELECT m\n" +
                       "FROM MedicalEntity m\n" +
                       "WHERE m.patient.idCode = :idCode" +
                       "ORDER BY m.date";
        TypedQuery<MedicalEntity> preparedQuery = man.readMedicals(query);
        preparedQuery.setParameter("idCode", this.getIdCode());
        List<MedicalEntity> result = preparedQuery.getResultList();
        return result;
    }

    public MedicalEntity createMedicalRequest(DoctorEntity doctor, Date date) {
        Manager man = Manager.getInstance();
        MedicalEntity newMed = new MedicalEntity(doctor, this, date);
        man.create(newMed);
        return newMed;
    }

    public DeleteRequestEntity deleteRequest(MedicalEntity med) {
        Manager man = Manager.getInstance();
        DeleteRequestEntity del = new DeleteRequestEntity(med);
        man.create(del);
        return del;
    }

    public MoveRequestEntity moveRequest(MedicalEntity med, Date newDate) {
        Manager man = Manager.getInstance();
        MoveRequestEntity mov = new MoveRequestEntity(med, newDate);
        man.create(mov);
        return mov;
    }


}
