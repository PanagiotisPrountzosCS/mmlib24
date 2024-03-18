package com.example.mm24;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class bookOptionsController {
    Book currentBook;
    @FXML
    TextField titleField, authorField, ISBNField, publisherField, releaseDateField, inventoryQuantityField;
    @FXML
    Button updateInfoButton, deleteBookButton;
    @FXML
    Text topText;
    @FXML
    public ChoiceBox categoryField;
    @FXML
    public void updateBookInfo(){
        if(!titleField.getText().isEmpty()){
            currentBook.setTitle(titleField.getText());
        }
        if(!authorField.getText().isEmpty()){
            currentBook.setAuthor(authorField.getText());
        }
        if(!ISBNField.getText().isEmpty()){
            currentBook.setISBN(ISBNField.getText());;
        }
        if(!publisherField.getText().isEmpty()){
            currentBook.setPublisher(publisherField.getText());
        }
        if(!releaseDateField.getText().isEmpty()){
            if(Functions.isValidDate(releaseDateField.getText()) && releaseDateField.getText().contains("-")){
                String[] parts = releaseDateField.getText().split("-");
                Date temp = new Date(Integer.parseInt(parts[2]) - 1900, Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[0]));
                currentBook.setReleaseDate(temp);
            }
        }
        if(categoryField.getValue() != null){
            currentBook.setCategory(categoryField.getValue().toString());
        }
        if(!inventoryQuantityField.getText().isEmpty()){
            currentBook.setInventoryQuantity(Integer.valueOf(inventoryQuantityField.getText()));
        }
    }
    public void initialize(Book b){
        categoryField.getItems().addAll(Main.bookCategories);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        currentBook = b;
        topText.setWrappingWidth(150);
        titleField.setPromptText(b.getTitle());
        authorField.setPromptText(b.getAuthor());
        ISBNField.setPromptText(b.getISBN());
        publisherField.setPromptText(b.getPublisher());
        releaseDateField.setPromptText(sdf1.format(b.getReleaseDate()));
        inventoryQuantityField.setPromptText(String.valueOf(b.getInventoryQuantity()));
        topText.setText(b.getTitle() + " Info");
    }
    public void deleteBook(){
        //handle all requests and borrowings related to this book
        for(int i = 0 ; i < Main.userRequests.size() ; i++){
            if(Main.userRequests.get(i).getBook() == currentBook){
                Main.userRequests.get(i).deny();
                i--;
            }
        }
        for(int i = 0 ; i < Main.userBorrowings.size() ; i++){
            if(Main.userBorrowings.get(i).getBook() == currentBook){
                Main.userBorrowings.get(i).terminate();
                i--;
            }
        }
        //then remove from bookList
        Main.bookList.remove(currentBook);
        Stage stage = (Stage) deleteBookButton.getScene().getWindow();
        stage.close();
    }
}
