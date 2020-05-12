package main;

import agenda.Agenda;

import controlador.Controlador;

import vista.VistaEmisor;

public class Main
{
    public static void main(String[] args)
    {   
        VistaEmisor ventana = new VistaEmisor();
        Controlador controlador = new Controlador(ventana, new Agenda());
        ventana.setVisible(true);
        /*
         * GRACIAS PABLO POR LA AYUDA :)
         * BUA, AHORA APARECE EL COSITO NEGRO. AYER NO
         * TENIAS QUE APARECER
         * QUE MILAGRO
         * VAMOS A VER COMO SIGUE
         * */
    }
}
