package com.example.mm24;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

public class logInController {
    @FXML
    private HBox bookBox;
    @FXML
    private Button signUpButton, logInButton;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text invalidCredentialsText;
    @FXML
    private void goToSignUpScene() throws IOException {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("signUpScene.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("MMLIB24 Sign Up");
        stage.setResizable(false);
        stage.show();
    }
    @FXML
    private void attemptLogIn() throws IOException{
        //THIS BLOCK SHOULD BE DONE AT MAIN AND REFERENCED HERE (probably?) nevermind lol

        for(User i : Main.userMap.values()) {
            if(i.getUserName().equals(userNameField.getText()) && i.validatePassword(passwordField.getText())) {
                //Successful login
                invalidCredentialsText.setText("Successful log in");
                invalidCredentialsText.setFill(Paint.valueOf("GREEN"));
                invalidCredentialsText.setVisible(true);
                Main.currentUser = i;
                //check if user is admin or not
                //change scene here
                if(i.isAdmin) {
                    Stage stage = (Stage) logInButton.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("adminMenu.fxml"));
                    Scene scene = new Scene(loader.load());
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.setTitle("MMLIB24 Admin Menu");
                    stage.show();
                    break;
                }
                else {

                    Stage stage = (Stage) logInButton.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("mainScene.fxml"));
                    Scene scene = new Scene(loader.load());
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.setTitle("MMLIB24 Main Menu");
                    stage.show();

                    break;
                }
            }
            //failed login here, don't change scene
            invalidCredentialsText.setText("Invalid Credentials");
            invalidCredentialsText.setFill(Paint.valueOf("RED"));
            invalidCredentialsText.setVisible(true);
        }

    }
    @FXML
    public void initialize(){
        //display top 5 books here as well;
        bookBox.setAlignment(Pos.CENTER);
        List<Book> temp = new ArrayList<>();
        temp.addAll(Main.bookList);
        Collections.sort(temp, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return (int) Math.signum(o2.getBookRating() - o1.getBookRating());
            }
        });
        if(temp.size()>=5) {
            for (int i = 0; i < 5; i++) {
                Text msg = new Text(temp.get(i).getTitle() + "\n" + temp.get(i).getBookRating() + "/5");
                msg.setStyle("-fx-font-size: 14; -fx-font-family: Apercu; -fx-fill:#00AAFF");
                msg.setWrappingWidth(150);
                bookBox.getChildren().add(msg);
            }
        }
        else{
            for (int i = 0; i < temp.size(); i++) {
                Text msg = new Text(temp.get(i).getTitle() + "\n" + temp.get(i).getBookRating() + "/5");
                msg.setStyle("-fx-font-size: 14; -fx-font-family: Apercu; -fx-fill:#00AAFF");
                msg.setWrappingWidth(150);
                bookBox.getChildren().add(msg);
            }
        }
        temp = null;
    }
}
