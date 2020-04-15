package main;

import directorio.Directorio;

public class Main
{
    public static void main(String[] args)
    {
       Directorio directorio = new Directorio();
       
       directorio.escucharDestinatarios();
       directorio.atenderEmisores();
    }
}
