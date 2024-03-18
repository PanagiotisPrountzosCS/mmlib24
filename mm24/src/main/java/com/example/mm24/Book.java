package com.example.mm24;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Book implements Serializable {
    private String title;
    private String author;
    private String ISBN;
    private Date releaseDate;
    private String publisher;
    private String category;
    private int inventoryQuantity;
    private double bookRating;
    private int numberOfReviews;
    private List<Review> userComments;

    public Book(String title, String author, String isbn, Date releaseDate, String publisher, String category, int quantity){
        this.title = title;
        this.author = author;
        this.ISBN = isbn;
        this.releaseDate = releaseDate;
        this.publisher = publisher;
        this.category = category;
        this.inventoryQuantity = quantity;
        this.bookRating = 0;
        this.numberOfReviews = 0;
        this.userComments = new ArrayList<Review>();
    }

    //getters here

    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getISBN(){
        return ISBN;
    }
    public Date getReleaseDate(){
        return releaseDate;
    }
    public String getPublisher(){
        return publisher;
    }
    public String getCategory(){
        return category;
    }
    public int getInventoryQuantity(){
        return inventoryQuantity;
    }
    public double getBookRating(){
        return bookRating;
    }
    public int getNumberOfReviews(){
        return numberOfReviews;
    }
    public List<Review> getUserComments(){
        return userComments;
    }

    //setters here
    public void setTitle(String s){
        title = s;
    }
    public void setAuthor(String s){
        author = s;
    }
    public void setISBN(String s){
        ISBN = s;
    }
    public void setReleaseDate(Date d){
        releaseDate = d;
    }
    public void setPublisher(String s){
        publisher = s;
    }
    public void setCategory(String s){
        category = s;
    }
    public void setInventoryQuantity(int i){
        inventoryQuantity = i;
    }

    public void addRating(double newRating){
        this.bookRating = (this.bookRating*this.numberOfReviews + newRating) / (this.numberOfReviews + 1);
        this.numberOfReviews += 1;
    }
}
