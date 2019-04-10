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
@SuppressWarnings("ALL")
public class EchoServer {

    private static final int PORT = 1111;

    public static void main(String[] args) throws IOException {
        System.out.println("Server Started.");
        ServerSocket serverSocket = new ServerSocket(PORT);
        ThreadPool threadPool = new ThreadPool(3);
        System.out.println("Awaiting clients.");

        while (true) {
            try {
                Socket client = serverSocket.accept();
                //adding client socket to the threadPool
                threadPool.add(new Connection(client));//make this a if statement
            } catch (Exception e) {
                System.err.println("Exception the the EchoServer Class. Exception:" + e);
            }
        }
    }

    public static int getPort() {
        return PORT;
    }
}