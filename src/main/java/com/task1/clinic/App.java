package com.task1.clinic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static User user = null;
    public static int requestType;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("initialScreen"));
        stage.setWidth(630);
        stage.setHeight(480);
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

    public static void main(String[] args) throws ParseException {
        launch();

        PersistenceManager man = PersistenceManager.getInstance();
        /*
        Patient pat = new Patient("Francesco", "Francesconi");
        man.create(pat);
        Doctor doc = new Doctor("Carlo", "Vallati");
        man.create(doc);
        Date date = new Date();
        pat.createMedicalRequest(doc, date);
        List<Medical> res = pat.getSchedule();
        for(int i = 0; i < res.size(); ++i)
            System.out.println(res.get(i).toString());
        List<Medical> res2 = doc.getSchedule(date);
        for(int i = 0; i < res2.size(); ++i) {
            System.out.println("Risultato per dottore Carlo:");
            System.out.println(res2.get(i).toString());
        }
        Employee e = new Employee("giovanni", "lesto");
        man.create(e);
        DeleteRequest d = new DeleteRequest(res.get(0));
        man.create(d);
        String sDate1="31/12/1998";
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        MoveRequest m = new MoveRequest(res.get(0), date1);
        man.create(m);
        //e.handleDeleteRequest(d, false);
        e.handleMoveRequest(m, true);
        List<Medical> med = e.getSchedule(pat, null,null);
        System.out.println(med.get(0).getDate());

         */
        man.close();
    }

    public static void setUser(User user) {
        App.user = user;
    }

}