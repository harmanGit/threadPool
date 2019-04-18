package com.harmanDhillon.threadPoolSynchronized;

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
        String messageToSend = "";//message the user wants to send to the server, so it can be echoed back
        PrintWriter serverWriter;//writer is used to send a message to the server
        Scanner serverScanner;//scanner is used to get a message back from the server
        Socket serverSocket;//socket for the server

        try {
            //getting a copy of the server socket, based off local host and port number
            serverSocket = new Socket(HOST, EchoServer.getPort());
            System.out.println("Echo Client Connected.");
            serverWriter = new PrintWriter(serverSocket.getOutputStream(), true);
            serverScanner = new Scanner(serverSocket.getInputStream());

            do {
                System.out.print("Client: ");//Indictor for the client
                messageToSend = sn.nextLine();//getting and storing user input from the terminal
                serverWriter.println(messageToSend);//sending the server the message
                System.out.println(serverScanner.nextLine());//printing out server input
                if (messageToSend.equals(".")) {//ending the client if a "." is entered
                    serverSocket.close();//closing socket
                    return;
                }
            } while (true);
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Exception the the Connection Class. Exception:" + e);
        }
    }
}