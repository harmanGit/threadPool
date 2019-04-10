package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The client tries to pen a connection on the local host. It reads a line
 * of text from the keyboard, writes it to the server and then writes the
 * server's response to the console. This continues until the user enters a
 * single period, with terminates the connection.
 * <p>
 * Created by Harman Dhillon on 4/8/2019.
 */
public class EchoClient {

    public static void main(String[] args) {
        System.out.println("Starting Echo Client.");
        Scanner sn = new Scanner(System.in);
        final String HOST = "127.0.0.1";
        String messageToSend = "";
        PrintWriter serverWriter;
        Scanner serverScanner;
        Socket serverSocket;

        try {
            serverSocket = new Socket(HOST, EchoServer.getPort());
            System.out.println("Echo Client Connected.");
            System.err.println("server socket added: " + serverSocket.getPort() + "\n"); //debugging
            serverWriter = new PrintWriter(serverSocket.getOutputStream(), true);
            serverScanner = new Scanner(serverSocket.getInputStream());

            do {
                System.out.print("Client: ");//init output
                messageToSend = sn.nextLine();
                //System.out.print("Client: "+ messageToSend);//printing out user input
                serverWriter.println(messageToSend);//sending the server the message
                System.out.println(serverScanner.nextLine());//printing out server input
                if (messageToSend.equals(".")) {
                    return;
                }
            } while (true);


        } catch (IOException | NoSuchElementException e) {
            System.err.println("Exception the the Connection Class. Exception:" + e);
        }
    }
}