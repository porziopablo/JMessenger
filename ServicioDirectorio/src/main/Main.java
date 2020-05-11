package main;

import gestores.GestorDestinatario;
import gestores.GestorEmisor;

public class Main
{
    public static void main(String[] args)
    {                
        new Thread(new GestorEmisor()).start();
        new Thread(new GestorDestinatario()).start();
    }
}
