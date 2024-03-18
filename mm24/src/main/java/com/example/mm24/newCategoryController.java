package com.example.mm24;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class newCategoryController {
    @FXML
    public Button createNewCategoryButton;
    @FXML
    public TextField newNameField;
    @FXML
    public Label errorMessage;

    public void createNewCategory() {
        if (newNameField.getText().isEmpty()) {
            errorMessage.setVisible(true);
        }
        else if(Main.bookCategories.contains(newNameField.getText())){
            errorMessage.setText("Category already exists!");
            errorMessage.setVisible(true);
        }
        else{
            Main.bookCategories.add(newNameField.getText());
            Stage stage = (Stage) createNewCategoryButton.getScene().getWindow();
            stage.close();
        }
    }
}
