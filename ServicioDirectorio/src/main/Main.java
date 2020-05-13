package main;

import configuracion.Configurador;

import gestores.GestorDestinatario;
import gestores.GestorEmisor;

import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Object[] configuracion = Configurador.getInstance().cargarConfiguracion("config_directorio.txt");
            new Thread(new GestorDestinatario((Integer) configuracion[0])).start();
            new Thread(new GestorEmisor((Integer) configuracion[1])).start();
        } 
        catch (IOException e)
        {
            System.out.println("Error al Cargar Configuracion: " + e.getMessage());
        }
    }
}
