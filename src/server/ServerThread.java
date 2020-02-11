package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {

    private PrintWriter serverOut;
    private BufferedReader serverIn;
    private boolean running;
    private boolean validated;
    private boolean modifiedString;
    private String user;
    private StringManager strManager;
    private final String ACCESSCODE = "password";

    public ServerThread(Socket clientSocket, StringManager strManager){
        this.running = false;
        this.validated = false;
        this.modifiedString = false;
        this.user = "";
        this.strManager = strManager;
        try {
            serverOut = new PrintWriter(clientSocket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch (IOException io){
            System.err.println("Could not open socket IO stream!");
            io.printStackTrace();
        }
    }

    @Override
    public void run() {
        //try with resources to open IO stream to client
       try{
            //verify client
            while(!validated){
                serverOut.println("Enter your name: ");
                user = serverIn.readLine();
                serverOut.println("Enter access code: ");
                String access = serverIn.readLine();
                if(access.equals(ACCESSCODE)){
                    serverOut.println("you are connected");
                    System.out.println(user + " has connected!");
                    running = true;
                    validated = true;
                }
                else{
                    serverOut.println("incorrect access code");
                }
            }
            //Perform mutations on Mutable String (Server)
            while(running){
                String input = serverIn.readLine();
                //broadcast
                synchronized (strManager){
                    //step one: set modifiedString to true
                    this.modifiedString = true;
                    //step two: broadcast the string
                    strManager.broadcast(user,input);
                    this.modifiedString = false;
                }
            }
            serverOut.close();
            serverIn.close();
        }catch (IOException io) {
           System.out.println("Server socket IO stream is not open!");
           io.printStackTrace();
       }
    }

    protected void writeToClient(String str){
        serverOut.println(str);
    }

    protected boolean isRunning(){
        return running;
    }

    protected boolean isValidated(){
        return validated;
    }

    protected boolean updatedString(){
        return modifiedString;
    }
}
