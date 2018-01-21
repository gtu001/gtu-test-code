package gtu.javafx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class TestController extends AnchorPane implements Initializable {
    @FXML
    TextField userId;
    @FXML
    PasswordField password;
    @FXML
    Button loginBtn;
    @FXML
    Label errorMessage;

    private FXMLExemple application;

    public void setApp(FXMLExemple application) {
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorMessage.setText("");
        userId.setPromptText("demo");
        password.setPromptText("demo");
    }

    public void processLogin(ActionEvent event) {
        if (application == null) {
            // We are running in isolated FXML, possibly in Scene Builder.
            // NO-OP.
            errorMessage.setText("Hello " + userId.getText());
        } else {
            // if (!application.userLogging(userId.getText(),
            // password.getText())) {
            errorMessage.setText("Username/Password is incorrect");
            // }
        }
    }
}