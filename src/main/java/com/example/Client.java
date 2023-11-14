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
    public String clientName;
    Thread clientOutputThread;

    public clientInputThread(Socket socket) {
        this.clientSocket = socket;
    }
    public void chiudiConnessione() {

        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                System.out.println("Connessione chiusa.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
}
    @Override
    public void run(){

        try (
            BufferedReader inDaServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

            try {
                    boolean connesione = true;
                    
                    while(connesione){

                        String msgServer = inDaServer.readLine();
                        String[] serverMsg = msgServer.split("\\*");

                        switch(serverMsg[0]){

                            case "msg 0":
                                System.out.println(serverMsg[1]);
                                //comandi 
                                break;
                            case "msg 1":
                                System.out.println("hai ricevuto un messaggio privato da "+serverMsg[1]+"!\n");
                                System.out.println(serverMsg[1]+" : "+serverMsg[3]);
                                //client mittente : msg
                                break;
                            case "msg 2":
                                System.out.println("hai ricevuto un messaggio pubblico da "+serverMsg[1]+"!\n"); 
                                System.out.println(serverMsg[1]+" : "+serverMsg[3]+"\n");
                                break;
                            case "msg 3":
                                System.out.println(serverMsg[1]); //msg di benvenuto
                                System.out.println("hai ricevuto l'elenco degli utenti :");
                                System.out.println(serverMsg[2]); //lista client
                                break;
                            case "insert name":
                                System.out.println(serverMsg[1]);
                                break;
                            case "bye":
                                System.out.println("logout in corso ...");
                                connesione = false;
                                chiudiConnessione();
                                break;
                            case "ERRORE":
                                System.out.println(serverMsg[1]);
                                break;
                            default : System.out.println(msgServer);
                                break;
                        }
                    }

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