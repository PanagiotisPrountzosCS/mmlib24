package com.example.mm24;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class userSettingsController {
    @FXML
    public TextField userNameField, passwordField, firstNameField, emailField, lastNameField, userIDField;
    @FXML
    public Button updateUserInfoButton, deleteUserButton;
    @FXML
    public Text topText, usernamePopUp, passwordPopUp, namePopUp, emailPopUp, lastNamePopUp, IDPopUp;
    private User mainUser; // this object is used instead of Main.currentUser
    public void update(User u){
        userNameField.setPromptText(u.getUserName());
        firstNameField.setPromptText(u.getName());
        lastNameField.setPromptText(u.getLastName());
        emailField.setPromptText(u.getEmailAddress());
        userIDField.setPromptText(u.getUserID());
    }
    public void initialize(User u){
        mainUser = u;
        update(mainUser);
    }

    public void updateUserInfo() {
        boolean updateTrigger = true;
        if (!userNameField.getText().isEmpty()) {
            //check if username is already taken
            for (User i : Main.userMap.values()) {
                if (i.getUserName().equals(userNameField.getText())) {
                    //someone is using this username
                    usernamePopUp.setText("Username already in use!");
                    usernamePopUp.setVisible(true);
                    usernamePopUp.setFill(Paint.valueOf("RED"));
                    updateTrigger = false;
                    break;
                }
            }
            if(updateTrigger){
                mainUser.setUsername(userNameField.getText());
                usernamePopUp.setText("Username updated!");
                usernamePopUp.setVisible(true);
                usernamePopUp.setFill(Paint.valueOf("GREEN"));
            }
        }
        if (!passwordField.getText().isEmpty()) {
            //no checks needed here
            mainUser.setPassword(passwordField.getText());
            passwordPopUp.setText("Password Updated!");
            passwordPopUp.setVisible(true);
            passwordPopUp.setFill(Paint.valueOf("GREEN"));
        }
        if (!firstNameField.getText().isEmpty()) {
            //no checks here
            namePopUp.setText("Name Updated!");
            namePopUp.setVisible(true);
            namePopUp.setFill(Paint.valueOf("GREEN"));
            mainUser.setName(firstNameField.getText());
        }
        updateTrigger = true;
        if(!emailField.getText().isEmpty()) {
            if (Functions.isValidEmailAddress(emailField.getText())) {
                //probably check if it is not being used already
                for (User i : Main.userMap.values()) {
                    if (i.getEmailAddress().equals(emailField.getText())) {
                        emailPopUp.setText("Email address already in use!");
                        emailPopUp.setVisible(true);
                        emailPopUp.setFill(Paint.valueOf("RED"));
                        updateTrigger = false;
                        break;
                    }
                }
                if(updateTrigger){
                    mainUser.setEmailAddress(emailField.getText());
                    emailPopUp.setText("Email address updated!");
                    emailPopUp.setVisible(true);
                    emailPopUp.setFill(Paint.valueOf("GREEN"));
                }
            }
            else{
                emailPopUp.setText("Invalid Email Address!");
                emailPopUp.setVisible(true);
                emailPopUp.setFill(Paint.valueOf("RED"));
            }
        }
        if (!lastNameField.getText().isEmpty()) {
            //no checks needed here
            lastNamePopUp.setText("Last name Updated!");
            lastNamePopUp.setVisible(true);
            lastNamePopUp.setFill(Paint.valueOf("GREEN"));
            mainUser.setLastName(lastNameField.getText());
        }
        if(!userIDField.getText().isEmpty()) {
            if (Functions.isValidID(userIDField.getText())) {
                //checks ARE needed here, id is the identifier in the USERMAP (and also unique lol);
                for (User i : Main.userMap.values()) {
                    if (i.getUserID().equals(userIDField.getText())) {
                        //someone is using this ID
                        IDPopUp.setText("ID is already in use!");
                        IDPopUp.setVisible(true);
                        IDPopUp.setFill(Paint.valueOf("RED"));
                        break;
                    }
                    mainUser.setUserID(userIDField.getText());
                    IDPopUp.setText("ID Updated!");
                    IDPopUp.setVisible(true);
                    IDPopUp.setFill(Paint.valueOf("GREEN"));
                }
            } else {
                IDPopUp.setText("Invalid ID!");
                IDPopUp.setVisible(true);
                IDPopUp.setFill(Paint.valueOf("RED"));
            }
        }
        update(mainUser);
    }
    public void deleteUser() throws IOException {
        //we must also remove all active requests as well as borrowings!!!
        for(int i = 0 ; i < Main.userRequests.size(); i++){
            if(Main.userRequests.get(i).getUser() == mainUser){
                Main.userRequests.get(i).deny();
                i--;
            }
        }
        for(int i = 0 ; i < Main.userBorrowings.size(); i++){
            if(Main.userBorrowings.get(i).getUser() == mainUser){
                Main.userBorrowings.get(i).terminate();
                i--;
            }
        }
        Main.userMap.remove(mainUser.getUserID());
        if(!Main.currentUser.isAdmin){
            Main.currentUser = null;
        }
        else{
            mainUser = null;
        }
        //user has been deleted, return to log in screen;
        Stage stage = (Stage) deleteUserButton.getScene().getWindow();
        stage.close();

        /*FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("logInScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("MMLIB24");
        stage.setResizable(false);
        stage.show();
         */
        //return happens here;
    }
}
