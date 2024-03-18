package com.example.mm24;

import java.io.Serializable;


/**
 * The class Request implements serializable
 */
public class Request implements Serializable {
    private User usr;
    private Book book;

    /**
     *
     * Gets the user
     *
     * @return the user
     */
    public User getUser(){

        return usr;
    }

    /**
     *
     * Gets the book
     *
     * @return the book
     */
    public Book getBook(){

        return book;
    }

    /**
     *
     * Request
     *
     * @param user  the user.
     * @param book  the book.
     * @return public
     */
    public Request(User user, Book book) {

        this.usr = user;
        this.book = book;
        //There is no need to track wether or not the inventory of book b is more than 0,
        //We do that check in the event handler of the Request creation button,
        //and thus create a new request or not.
        user.getUserRequests().add(book);
    }


    /**
     *
     * Deny
     *
     */
    public void deny() {

        //this method should not be used in a for loop, because it removes this object from userRequests
        //and could, thus, create an out-of-bounds error
        Main.userRequests.remove(this);
        usr.getUserRequests().remove(book);
    }


    /**
     *
     * Accept
     *
     */
    public void accept() {
        //if there's more than 1 book in the inventory, we will give the book to the user
        if(book.getInventoryQuantity()>=1) {
            Main.userRequests.remove(this);
            book.setInventoryQuantity(book.getInventoryQuantity() - 1);
            Main.userBorrowings.add(new Borrowing(usr, book));
            usr.getUserRequests().remove(book);
        }
        //otherwise, we deny the request
        else{
            //this.deny();
            Main.userRequests.remove(this);
            usr.getUserRequests().remove(book);
        }
    }
}
