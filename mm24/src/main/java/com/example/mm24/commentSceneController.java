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

public class commentSceneController {
    @FXML
    public Text topText;
    @FXML
    private VBox reviewsBox;
    private Book currentBook;
    public void initialize(Book b){
        currentBook = b;
        topText.setText("Reviews of " + currentBook.getTitle());
        updateGUI();
    }
    private void createReview(Book b) throws IOException {
        //probably create new window here, to input text
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("leaveCommentScene.fxml"));
        Scene scene = new Scene(loader.load());
        leaveCommentController newController = loader.getController();
        newController.initialize(b);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Leave Review");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        updateGUI();
    }
    private void updateGUI(){
        reviewsBox.getChildren().clear();
        reviewsBox.getChildren().add(topText);
        boolean reviewTrigger = false;
        Button leaveReview = new Button("Leave a review");
        leaveReview.setOnMouseClicked(event -> {
            try {
                createReview(currentBook);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        for(Book b1 : Main.currentUser.getUserBorrowings()){
            if(b1.getISBN().equals(currentBook.getISBN())){
                reviewTrigger = true;
                for(Book b2 : Main.currentUser.getBooksReviewed()){
                    if(b1.getISBN().equals(b2.getISBN())){
                        reviewTrigger = false;
                        break;
                    }
                }
            }
        }

        if(reviewTrigger){
            reviewsBox.getChildren().add(leaveReview);
        }

        reviewsBox.setPrefHeight(200*currentBook.getUserComments().size());
        for(Review r : currentBook.getUserComments()){
            //all reviews will go on a text in a HBox, along with the rating on the right
            HBox temp = new HBox();
            temp.setPrefHeight(200);
            temp.setAlignment(Pos.CENTER);
            Text msg = new Text(r.getUser().getUserName() + '\n' + r.getReview() + '\n');
            msg.setWrappingWidth(450);
            msg.setStyle("-fx-font-size: 18; -fx-font-family: Apercu; -fx-fill:#00AAFF");
            Text rating = new Text(r.getRating() + "/5\n");
            rating.setStyle("-fx-font-size: 18; -fx-font-family: Apercu; -fx-fill:#00AAFF");
            temp.getChildren().add(msg);
            temp.getChildren().add(rating);
            reviewsBox.getChildren().add(temp);
        }
    }
}
