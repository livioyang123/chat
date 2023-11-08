package com.example;
import java.io.*;
import java.net.*;

public class Client {

    String nomeServer = "localhost";
    int portaServer = 6789;
    Socket miSocket;
    String strRicevutaDalServer;
    DataOutputStream outVersoServer;
    BufferedReader inDaServer;

    public Socket connetti(){
        try {

            miSocket = new Socket(nomeServer,portaServer);

            Thread clientInputThread = new Thread(new clientInputThread(miSocket));
            Thread clientOutputThread = new Thread(new clientOutputThread(miSocket));

            clientInputThread.start();
            clientOutputThread.start();

        } catch (UnknownHostException e) {
            // TODO: handle exception
            System.err.println("host sconosciuto");
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("errore durante la connesione ");
            System.exit(1);
        }
        return miSocket;
    }

    public void comunica(){

        try {
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            System.out.println("errore durante la connesione con il server!");
            System.exit(1);
        }
    }
    
}

class clientInputThread implements Runnable {
    private Socket clientSocket;

    public clientInputThread(Socket socket) {
        this.clientSocket = socket;
    }
    
    @Override
    public void run(){

        try (
            BufferedReader inDaServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

            try {
                    

            } catch (Exception e) {
                e.printStackTrace();
            }
                
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class clientOutputThread implements Runnable {
    private Socket clientSocket;

    public clientOutputThread(Socket socket) {
        this.clientSocket = socket;
    }
    
    @Override
    public void run(){

        try (  
            DataOutputStream outVersoServer = new DataOutputStream(clientSocket.getOutputStream());
        ) {

            try {
                    

            } catch (Exception e) {
                e.printStackTrace();
            }
                
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}