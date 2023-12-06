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
    private String clientName;
    private static HashMap<String,Socket> clientList = new HashMap<String,Socket>();
    

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public synchronized static void addClient(String clientName,Socket clientSocket){
        clientList.put(clientName,clientSocket);
    }

    public boolean verifyClientName(String clientName){
        return clientList.containsKey(clientName);
    }
    // msg 1 privato
    public void sendMessage_private(String senderName,String receiverName,String Message,String msgType){
        Socket receiverSocket = clientList.get(receiverName);
        if(receiverSocket !=null){
            try {
 
                DataOutputStream outVersoClient = new DataOutputStream(receiverSocket.getOutputStream());
                outVersoClient.writeBytes(msgType+"*"+senderName+"*"+receiverName+"*"+Message+"\n");
                //msg 1* nome_dest: messaggio

            } catch (IOException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        

    }
    // msg 2 broadcast
    public void sendMessage_pubblic(String senderName,String Message,String msgType){

        for(String clientName : clientList.keySet()){
            if(!clientName.equalsIgnoreCase(senderName))sendMessage_private(senderName, clientName, Message, msgType);
        }
    }
    // msg 3 invio lista client
    public void sendClientList(String receiverName,String msgType){
        Socket receiverSocket = clientList.get(receiverName);
        if(receiverSocket !=null){
            try {

                //String typeMsg="msg"+msgType;
                DataOutputStream outVersoClient = new DataOutputStream(receiverSocket.getOutputStream());
                String lista = clientList();
                System.out.println(clientList());
                outVersoClient.writeBytes(msgType+"*"+lista+"\n");
                //msg 3*msg
                
            } catch (IOException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
    //lista client
    public String clientList(){
        String msg = "";
        Boolean isFirst = true;
        for(String clientNames : clientList.keySet()){
            if(!isFirst) msg+="-"; // se non è il primo aggiungi - per differenziare i nomi
            msg += clientNames;
            isFirst = false;
        }
        return msg;
    }
   //comandi che un client puo fare
    public String commands(){
        String commands = "msg 0*";

        commands += "scegli l'operazione da svolgere:"+"*"
                + "1 per inviare un messaggio privato"+"*"
                + "2 per inviare un messaggio pubblico"+"*"
                + "3 per uscire dalla chat*";
            
        return commands;
    }
    //per chiuder la connesione del client dato il nome
    public void disconnessione(String clientName) {
        Socket clientSocket = clientList.get(clientName);
        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                clientSocket.close();
                clientList.remove(clientName);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

                    outVersoClient.writeBytes("insert name*inserisci il tuo nome "+"\n");
                    clientName = inDaClient.readLine().trim();

                }while(clientName.isEmpty()||verifyClientName(clientName));

                addClient(clientName, clientSocket);
                System.out.println(clientName+" si è collegato");

                //invio lista client
                outVersoClient.writeBytes("Benvenuto nella chat!"+"\n");
                sendClientList(clientName, "msg 3");
                
                boolean clientConnesso = true;
                String scelta = "";
                while(clientConnesso){
                    int operazione;
                    try {
                        
                        //invio comandi al client
                        outVersoClient.writeBytes(commands()+"\n");
                        scelta = inDaClient.readLine();
                        operazione = Integer.parseInt(scelta);
                    

                    } catch (NumberFormatException e) {
                        // se la scelta non è un numero
                        operazione = 0;
                    }

                    String destinatario;
                    String msg;

                    switch(operazione){
                        case 1 : 

                                do{ // verifica nome del destinatario
                                    outVersoClient.writeBytes("inserisci il nome del destinatario"+"(scegli tra:"+clientList()+")"+"\n");
                                    destinatario = inDaClient.readLine();
                                }while(!verifyClientName(destinatario));

                                outVersoClient.writeBytes("inserisci il messaggio da inviare a "+destinatario+"\n");
                                msg = inDaClient.readLine();

                                sendMessage_private(clientName, destinatario, msg, "msg 1");

                                break;

                        case 2 : 
                               
                                outVersoClient.writeBytes("inserisci il messaggio da inviare  "+"\n");
                                msg = inDaClient.readLine();

                                sendMessage_pubblic(clientName,msg, "msg 2");
                                
                                break;

                        case 3 : 

                                outVersoClient.writeBytes("bye*"+"\n");
                                disconnessione(clientName);
                                clientConnesso = false; //per uscire dal ciclo
                                break;

                        default : outVersoClient.writeBytes("ERRORE*scegli l'operazione corretta"+"\n");
                                 
                    }
                }
                
                //ArrayIndexOutOfBoundsException,NumberFormatException
            } catch (ArrayIndexOutOfBoundsException e) {
                outVersoClient.writeBytes("ERRORE*"+"inserisci un numero valido\n");
            } 
                
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally { //se viene chiuso il socket del client
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
