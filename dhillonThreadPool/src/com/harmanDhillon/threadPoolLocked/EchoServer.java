package com.harmanDhillon.threadPoolLocked;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server opens a server and listens (in an infinite loop) for connections.
 * When it accepts a new connection, the server creates a Connection Object and
 * it to the thread pool.
 * <p>
 * Created by Harman Dhillon on 4/8/2019.
 */
public class EchoServer {

    private static final int PORT = 1111;

    public static void main(String[] args) throws IOException {
        System.out.println("Server Started.");
        ServerSocket serverSocket = new ServerSocket(PORT);//creating the actual server socket itself
        //ThreadPool represents the number of clients that can connect to the server
        ThreadPool threadPool = new ThreadPool(3);
        System.out.println("Thread Pool Created.");
        System.out.println("Awaiting Clients.");

        //runs for ever waiting on clients to connect
        while (true) try {
            //if a client connects to the server, then the client object is created from it
            Socket client = serverSocket.accept();//accept returns client object
            threadPool.add(new Connection(client)); //adding client socket to the threadPool
        } catch (Exception e) {
            System.err.println("Exception the the EchoServer Class. Exception:" + e);
        }
    }

    /**
     * Method simply returns the port, with represents the server port
     * @return <code>int</code> represents the server port as an integer
     */
    public static int getPort() {
        return PORT;
    }
}