package com.example.mm24;

import java.io.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.List;

public class Main extends Application{
    static HashMap<String, User> userMap = new HashMap<>();
    static List<Book> bookList = new ArrayList<>();
    static List<String> bookCategories = new ArrayList<>();
    static List<Request> userRequests = new ArrayList<>();
    static List<Borrowing> userBorrowings = new ArrayList<>();
    static User currentUser;
    @Override
    public void start(Stage stage) throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("logInScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("MMLIB24");
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //This block was used to initialize the program's data. After that had been serialized,
        //this block was commented, because we read the serialized object from now on.
        boolean newData = false;
        if(newData) {
            //USER DATA INITIALIZATION
            User panos = new User("panosP", "29062001", "Panos", "Prountzos", "panoslmao@gmail.com", "ΑΒ123456");
            User admin = new Admin("medialab", "medialab_2024", "media", "lab", "idkman@gmail.com", "ΑΒ111111");
            User user1 = new User("drFreeman", "hl1guy", "Gordon", "Freeman", "gfreeman@blackmesa.com", "ΑΓ121212");
            User user2 = new User("numbersGuy", "thenumbersmason", "Alex", "Mason", "amason@gmail.com", "ΑΓ999999");
            User user3 = new User("simonH", "cof", "Simon", "Henriksson", "simonh@gmail.com", "ΑΓ421742");

            userMap.put(panos.getUserID(), panos);
            userMap.put(admin.getUserID(), admin);
            userMap.put(user1.getUserID(), user1);
            userMap.put(user2.getUserID(), user2);
            userMap.put(user3.getUserID(), user3);

            //BOOK DATA INITIALIZATION
            Book book1 = new Book("The C Programming Language", "Dennis Ritchie / Brian Kernighan", "9780131101630", new Date(1978 - 1900, 2 - 1, 22), "Prentice Hall", "Computer Science", 42);
            Book book2 = new Book("Microelectronic Circuits", "Adel Sedra / Kenneth Smith", "9780199339136", new Date(2014 - 1900, 11 - 1, 14), "Oxford University Press", "Electrical Engineering", 17);
            Book book3 = new Book("Analysis and Design of Analog Integrated Circuits", "Paul Gray / Robert Meyer", "9780470245996", new Date(2009 - 1900, 1 - 1, 20), "Wiley", "Electrical Engineering", 1);
            Book book4 = new Book("Modern Control Systems", "Richard Dorf / Robert Bishop", "9780134407623", new Date(2016 - 1900, 1 - 1, 5), "Pearson", "Control Theory", 2);
            Book book5 = new Book("Digital Signal Processing", "Alan Oppenheim / Ronald Schafer", "9780132146357", new Date(1975 - 1900, 1 - 1, 12), "Pearson", "Signal Processing", 9);


            bookList.add(book1);
            bookList.add(book2);
            bookList.add(book3);
            bookList.add(book4);
            bookList.add(book5);

            Request r1 = new Request(panos, book1);
            Request r2 = new Request(panos, book3);
            userRequests.add(r1);
            userRequests.add(r2);

            //Borrowing b1 = new Borrowing(panos, book3);
            Borrowing b2 = new Borrowing(panos, book4);
            //userBorrowings.add(b1);
            userBorrowings.add(b2);

        }

        else {
            //READING USER DATA
            FileInputStream fileIn = new FileInputStream("medialab/UsersMap.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            userMap = (HashMap<String, User>) in.readObject();
            in.close();
            fileIn.close();

            //READING BOOK DATA

            fileIn = new FileInputStream("medialab/bookList.ser");
            in = new ObjectInputStream(fileIn);
            bookList = (List<Book>) in.readObject();
            in.close();
            fileIn.close();

            //READING BOOK CATEGORIES

            fileIn = new FileInputStream("medialab/bookCategories.ser");
            in = new ObjectInputStream(fileIn);
            bookCategories = (List<String>) in.readObject();
            in.close();
            fileIn.close();

            //remake the requests as well as the borrowings here.
            //for some reason, the requests and borrowings saved
            //create new books, copies of the ones in booklist
            //and without this fix, the logic is broken
            for(User u : userMap.values()){
                HashMap<Book, Book> fixer = new HashMap<>();
                for(int i = 0 ; i < u.getUserRequests().size() ; i++){
                    for(Book b : bookList){
                        if(u.getUserRequests().get(i).getISBN().equals(b.getISBN())){
                            //remove ith element from getuserrequests and make new request for book b, user u and add it to userRequests
                            fixer.put(u.getUserRequests().get(i),b);
                            break;
                        }
                    }
                }
                u.getUserRequests().removeAll(fixer.keySet());
                for(Book b : fixer.values()){
                    userRequests.add(new Request(u, b));
                }
                fixer.clear();
                for(int i = 0 ; i < u.getUserBorrowings().size() ; i++){
                    for(Book b : bookList){
                        if(u.getUserBorrowings().get(i).getISBN().equals(b.getISBN())){
                            fixer.put(u.getUserBorrowings().get(i),b);
                            break;
                        }
                    }
                }
                u.getUserBorrowings().removeAll(fixer.keySet());
                for(Book b : fixer.values()){
                    userBorrowings.add(new Borrowing(u, b));
                }
            }
        }

        //the data is being read wrong for some reason. We NEED to remake the borrowings and the
        //requests here, before we start working with any of the data

        for(Book b : bookList){
            if(!bookCategories.contains(b.getCategory())){
                bookCategories.add(b.getCategory());
            }
        }

        //GRAPHICAL USER INTERFACE BEGINS HERE
        launch();
        //GRAPHICAL USER INTERFACE ENDS HERE (the user closed the program, time to save the data back to medialab directory)

        //WRITING USER DATA

        FileOutputStream fileOut = new FileOutputStream("medialab/UsersMap.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(userMap);
        out.close();
        fileOut.close();

        //WRITING BOOK DATA

        fileOut = new FileOutputStream("medialab/bookList.ser");
        out = new ObjectOutputStream(fileOut);
        out.writeObject(bookList);
        out.close();
        fileOut.close();

        //WRITING BOOK CATEGORIES

        fileOut = new FileOutputStream("medialab/bookCategories.ser");
        out = new ObjectOutputStream(fileOut);
        out.writeObject(bookCategories);
        out.close();
        fileOut.close();
    }
}
