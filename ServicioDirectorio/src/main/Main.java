package main;

import directorio.Directorio;

import java.util.Date;

import usuarios.Destinatario;

public class Main
{
    public static void main(String[] args)
    {
        Directorio directorio = new Directorio();
                              
        directorio.atenderEmisores();
        directorio.escucharDestinatarios();
    }
}
