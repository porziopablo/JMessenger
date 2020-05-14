
package main;

import almacen.Almacen;

import configurador.Configurador;

import java.io.IOException;

import persistencia.FactoryPersistencia;

import receptora.Receptora;

public class Main 
{
    public static void main(String[] args) 
    {
        try {
            Object[] configuracion = Configurador.getInstance().cargarConfiguracion("config_almacen.txt");
            Almacen.getInstance().setPersistencia(FactoryPersistencia.getInstance().getPersistencia((String)configuracion[3]));
            new Thread(new Receptora((String) configuracion[2])).start();
        } catch (IOException e) {
            System.out.println("Error al Cargar Configuracion: " + e.getMessage());
        }
    }
}
