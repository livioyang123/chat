package com.example;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Server {
    public  void avvioServer(){
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(6789);
            System.out.println("Server in attesa di connessioni...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread clientHandlerThread = new Thread(new ClientHandler(clientSocket));
                clientHandlerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private HashMap<String,Socket> clientList = new HashMap<String,Socket>();
    private String clientName;
    private enum messageType{
        msg1,
        msg2,
        msg3
    }

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void addClient(String clientName){
        clientList.put(clientName,clientSocket);
    }

    public boolean verifyClientName(String clientName){
        return clientList.containsKey(clientName);
    }

    public void sendMessage_private(String senderName,String receiverName,String Message,String msgType){
        Socket receiverSocket = clientList.get(receiverName);
        if(receiverSocket !=null){
            try {
                //String typeMsg="msg"+msgType;
                DataOutputStream outVersoClient = new DataOutputStream(receiverSocket.getOutputStream());
                outVersoClient.writeBytes(senderName+"*"+msgType+"* "+receiverName+": "+Message+"\n");
                //*msg 1* nome_dest: messaggio

            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        

    }
    public void sendMessage_pubblic(String senderName,String receiverName,String Message,String msgType){
        for(String clientName : clientList.keySet()){
            if(!clientName.equalsIgnoreCase(senderName))sendMessage_private(senderName, receiverName, Message, msgType);
        }
    }
    
    @Override
    public void run(){

        try (
            BufferedReader inDaClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outVersoClient = new DataOutputStream(clientSocket.getOutputStream());
        ) {

            try {
                //verifica nome
                do{

                    outVersoClient.writeBytes("*insert name*inserisci il tuo nome"+"\n");
                    clientName = inDaClient.readLine();

                }while(verifyClientName(clientName));
                //

                
                
                



            } catch (Exception e) {
                outVersoClient.writeBytes("ERRORE"+"\n");
            }
                
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
