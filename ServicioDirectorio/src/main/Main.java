package main;

import almacen.Almacen;

import configuracion.Configurador;

import directorio.Directorio;

import gestores.GestorDestinatario;
import gestores.GestorEmisor;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Date;

import replicacion.Replicacion;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Object[] configuracion = Configurador.getInstance().cargarConfiguracion("config_directorio.txt");
            Replicacion replicacion = new Replicacion((ArrayList<Directorio>)configuracion[3],(Integer)configuracion[2]); 
            
            Almacen.getInstance().setReplicacion(replicacion);
            replicacion.recibirCambios();
            
            new Thread(new GestorDestinatario((Integer) configuracion[0])).start();
            new Thread(new GestorEmisor((Integer) configuracion[1])).start();
        } 
        catch (IOException e)
        {
            System.out.println("Error al Cargar Configuracion: " + e.getMessage());
        }
    }
}
