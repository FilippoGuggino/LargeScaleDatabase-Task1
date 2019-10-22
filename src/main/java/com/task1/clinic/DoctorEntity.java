package com.task1.clinic;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "doctor")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class DoctorEntity {
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public int idCode;

        @Column(name = "firstName")
        public String firstName;

        @Column(name = "lastName")
        public String lastName;

        @OneToMany(mappedBy = "docMedical")
        Set<MedicalEntity> docMedicals;

        public DoctorEntity(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

    public DoctorEntity() {

    }

        public int getIdCode() {
            return idCode;
        }

        public void setIdCode(int idCode) {
            this.idCode = idCode;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
}
