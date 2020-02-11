package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {

    private final int PRTNUMBER = 8600;
    private StringManager stringManager;

    public Server(){
        stringManager = new StringManager();
    }

    public void start(){
        while(true) {
            try (ServerSocket serverSocket = new ServerSocket(PRTNUMBER)) {
                Socket connection = serverSocket.accept();
                ServerThread serverThread = new ServerThread(connection,stringManager);
                //add serverThread
                stringManager.addServerThread(serverThread);
                new Thread(serverThread).start();
            } catch(IOException io) {
                System.err.println("Could not attach server socket!");
                io.printStackTrace();
            }
        }
    }

    public static void main(String []args)
    {
        new Server().start();
    }
}
