package com.example.mm24;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class adminMenuController {
    @FXML
    private VBox bookOptionsBox, userOptionsBox, requestBox, borrowedBooksVBox, topVBox, categoriesVBox;
    private void deleteUser(User u){
        //need to delete all active borrowings, as well as requests here :/
        for(int i = 0 ; i < Main.userRequests.size() ; i++){
            if(Main.userRequests.get(i).getUser() == u){
                Main.userRequests.remove(i);
                i--;
            }
        }
        for(int i = 0 ; i < Main.userBorrowings.size() ; i++){
            if(Main.userBorrowings.get(i).getUser() == u){
                Main.userBorrowings.remove(i);
                i--;
            }
        }
        Main.userMap.remove(u.getUserID());
        updateGUI();
    }
    private void acceptRequest(Request r){
        if(r.getUser().getUserBorrowings().size() >= 2){
            r.deny();
            updateGUI();
        }
        else {
            r.accept();
            updateGUI();
        }
    }

    public void updateGUI(){
        //we are updating the vbox responsible for user requests
        requestBox.getChildren().clear();
        requestBox.setPrefHeight(0);
        for(Request r : Main.userRequests) {
            requestBox.setPrefHeight(requestBox.getPrefHeight() + 200);
            HBox temp = new HBox();
            temp.setAlignment(Pos.CENTER);
            temp.setPrefHeight(200);
            temp.setPrefWidth(385);
            temp.setSpacing(20);
            Button accept = new Button("Accept"), deny = new Button("Deny");
            accept.setOnMouseClicked(event -> acceptRequest(r));
            deny.setOnMouseClicked(event -> {
                r.deny();
                updateGUI();
            });
            Text msg = new Text(r.getUser().getUserName() + " has requested \n" + r.getBook().getTitle());
            msg.setWrappingWidth(120);
            temp.getChildren().add(msg);
            temp.getChildren().add(accept);
            temp.getChildren().add(deny);
            requestBox.getChildren().add(temp);
        }

        //we are updating the vbox responsible for user options
        userOptionsBox.getChildren().clear();
        userOptionsBox.setPrefHeight(200*Main.userMap.size());
        for(User u : Main.userMap.values()){
            HBox temp = new HBox();
            temp.setAlignment(Pos.CENTER);
            temp.setPrefHeight(200);
            temp.setPrefWidth(385);
            temp.setSpacing(20);
            if(!u.getUserName().equals("medialab")) {
                Button delete = new Button("Delete User");
                Button options = new Button("Options");
                delete.setOnMouseClicked(event -> deleteUser(u));
                options.setOnMouseClicked(event -> {
                    try {
                        openUserOptions(u);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                temp.getChildren().add(delete);
                temp.getChildren().add(options);
            }
            Text msg = new Text(u.getUserName() + "\n" + u.getName() + " " + u.getLastName() + " (" + u.getUserID() + ")");
            msg.setWrappingWidth(120);
            temp.getChildren().add(msg);
            userOptionsBox.getChildren().add(temp);
        }
        //we are updating the vbox responsible for active borrowings
        borrowedBooksVBox.getChildren().clear();
        borrowedBooksVBox.setPrefHeight(0);
        for(Borrowing b : Main.userBorrowings){
            borrowedBooksVBox.setPrefHeight(borrowedBooksVBox.getPrefHeight() + 200);
            HBox temp = new HBox();
            temp.setAlignment(Pos.CENTER);
            temp.setPrefHeight(200);
            temp.setPrefWidth(385);
            temp.setSpacing(20);
            Button terminate = new Button("Terminate Borrowing");
            terminate.setOnMouseClicked(event -> {b.terminate(); updateGUI();});
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Text msg = new Text(b.getUser().getUserName() + " is currently borrowing \n" + b.getBook().getTitle() + "\nStart Date: " + formatter.format(b.getStartDate()) + "\nEnd Date: " + formatter.format(b.getEndDate()));
            msg.setWrappingWidth(120);
            temp.getChildren().add(msg);
            temp.getChildren().add(terminate);
            borrowedBooksVBox.getChildren().add(temp);
        }

        //We are updating the VBox responsible for the Book options
        bookOptionsBox.getChildren().clear();
        bookOptionsBox.setPrefHeight(150*Main.bookList.size() + 150);
        for(Book b : Main.bookList){
            HBox temp = new HBox();
            temp.setAlignment(Pos.CENTER);
            temp.setPrefHeight(200);
            temp.setPrefWidth(385);
            temp.setSpacing(20);
            Button options = new Button("Options");
            options.setOnMouseClicked(event -> {
                try {
                    openBookOptions(b);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Text msg = new Text(b.getTitle() + "\nISBN = " + b.getISBN() + "\nQuantity = " + b.getInventoryQuantity() + "\nCategory = " + b.getCategory());
            msg.setWrappingWidth(150);
            temp.getChildren().add(msg);
            temp.getChildren().add(options);
            bookOptionsBox.getChildren().add(temp);
        }
        //Before we update the categories VBox, we should add a button for creating new books in this VBox!

        HBox tempNewBook = new HBox();
        tempNewBook.setAlignment(Pos.CENTER);
        tempNewBook.setPrefHeight(200);
        tempNewBook.setPrefWidth(385);
        tempNewBook.setSpacing(20);
        Button createNewBookButton = new Button("Create new book");
        createNewBookButton.setOnMouseClicked(event -> {
            try {
                createNewBook();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        tempNewBook.getChildren().add(createNewBookButton);
        bookOptionsBox.getChildren().add(tempNewBook);

        //We are updating the VBox responsible for the available categories
        categoriesVBox.getChildren().clear();
        categoriesVBox.setPrefHeight(150*Main.bookCategories.size() + 150);
        for(String s : Main.bookCategories){
            HBox temp = new HBox();
            temp.setAlignment(Pos.CENTER);
            temp.setPrefHeight(200);
            temp.setPrefWidth(385);
            temp.setSpacing(20);
            Text msg = new Text(s);
            msg.setWrappingWidth(150);
            Button options = new Button("Options");
            options.setOnMouseClicked(event -> {
                try {
                    openCategoryOptions(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            temp.getChildren().add(msg);
            temp.getChildren().add(options);
            categoriesVBox.getChildren().add(temp);
        }
        //Similarly, we will create a Button for creating new categories here!

        HBox tempNewCategory = new HBox();
        tempNewCategory.setAlignment(Pos.CENTER);
        tempNewCategory.setPrefHeight(200);
        tempNewCategory.setPrefWidth(385);
        tempNewCategory.setSpacing(20);
        Button createNewCategoryButton = new Button("Create new category");
        createNewCategoryButton.setOnMouseClicked(event -> {
            try {
                createNewCategory();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        tempNewCategory.getChildren().add(createNewCategoryButton);
        categoriesVBox.getChildren().add(tempNewCategory);

    }
    private void openBookOptions(Book b) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("bookOptionsPopUp.fxml"));
        Scene scene = new Scene(loader.load());
        bookOptionsController popUpController = loader.getController();
        popUpController.initialize(b);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Book Options Menu");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        updateGUI();
    }
    private void openUserOptions(User u) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("userSettings.fxml"));
        Scene scene = new Scene(loader.load());
        userSettingsController newController = loader.getController();
        newController.initialize(u);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("User settings");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        if(Main.currentUser == null){
            stage = (Stage) bookOptionsBox.getScene().getWindow();
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
        updateGUI();
    }
    public void openCategoryOptions(String s) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("categoryPopUp.fxml"));
        Scene scene = new Scene(loader.load());
        //initialize new controller here!
        categoryPopUpController newController = loader.getController();
        newController.initialize(s);
        //initialize new controller here!
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Category Options Menu");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        updateGUI();
    }
    public void createNewBook() throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("newBookPopUp.fxml"));
        Scene scene = new Scene(loader.load());
        //initialize new controller here!

        //initialize new controller here!
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create new book");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        updateGUI();
    }
    public void createNewCategory() throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("newCategoryPopUp.fxml"));
        Scene scene = new Scene(loader.load());
        //initialize new controller here!

        //initialize new controller here!
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create new category");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        updateGUI();
    }
    public void initialize(){
        updateGUI();
    }
}
