package main;

import agenda.Agenda;

import almacen.Almacen;

import configurador.Configurador;

import emisora.Emisora;

import java.io.IOException;

import persistencia.FactoryPersistencia;

import receptora.Receptora;

public class Main 
{
    public static void main(String[] args) 
    {
        try 
        {
            Object[] configuracion = Configurador.getInstance().cargarConfiguracion("config_almacen.txt");
            Almacen.getInstance().setPersistencia(FactoryPersistencia.getInstance().getPersistencia((String)configuracion[3]));
            new Thread(new Receptora((String) configuracion[2])).start();
            new Thread(new Emisora(new Agenda((String) configuracion[0], (Integer) configuracion[1]))).start();
        } 
        catch (IOException e) 
        {
            System.out.println("Error al Cargar Configuracion: " + e.getMessage());
        }
    }
}
