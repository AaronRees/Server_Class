package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args)
    {
        final String hostname = "127.0.0.1";
        final int portNumber = 8600;

        try(
                Socket clientSocket = new Socket(hostname, portNumber);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))
                )
        {
            while(true)
            {
                System.out.println(in.readLine());
                out.println(stdin.readLine());
                System.out.println(in.readLine());
                out.println(stdin.readLine());
                String serverResponse = in.readLine();
                System.out.println(serverResponse);
                if(serverResponse.equals("you are connected"))
                {
                    break;
                }
            }

            //spawn thread for monitoring input stream
            new Thread(()->{
                while(true){
                    try {
                        System.out.println(in.readLine());
                    }catch (IOException io){
                        System.out.println("Socket input stream lost!");
                        io.printStackTrace();
                    }
                }
            }).start();

            //while loop for supplying user input
            while(true)
            {
                out.println(stdin.readLine());
            }
        }catch(IOException io) {
            System.out.println("Socket connection failed!");
            io.printStackTrace();
        }
    }
}
