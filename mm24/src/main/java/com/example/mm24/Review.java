package com.example.mm24;

import java.io.Serializable;

public class Review implements Serializable {
    private String review;
    private double rating;
    private User user;
    private Book book;
    public Review(String s, double d, User u, Book b){
        review = s;
        rating = d;
        user = u;
        book = b;
        b.addRating(d);
        u.getBooksReviewed().add(b);
    }
    public String getReview(){
        return review;
    }
    public double getRating(){
        return rating;
    }
    public User getUser(){
        return user;
    }
}
