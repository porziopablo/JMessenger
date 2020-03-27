package main;

import emisora.Emisora;

import java.util.Iterator;

import mensaje.Mensaje;

import usuarios.Destinatario;
import usuarios.Emisor;

public class Main
{
    public static void main(String[] args)
    {   
        Emisor emisor = new Emisor("Pablo Porzio", "192.168.0.9", "2380");
        String asunto = "Prueba Mensaje\nIntento 1";
        String cuerpo = "Hola\ntodo bien?\nchau";
        
        Mensaje mensaje = new Mensaje(asunto, cuerpo, Mensaje.MENSAJE_ALERTA, null);
        
        Emisora emisora = new Emisora(emisor);
        
        System.out.println(emisora.mensajeAString(mensaje));
    }
}
