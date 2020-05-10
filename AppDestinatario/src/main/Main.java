package main;

import controlador.Controlador;

import notificadora.Notificadora;

import vista.vistaDestinatario;


public class Main
{
    public static void main(String[] args)
    {
        vistaDestinatario ventana = new vistaDestinatario();  
        Controlador controlador = new Controlador(ventana, new Notificadora());
        ventana.setVisible(true);
    }
}
