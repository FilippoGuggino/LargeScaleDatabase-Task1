package com.task1.clinic;

import javax.persistence.*;
import java.util.List;

/**
 * Abstract class that represents a generic User of the system.
 */
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
     * constructor which takes and initializes all attributes of the class.
     * @param firstName first name of the user
     * @param lastName last name of the user
     */

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * default constructor.
     */

    public User() {

    }

    /**
     * Get the idCode of the user.
     * @return integer idCode
     */

    public int getIdCode() {
        return idCode;
    }

    /**
     * Set the idCode of the user through the specified one.
     * @param idCode idCode to be set to the user
     */

    public void setIdCode(int idCode) {
        this.idCode = idCode;
    }

    /**
     * Get the first name of the user.
     * @return string firstName
     */

    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the user.
     * @param firstName first name of the user
     */

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the last name of the user.
     * @return the last name of the user
     */

    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of the user.
     * @param lastName last name of the user
     */

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Transform the object User into an object UserBean.
     * @return an object UserBean
     */

    public UserBean toBean() {
        return new UserBean(getFirstName(), getLastName());
    }

    /**
     * Get the default schedule of the user.
     * @return the list of requested medicals
     */

    public abstract List<Medical> getSchedule();

}
