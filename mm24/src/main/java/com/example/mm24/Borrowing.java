package com.example.mm24;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Borrowing implements Serializable {
    private User usr;
    private Book book;
    private Date startDate;
    private Date endDate;
    public Borrowing(User a, Book b){
        usr = a;
        book = b;
        startDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, 5);
        endDate = c.getTime();
        //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        a.getUserBorrowings().add(b);
    }
    public User getUser() {
        return usr;
    }
    public Book getBook(){
        return book;
    }
    public Date getStartDate(){
        return startDate;
    }
    public Date getEndDate(){
        return endDate;
    }
    public void terminate(){
        //this function should NOT be used in a for loop, because it removes this object.
        book.setInventoryQuantity(book.getInventoryQuantity() + 1);
        Main.userBorrowings.remove(this);
        usr.getUserBorrowings().remove(book);
    }
}
