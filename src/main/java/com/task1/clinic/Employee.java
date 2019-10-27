package com.task1.clinic;

import javax.persistence.*;

@Entity
@Table(name = "employee", uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "lastName"})})
public class Employee extends User{

    public Employee(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Employee() {
        super();
    }
}
