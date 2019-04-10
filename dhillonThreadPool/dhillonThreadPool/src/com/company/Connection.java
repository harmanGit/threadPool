package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * The constructor initializes an instance variable with a reference to a socket.
 * This class implements the Runnable interface, and the run method has a loop
 * that reads a line of text from the client and then writes the same line back
 * to the client. You may need to flush the output stream after writing.
 *
 * Created by Harman Dhillon on 4/8/2019.
 */
public class Connection implements Runnable {
    private Socket socket;

    public Connection(Socket socket) {
        this.socket = socket;
        System.err.println("Connection Active: " + socket.getPort());//Debugging
    }

    public void run() {
        String message;
        Scanner clientScanner;
        PrintWriter clientWriter;
        try{
            clientScanner = new Scanner(socket.getInputStream());
            clientWriter = new PrintWriter(socket.getOutputStream(), true);

            while(clientScanner.hasNextLine()){
                message = clientScanner.nextLine();//Getting the message from the client
                System.err.println("message: " + message);//Debugging
                clientWriter.println("Server: " + message);//sending the client the echo message back
            }
        } catch(IOException e){
            System.err.println("Exception the the Connection Class. Exception:" + e);
        }
    }
}