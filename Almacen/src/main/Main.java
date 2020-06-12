package main;

import agenda.Agenda;

import almacen.Almacen;

import configurador.Configurador;

import confirmadora.Confirmadora;

import directorio.Directorio;

import emisora.Emisora;

import java.io.IOException;

import java.util.ArrayList;

import persistencia.FactoryPersistencia;

import receptora.Receptora;

public class Main 
{
    public static void main(String[] args) 
    {
        try 
        {
            Object[] configuracion = Configurador.getInstance().cargarConfiguracion("config_almacen.txt");
            Almacen.getInstance().setPersistencia(FactoryPersistencia.getInstance().getPersistencia((String)configuracion[1]));
            new Thread(new Receptora((Integer) configuracion[0])).start();
            new Thread(new Emisora(new Agenda((ArrayList<Directorio>) configuracion[2]))).start();
            new Thread(new Confirmadora()).start();
        } 
        catch (IOException e) 
        {
            System.out.println("Error al Cargar Configuracion: " + e.getMessage());
        }
    }
}
