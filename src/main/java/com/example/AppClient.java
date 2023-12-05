package com.example;

/**
 * Hello world!
 *
 */
public class AppClient 
{
    public static void main( String[] args )
    {
        Client utente = new Client();
        utente.connetti();
        utente.comunica();
    }
}
