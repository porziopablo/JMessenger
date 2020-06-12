package main;

import agenda.Agenda;

import almacen.Almacen;

import configuracion.Configurador;

import controlador.Controlador;

import directorio.Directorio;

import emisora.Emisora;

import encriptacion.FactoryEncriptacion;

import java.io.IOException;

import java.util.ArrayList;

import persistencia.FactoryPersistencia;

import vista.VistaEmisor;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Object[] config = Configurador.getInstance().cargarConfiguracion("config_emisor.txt");
            VistaEmisor ventana = new VistaEmisor();
            Almacen.getInstance().setPersistencia(FactoryPersistencia.getInstance().getPersistencia((String)config[5]));
            new Controlador(ventana, new Agenda((ArrayList<Directorio>) config[6]), 
                                new Emisora
                                (
                                            (String) config[2], (Integer) config[3], (Integer) config[4], 
                                            FactoryEncriptacion.getInstance().getEncriptacion
                                            (
                                                (String) config[0], (String) config[1]
                                            )
                                )
                            );
            ventana.setVisible(true);
        } 
        catch (IOException e)
        {
            System.out.println("Error al cargar configuracion: " + e.getMessage());
        }
    }
}
