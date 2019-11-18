package com.task1.clinic;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void display(String text){
        Stage window= new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setWidth(250);
        window.setHeight(150);
        Label label=new Label(text);
        Button button=new Button("Close");
        button.setOnAction(e->window.close());
        VBox vbox=new VBox(10);
        vbox.getChildren().addAll(label,button);
        vbox.setAlignment(Pos.CENTER);
        Scene scene=new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();
    }
}
