package com.example.mm24;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class mainSceneController {
    @FXML
    VBox mainVBox, bookVBox;
    @FXML
    HBox topHBox;
    @FXML
    ChoiceBox<String> sortByBox;
    @FXML
    Text msgText;
    @FXML
    Button userSettingsButton;
    @FXML
    ScrollPane mainPane;
    @FXML
    TextField searchBar;
    @FXML
    private void openUserSettingsMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("userSettings.fxml"));
        Scene scene = new Scene(loader.load());
        userSettingsController newController = loader.getController();
        newController.initialize(Main.currentUser);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("User settings");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        if(Main.currentUser == null){
            stage = (Stage) userSettingsButton.getScene().getWindow();
            stage.close();
            //if we are here, then the current user has been deleted through settings.
            //therefore, we will send the user back to the login scene

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("logInScene.fxml"));
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle("MMLIB24");
            stage.setResizable(false);
            stage.show();
        }
    }
    public void updateGUI(String s){
        bookVBox.getChildren().clear();
        bookVBox.setPrefHeight(0);
        for(Book b : Main.bookList){
            if(b.getTitle().toLowerCase().contains(s.toLowerCase()) || b.getAuthor().toLowerCase().toLowerCase().contains(s.toLowerCase()) || b.getReleaseDate().toString().toLowerCase().contains(s.toLowerCase()) || b.getCategory().toLowerCase().contains(s.toLowerCase())) {
                bookVBox.setPrefHeight(bookVBox.getPrefHeight() + 200);
                HBox temp = new HBox();
                temp.setSpacing(50);
                temp.setAlignment(Pos.CENTER);
                temp.setPrefHeight(200);
                temp.setPrefWidth(385);
                Text msg1 = new Text(b.getTitle() + "\n" + b.getAuthor());
                msg1.setStyle("-fx-font-size: 18; -fx-font-family: Apercu; -fx-fill:#00AAFF");
                msg1.setWrappingWidth(250);
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Text msg2 = new Text("Category: " + b.getCategory() + "\nISBN: " + b.getISBN() + "\nQuantity = " + b.getInventoryQuantity() + "\nRelease Date: " + formatter.format(b.getReleaseDate()));
                msg2.setWrappingWidth(200);
                Text msg3 = new Text("Rating: " + b.getBookRating() + "/5\n" + b.getNumberOfReviews() + " Review(s)\n");
                msg3.setWrappingWidth(150);
                //all default messages have been created, now we need to make buttons that interact with the users borrowings.
                Button commentsButton = new Button("View Comments");
                commentsButton.setOnMouseClicked(event -> {
                    try {
                        openCommentsPopUp(b);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                Button requestButton = new Button("Request Book");
                requestButton.setOnMouseClicked(event -> {
                    attemptRequestBook(b);
                });
                for (Borrowing borrowing : Main.userBorrowings) {
                    if (borrowing.getBook() == b && borrowing.getUser() == Main.currentUser) {
                        requestButton.setText("Return Book");
                        requestButton.setOnMouseClicked(event -> {
                            attemptReturnBook(borrowing);
                        });
                        msg3.setText(msg3.getText() + "You are borrowing this book, leave a review on the comments section!\nBorrowing Date: " + formatter.format(borrowing.getStartDate()) + "\nReturn Date: " + formatter.format(borrowing.getEndDate()));
                        for(Review r : b.getUserComments()){
                            if(r.getUser().getUserID().equals(Main.currentUser.getUserID())){
                                msg3.setText("Rating: " + b.getBookRating() + "/5\n" + b.getNumberOfReviews() + " Review(s)\n" + "You are borrowing this book!\nBorrowing Date: " + formatter.format(borrowing.getStartDate()) + "\nReturn Date: " + formatter.format(borrowing.getEndDate()));
                                break;
                            }
                        }
                        break;
                    }
                }

                temp.getChildren().add(msg1);
                temp.getChildren().add(msg2);
                temp.getChildren().add(msg3);
                temp.getChildren().add(commentsButton);
                temp.getChildren().add(requestButton);
                bookVBox.getChildren().add(temp);
            }
        }
    }
    public void initialize(){
        msgText.setWrappingWidth(250);
        msgText.setText(msgText.getText() + ", " + Main.currentUser.getUserName() + "!");
        sortByBox.getItems().addAll(new String[]{"Name", "Rating, low to high", "Rating, high to low", "Available Copies", "Category"});
        sortByBox.setOnAction(actionEvent -> {updateBookList(sortByBox.getValue());});
        updateGUI("");
    }
    public void updateBookList(String option){
        if(option.equals("Name")){
            Collections.sort(Main.bookList, new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
            });
        }
        else if(option.equals("Rating, low to high")){
            Collections.sort(Main.bookList, new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return (int) Math.signum(o1.getBookRating() - o2.getBookRating());
                }
            });
        }
        else if(option.equals("Rating, high to low")){
            Collections.sort(Main.bookList, new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return (int) Math.signum(o2.getBookRating() - o1.getBookRating());
                }
            });
        }
        else if(option.equals("Available Copies")){
            Collections.sort(Main.bookList, new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o2.getInventoryQuantity() - o1.getInventoryQuantity();
                }
            });
        }
        else if(option.equals("Category")){
            Collections.sort(Main.bookList, new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o1.getCategory().compareTo(o2.getCategory());
                }
            });
        }
        //we have sorted the list, we now need to update the gui
        updateGUI(searchBar.getText());
    }
    public void attemptRequestBook(Book b){
        if(Main.currentUser.getUserRequests().contains(b)){
            msgText.setText("You have already requested this book!");
            msgText.setStyle("-fx-font-size: 14");
            msgText.setWrappingWidth(250);
        }
        else if(Main.currentUser.getUserBorrowings().size() == 2){
            msgText.setText("You cannot borrow more than 2 books at a time!");
            msgText.setStyle("-fx-font-size: 14");
            msgText.setWrappingWidth(250);
        }
        else if(b.getInventoryQuantity()>0){
            Request temp = new Request(Main.currentUser, b);
            msgText.setText("You have requested: " + b.getTitle() + "\nAwaiting admin approval");
            msgText.setStyle("-fx-font-size: 14");
            msgText.setWrappingWidth(250);
        }
        else{
            msgText.setText("There are no more copies of this book available!");
            msgText.setStyle("-fx-font-size: 14");
            msgText.setWrappingWidth(250);
        }
        updateGUI(searchBar.getText());
    }
    public void attemptReturnBook(Borrowing b){
        msgText.setText("You have returned " + b.getBook().getTitle());
        msgText.setStyle("-fx-font-size: 14");
        msgText.setWrappingWidth(250);
        b.terminate();
        updateGUI(searchBar.getText());
    }
    public void openCommentsPopUp(Book b) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("commentScene.fxml"));
        Scene scene = new Scene(loader.load());
        commentSceneController newController = loader.getController();
        newController.initialize(b);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Book Reviews");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        updateGUI("");
    }
    public void keyPress() {
        updateGUI(searchBar.getText());
    }
}
