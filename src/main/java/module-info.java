module com.aridaje {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires net.bytebuddy;
    requires java.xml;
    requires java.xml.bind;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires leveldb.api;
    requires leveldb;

    opens com.task1.clinic to javafx.fxml, org.hibernate.orm.core;
    exports com.task1.clinic;
}