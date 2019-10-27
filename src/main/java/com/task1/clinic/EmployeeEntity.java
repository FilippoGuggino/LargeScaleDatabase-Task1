package com.task1.clinic;

import javax.persistence.*;

@Entity(name = "employee")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class EmployeeEntity extends User{

    public EmployeeEntity(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public EmployeeEntity() {
        super();
    }
}
