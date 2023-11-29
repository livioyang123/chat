package com.example;
import java.io.*;
import java.net.*;

class clientOutputThread implements Runnable {
    private Socket clientSocket;
    public String clientName;
    clientInputThread clientInputThread;
    BufferedReader tastiera;
    boolean connessione = true;

    public clientOutputThread(Socket socket,clientInputThread clientInputThread) {
        this.clientSocket = socket;
        this.clientInputThread = clientInputThread;
        tastiera = new BufferedReader(new InputStreamReader(System.in));
    }
    public clientOutputThread(Socket socket) {
        this.clientSocket = socket;
        tastiera = new BufferedReader(new InputStreamReader(System.in));
    }
    @Override
    public void run(){

        try (  
            DataOutputStream outVersoServer = new DataOutputStream(clientSocket.getOutputStream());
        ) {

            try {

                String messaggioUtente;
                
                
                while (connessione) {

                    messaggioUtente = tastiera.readLine();
                    outVersoServer.writeBytes(messaggioUtente + "\n");  
                    
                }

            } catch (SocketException e) {
                e.printStackTrace();
            }
                
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}