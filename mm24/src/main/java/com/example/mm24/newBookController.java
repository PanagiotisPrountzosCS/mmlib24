package com.example.mm24;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.util.Date;
import java.util.function.Function;

public class newBookController {
    @FXML
    public TextField titleField, authorField, ISBNField, publisherField, releaseDateField, inventoryQuantityField;
    @FXML
    public Button createNewBook;
    @FXML
    public Text topText;
    @FXML
    public ChoiceBox categoryField;
    @FXML
    public Label errorMessage;

    public void initialize(){
        categoryField.getItems().addAll(Main.bookCategories);
        topText.setText("Create new book");
    }

    public void makeNewBook() {
        errorMessage.setTextFill(Paint.valueOf("RED"));
        boolean makeBook = true;
        if(titleField.getText().isEmpty() || authorField.getText().isEmpty() || ISBNField.getText().isEmpty() || publisherField.getText().isEmpty() || releaseDateField.getText().isEmpty() || inventoryQuantityField.getText().isEmpty() || categoryField.getValue() == null){
            errorMessage.setText("Some fields are empty!");
            errorMessage.setVisible(true);
            makeBook = false;
        }
        else if(!Functions.isValidISBN(ISBNField.getText())){
            makeBook = false;
            errorMessage.setText("ISBN is invalid! Correct format: 978[0-9]{10}");
            errorMessage.setVisible(true);
        }
        else if(!Functions.isNumeric(inventoryQuantityField.getText())){
            makeBook = false;
            errorMessage.setText("Inventory quantity is non-integer!");
            errorMessage.setVisible(true);
        }
        //check if title/isbn already exist! else, make the book!
        for(Book b : Main.bookList){
            if(b.getTitle().equals(titleField.getText()) || b.getISBN().equals(ISBNField.getText())){
                makeBook = false;
                errorMessage.setText("Title or ISBN are already taken by some other book!");
                errorMessage.setVisible(true);
                break;
            }
        }
        if(!Functions.isValidDate(releaseDateField.getText())){
            makeBook = false;
            errorMessage.setText("Invalid date format!(Correct format: dd-MM-yyyy)");
            errorMessage.setVisible(true);
        }
        if(makeBook){
            String[] parts = releaseDateField.getText().split("-");
            Date temp = new Date(Integer.parseInt(parts[2]) - 1900, Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[0]));
            errorMessage.setText("Book successfully created!");
            errorMessage.setTextFill(Paint.valueOf("GREEN"));
            errorMessage.setVisible(true);
            Main.bookList.add(new Book(titleField.getText(), authorField.getText(), ISBNField.getText(), temp, publisherField.getText(), categoryField.getValue().toString(), Integer.valueOf(inventoryQuantityField.getText())));
        }
    }
}
