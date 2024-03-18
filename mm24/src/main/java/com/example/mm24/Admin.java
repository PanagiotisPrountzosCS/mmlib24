package com.example.mm24;

import java.io.Serializable;

public class Admin extends User implements Serializable {
    public Admin(String givenUserName, String givenPassWord, String givenName, String givenLastName, String givenEmailAddress, String givenID) {
        super(givenUserName, givenPassWord, givenName, givenLastName, givenEmailAddress, givenID);
        this.isAdmin = true;
    }
}
