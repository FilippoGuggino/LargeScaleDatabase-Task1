package com.task1.clinic;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
public abstract class User {
    @Id
    @Column(name = "idCode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int idCode;

    @Column(name = "firstName")
    protected String firstName;

    @Column(name = "lastName")
    protected String lastName;

    /**
     * constructor which takes and initializes all attributes of the class
     * @param firstName first name of the user
     * @param lastName last name of the user
     */

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * default constructor
     */

    public User() {

    }

    /**
     * function that gets the idCode of the user
     * @return integer idCode
     */

    public int getIdCode() {
        return idCode;
    }

    /**
     * function that set the idCode of the user through the specified one
     * @param idCode idCode to be set to the user
     */

    public void setIdCode(int idCode) {
        this.idCode = idCode;
    }

    /**
     * function that gets the first name of the user
     * @return string firstName
     */

    public String getFirstName() {
        return firstName;
    }

    /**
     * function that set the first name of the user
     * @param firstName first name of the user
     */

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * function that gets the last name of the user
     * @return string lastName
     */

    public String getLastName() {
        return lastName;
    }

    /**
     * function that set the last name of the user
     * @param lastName last name of the user
     */

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * function that transform the object User to an object UserBean
     * @return an object UserBean
     */

    public UserBean toBean() {
        return new UserBean(getFirstName(), getLastName());
    }

    /**
     * abstract function that gets the schedule of an user
     * @return a list of objects Medical
     */

    public abstract List<Medical> getSchedule();

}
