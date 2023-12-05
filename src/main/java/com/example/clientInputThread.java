package com.example;
import java.io.*;
import java.net.*;

class clientInputThread implements Runnable {
    private Socket clientSocket;
    public String clientName;
    Thread clientOutputThread;
    public boolean connesione;

    public clientInputThread(Socket socket,Thread clientOutputThread) {
        this.clientSocket = socket;
        this.clientOutputThread = clientOutputThread;
        connesione = true;
    }
    public void setConnssione(boolean connessione){
        this.connesione = connessione;
    }
    public clientInputThread(Socket socket) {
        this.clientSocket = socket;
        connesione = true;
    }
    public boolean getConnessione(){
        return connesione;
    }
    public void chiudiConnessione() {

        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                setConnssione(false);
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
                    
                    
                    while(connesione){

                        String msgServer = inDaServer.readLine();
                        String[] serverMsg = msgServer.split("\\*");

                        switch(serverMsg[0]){

                            case "msg 0":
                                for(int i = 1;i<serverMsg.length;i++) System.out.println(serverMsg[i]);
                                //comandi 
                                break;
                            case "msg 1":
                                System.out.println("hai ricevuto un messaggio privato da "+serverMsg[1]+"!\n");
                                System.out.println(serverMsg[1]+" : "+serverMsg[3]+"\n");
                                //client mittente : msg
                                break;
                            case "msg 2":
                                System.out.println("hai ricevuto un messaggio pubblico da "+serverMsg[1]+"!\n"); 
                                System.out.println(serverMsg[1]+" : "+serverMsg[3]+"\n");
                                break;
                            case "msg 3":
                                System.out.println("hai ricevuto l'elenco degli utenti :");
                                System.out.println(serverMsg[1]); //lista client
                                break;
                            case "insert name":
                                System.out.println(serverMsg[1]);
                                break;
                            case "bye":
                                System.out.println("logout in corso ...");
                                chiudiConnessione();
                                break;
                            case "ERRORE":
                                System.out.println(serverMsg[1]);
                                break;
                            default : System.out.println(msgServer);
                                break;
                        }
                    }

            } catch (SocketException e) {
                e.printStackTrace();
            }
                
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}