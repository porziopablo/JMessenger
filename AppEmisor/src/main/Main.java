package main;

import agenda.Agenda;

import configuracion.Configurador;

import controlador.Controlador;

import emisora.Emisora;

import java.io.IOException;

import vista.VistaEmisor;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Object[] configuracion = Configurador.getInstance().cargarConfiguracion("config_emisor.txt");
            VistaEmisor ventana = new VistaEmisor();
            new Controlador(
                                ventana, 
                                new Agenda((String) configuracion[0], (Integer) configuracion[1]), 
                                new Emisora((String) configuracion[2], (Integer) configuracion[3], 
                                                                        (Integer) configuracion[4])
                            );
            ventana.setVisible(true);
        } 
        catch (IOException e)
        {
            System.out.println("Error al cargar configuracion: " + e.getMessage());
        }
    }
}
