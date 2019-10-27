package com.task1.clinic;

import javafx.beans.property.SimpleStringProperty;

public class UserBean {
    private SimpleStringProperty patName;
    private SimpleStringProperty patSurname;

    public UserBean(String patName,String patSurname){
        this.patName=new SimpleStringProperty(patName);
        this.patSurname=new SimpleStringProperty(patSurname);
    }
    public String getPatName(){
        return patName.get();
    }
    public String getPatSurname(){
        return patSurname.get();
    }
}
