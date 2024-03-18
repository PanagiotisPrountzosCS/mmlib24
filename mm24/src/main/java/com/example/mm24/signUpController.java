package com.example.mm24;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.IOException;

public class signUpController {

    @FXML
    private Button createAccount, returnToLogInButton;
    @FXML
    private TextField usernameField, firstNameField, lastNameField, idField, emailField;
    @FXML
    public Text errorMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    public void attemptSignUp(){
        boolean emptyInputs, validCredentials = true;
        emptyInputs = usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty() || firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty() || idField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty();

        if(emptyInputs){
            //empty inputs error
            errorMessage.setText("All fields must be filled!");
            errorMessage.setFill(Paint.valueOf("RED"));
            errorMessage.setWrappingWidth(200);
            errorMessage.setVisible(true);
            validCredentials = false;
        }
        else if(!Functions.isValidID(idField.getText())){
            //id is not numeric...
            errorMessage.setText("ID does not follow the format [Α-Ω]{2}[0-9]{6}");
            errorMessage.setFill(Paint.valueOf("RED"));
            errorMessage.setWrappingWidth(200);
            errorMessage.setVisible(true);
            validCredentials = false;
        }
        else if(Main.userMap.get(idField.getText()) != null){
            //userID already exists in userMap
            errorMessage.setText("ID is already taken by other user");
            errorMessage.setFill(Paint.valueOf("RED"));
            errorMessage.setWrappingWidth(200);
            errorMessage.setVisible(true);
            validCredentials = false;
        }
        else if(!Functions.isValidEmailAddress(emailField.getText())){
            //invalid email address given
            errorMessage.setText("E-mail address is invalid");
            errorMessage.setFill(Paint.valueOf("RED"));
            errorMessage.setWrappingWidth(200);
            errorMessage.setVisible(true);
            validCredentials = false;
        }
        if(validCredentials) {
            for (User i : Main.userMap.values()) {
                if (i.getEmailAddress().equals(emailField.getText()) || i.getUserName().equals(usernameField.getText())) {
                    //Other user has this email or username, try again
                    errorMessage.setText("E-mail or username is taken by other user");
                    errorMessage.setFill(Paint.valueOf("RED"));
                    errorMessage.setWrappingWidth(200);
                    errorMessage.setVisible(true);
                    validCredentials = false;
                    break;
                }
            }
        }
        //At this point, all user input is valid, we can update the hash map
        if(validCredentials) {
            errorMessage.setText("Account successfully created, please return to log in");
            errorMessage.setFill(Paint.valueOf("GREEN"));
            errorMessage.setWrappingWidth(200);
            errorMessage.setVisible(true);
            Main.userMap.put(idField.getText(), new User(usernameField.getText(), passwordField.getText(), firstNameField.getText(), lastNameField.getText(), emailField.getText(), idField.getText()));
        }

    }
    public void goToLogInScene() throws IOException {
        Stage stage = (Stage) returnToLogInButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("logInScene.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }
}
