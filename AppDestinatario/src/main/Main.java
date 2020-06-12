package main;

import configuracion.Configurador;

import controlador.Controlador;

import desencriptacion.DesencriptadorAES;

import desencriptacion.FactoryDesencriptacion;
import desencriptacion.IDesencriptacion;

import directorio.Directorio;

import java.io.IOException;

import java.util.ArrayList;

import notificadora.Notificadora;

import receptora.Receptora;

import vista.vistaDestinatario;


public class Main
{
    public static void main(String[] args)
    {
        try {
            Object[] configuracion = Configurador.getInstance().cargarConfiguracion("config_destinatario.txt");
            vistaDestinatario ventana = new vistaDestinatario(); 
            IDesencriptacion des = FactoryDesencriptacion.getInstance().getDesencriptacion((String)configuracion[0], (String) configuracion[1]);
            Controlador controlador = new Controlador(ventana, 
                                                      new Notificadora( (ArrayList<Directorio>) configuracion[2]),
                                                      new Receptora(des));
            ventana.setVisible(true);
            
        } catch (IOException e) {
            System.out.println("Error al cargar configuracion: " + e.getMessage());
        }

    }
}
