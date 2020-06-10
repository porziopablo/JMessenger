package main;

import agenda.Agenda;

import configuracion.Configurador;

import controlador.Controlador;

import directorio.Directorio;

import emisora.Emisora;

import encriptacion.EncriptadorAES;
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
            Object[] configuracion = Configurador.getInstance().cargarConfiguracion("config_emisor.txt");
            VistaEmisor ventana = new VistaEmisor();
            new Controlador(
                                ventana, 
                                new Agenda((ArrayList<Directorio>) configuracion[5]), 
                                new Emisora
                                (
                                            (String) configuracion[2], 
                                            (Integer) configuracion[3], 
                                            (Integer) configuracion[4], 
                                            FactoryEncriptacion.getInstance().getEncriptacion
                                            (
                                                (String)configuracion[0],
                                                (String) configuracion[1]
                                            ),
                                            FactoryPersistencia.getInstance().getPersistencia((String) configuracion[6])
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
