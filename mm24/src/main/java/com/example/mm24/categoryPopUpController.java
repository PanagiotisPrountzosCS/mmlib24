package com.example.mm24;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class categoryPopUpController {
    @FXML
    public Button deleteCategoryButton, updateNameButton;
    @FXML
    public TextField newNameField;
    @FXML
    public Label topLabel, errorLabel;
    private String currentCategory;
    public void initialize(String s){
        currentCategory = s;
        topLabel.setText(s + " Options");
    }
    public void deleteCategory(){
        Main.bookCategories.remove(currentCategory);
        for(int i = 0 ; i < Main.bookList.size() ; i++){
            if(Main.bookList.get(i).getCategory().equals(currentCategory)){
                Main.bookList.remove(i);
                i--;
            }
        }
        //check if there's any active borrowings or requests for the books of this category, and delete them!
        for(int i = 0 ; i < Main.userRequests.size() ; i++){
            if(Main.userRequests.get(i).getBook().getCategory().equals(currentCategory)){
                Main.userRequests.get(i).deny();
                i--;
            }
        }
        for(int i = 0 ; i < Main.userBorrowings.size() ; i++){
            if(Main.userBorrowings.get(i).getBook().getCategory().equals(currentCategory)){
                Main.userBorrowings.get(i).terminate();
                i--;
            }
        }
        //close pop up here
        Stage stage = (Stage) deleteCategoryButton.getScene().getWindow();
        stage.close();
    }
    public void updateCategory(){
        String newCategory = newNameField.getText();
        if(newCategory.isEmpty()){
            errorLabel.setText("New name field is empty!");
            errorLabel.setVisible(true);
        }
        else{
            errorLabel.setVisible(false);
            //replace the string in Main.bookCategories, and replace the category of all books that have the old one
            Main.bookCategories.remove(currentCategory);
            Main.bookCategories.add(newCategory);
            for(Book b : Main.bookList){
                if(b.getCategory().equals(currentCategory)){
                    b.setCategory(newCategory);
                }
            }
            topLabel.setText(newCategory + " Options");
            //anything else?
        }
    }
}
