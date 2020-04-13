package main;

import controlador.Controlador;

import vista.VistaEmisor;

public class Main
{
    public static void main(String[] args)
    {   
        VistaEmisor ventana = new VistaEmisor();
        Controlador controlador = new Controlador(ventana);
        ventana.setVisible(true);
    }
}
