package com.example.mm24;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class leaveCommentController {
    @FXML
    public TextField ratingField;
    @FXML
    public Button leaveReviewButton;
    @FXML
    public TextArea reviewField;
    @FXML
    public Text errorMessage;
    private Book currentBook;
    public void attemptLeaveReview() throws NumberFormatException{
        if(reviewField.getText().isEmpty() || ratingField.getText().isEmpty()){
            errorMessage.setText("Review or Rating is empty!");
            errorMessage.setVisible(true);
        } else if(Double.parseDouble(ratingField.getText()) > 5 || Double.parseDouble(ratingField.getText()) < 0) {
            errorMessage.setText("Rating must be in the range 0 - 5");
            errorMessage.setVisible(true);
        } else{
            //review is valid, adding it to the books reviews, and updating its rating!
            errorMessage.setText("Review has been created!");
            errorMessage.setFill(Paint.valueOf("GREEN"));
            currentBook.getUserComments().add(new Review(reviewField.getText(), Double.parseDouble(ratingField.getText()), Main.currentUser, currentBook));
            //the review initializer already updates the books score, so we shouldn't do it here again!
            //We should close this window and update the comment scene here!
            Stage stage = (Stage) leaveReviewButton.getScene().getWindow();
            stage.close();
        }
    }

    public void initialize(Book b){
        currentBook = b;
    }
}
