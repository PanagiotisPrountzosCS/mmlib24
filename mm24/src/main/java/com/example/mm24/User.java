package com.example.mm24;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    private String name;
    private String lastName;
    private String emailAddress;
    public Boolean isAdmin = false;
    private String userID;
    private List<Book> userRequests = new ArrayList<>();
    private List<Book> userBorrowings = new ArrayList<>();

    private List<Book> booksReviewed = new ArrayList<>();
    public User(String givenUserName, String givenPassWord, String givenName, String givenLastName, String givenEmailAddress, String givenID) {
        this.username = givenUserName;
        this.password = givenPassWord;
        this.name = givenName;
        this.lastName = givenLastName;
        this.emailAddress = givenEmailAddress;
        this.userID = givenID;
    }
    //getters here
    public String getUserName(){
        return username;
    }
    public String getName(){
        return name;
    }
    public String getLastName(){
        return lastName;
    }
    public String getEmailAddress(){
        return emailAddress;
    }
    public String getUserID(){
        return userID;
    }
    public List<Book> getUserBorrowings(){
        return userBorrowings;
    }
    public List<Book> getUserRequests(){
        return userRequests;
    }
    public List<Book> getBooksReviewed() {
        return booksReviewed;
    }
    //setters here
    public void setUsername(String s){
        username = s;
    }
    public void setPassword(String s){
        password = s;
    }
    public void setName(String s){
        name = s;
    }
    public void setLastName(String s){
        lastName = s;
    }
    public void setEmailAddress(String s){
        emailAddress = s;
    }
    public void setUserID(String s){
        userID = s;
    }
    public boolean validatePassword(String passwd){
        return password.equals(passwd);
    }

}
