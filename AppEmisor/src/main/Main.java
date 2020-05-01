package main;

import agenda.Agenda;

import controlador.Controlador;

import vista.VistaEmisor;

public class Main
{
    public static void main(String[] args)
    {   
        VistaEmisor ventana = new VistaEmisor();
        Controlador controlador = new Controlador(ventana, new Agenda());
        ventana.setVisible(true);
    }
}
