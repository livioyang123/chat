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

            clientInputThread clientInput = new clientInputThread(miSocket);
            clientOutputThread clientOutput = new clientOutputThread(miSocket,clientInput);
            Thread clientInputThread = new Thread(clientInput);
            Thread clientOutputThread = new Thread(clientOutput);
            
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

    public void chiudiConnessione() {

        try {
            if (miSocket != null && !miSocket.isClosed()) {
                miSocket.close();
                System.out.println("Connessione chiusa.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}



