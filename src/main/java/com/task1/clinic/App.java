package com.task1.clinic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("initialScreen"));
        stage.setWidth(500);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
        Manager man = Manager.getInstance();
        PatientEntity pat = new PatientEntity("Francesco", "Francesconi");
        man.create(pat);
        DoctorEntity doc = new DoctorEntity("Carlo", "Vallati");
        man.create(doc);
        Date date = new Date();
        pat.createMedicalRequest(doc, date);
        List<MedicalEntity> res = pat.getSchedule();
        for(int i = 0; i < res.size(); ++i)
            System.out.println(res.get(i).toString());
        man.close();
    }

}