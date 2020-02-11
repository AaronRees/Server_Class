package server;

import java.util.ArrayList;
import java.util.List;

public class StringManager {

    private String mutableString;
    private List<ServerThread> threads;

    public StringManager(){
        mutableString = "";
        threads = new ArrayList<>();
    }

    protected void broadcast(String currentUser, String msg){
        mutableString = msg;
        //broadcast to server
        System.out.println(mutableString);
        for(ServerThread thread : threads){
            if(!thread.updatedString() && thread.isRunning() && thread.isValidated()) {
                thread.writeToClient(mutableString);
            }
        }
    }

    protected void addServerThread(ServerThread serverThread){
        threads.add(serverThread);
    }
}
