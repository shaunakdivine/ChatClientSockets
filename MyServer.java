package assignment5;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyServer {

    public static void main(String[] args) {
        Users user = new Users(6666);
        Thread userThread = new Thread(user);
        userThread.start();
        System.out.println("Waiting for clients");
    }
}



