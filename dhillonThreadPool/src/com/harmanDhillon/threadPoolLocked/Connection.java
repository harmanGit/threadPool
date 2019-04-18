package com.harmanDhillon.threadPoolLocked;

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
    private Socket socket; //stores the client socket

    /**
     * Constructor for the connection, requires a socket
     * which is meant to be the client socket.
     * @param socket <code>Socket</code> client socket
     */
    public Connection(Socket socket) {
        this.socket = socket;
    }

    /**
     * This method meant to be used when running a thread, this contains the echo feature of the program.
     */
    public void run() {
        String message;//message from the client
        Scanner clientScanner;//used for receiving input from the client
        PrintWriter clientWriter;//used for output back to the client
        try{
            clientScanner = new Scanner(socket.getInputStream());//scanner created based off the client socket
            clientWriter = new PrintWriter(socket.getOutputStream(), true);//writer created based off the client socket

            while(clientScanner.hasNextLine()){
                message = clientScanner.nextLine();//Getting the message from the client
                clientWriter.println("Server: " + message);//sending the client the "echo message back"
            }
        } catch(IOException e){
            System.err.println("Exception the the Connection Class. Exception:" + e);
        }
    }
}