package com.example;
import java.io.*;
import java.net.*;

class clientOutputThread implements Runnable {
    private Socket clientSocket;
    public String clientName;
    BufferedReader tastiera;

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
                //scrivi nome
                clientName = tastiera.readLine();

                // Invia il nome al server
                outVersoServer.writeBytes(clientName + "\n");

                // Attendi l'input dall'utente e invialo al server
                String messaggioUtente;
                while (true) {
                    messaggioUtente = tastiera.readLine();
                    outVersoServer.writeBytes(messaggioUtente + "\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
                
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}