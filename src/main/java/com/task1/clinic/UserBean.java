package com.task1.clinic;

import javafx.beans.property.SimpleStringProperty;

public class UserBean {
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;

    public UserBean(String firstName,String lastName){
        this.firstName=new SimpleStringProperty(firstName);
        this.lastName=new SimpleStringProperty(lastName);
    }
    public String getFirstName(){
        return firstName.get();
    }

    public String getLastName(){return lastName.get(); }
}
