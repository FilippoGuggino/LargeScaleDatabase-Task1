package com.task1.clinic;

import java.io.IOException;
import javafx.fxml.FXML;

public class InitialScreenController {

    @FXML
    private void switchToSignin() throws IOException {
        App.setRoot("signinScreen");
    }
    @FXML
    private void switchToSignup() throws IOException {
        App.setRoot("signupScreen");
    }
}
