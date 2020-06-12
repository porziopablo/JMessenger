package notificadora;

import directorio.Directorio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.ArrayList;


public class Notificadora implements IConexion{
    
    public final static String DESTINATARIO_LOG_UP = "Log Up";
    public final static String DESTINATARIO_ONLINE = "Online";
    public final static String DESTINATARIO_OFFLINE = "Offline";
    public final static int FREC_AVISO = 5; 
    public final static int REG_EXITOSO = 1;
    public final static int REG_FALLIDO = 0;

    private ArrayList<Directorio> directorios;
    private int directorioActual;
    private boolean encendido;
    private String nombreDestinatario; 
    
    
    public Notificadora(ArrayList<Directorio> directorios) {
        this.directorios = directorios;
        this.directorioActual = 0;
    }
  
    @Override
    public int registrarDestinatario(String nombreDest, String ipDest, String puertoDest){
        
        Socket socket;
        PrintWriter salida = null;
        BufferedReader entrada = null;  
        int rta = 0;
        boolean opRealizada = false;
        Directorio directorio;
        StringBuilder sb = new StringBuilder();
        final String SEPARADOR = "_###_";
        
        while (! opRealizada) /* pre-condicion: al menos un directorio activo en todo momento */
        {
            directorio = this.directorios.get(this.directorioActual);
            try
            {
                socket = new Socket(directorio.getIp(), directorio.getPuerto());
                salida = new PrintWriter(socket.getOutputStream(), true);
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                sb.append(DESTINATARIO_LOG_UP + "\n");
                sb.append(nombreDest);
                sb.append(SEPARADOR);
                sb.append(ipDest);
                sb.append(SEPARADOR);
                sb.append(puertoDest);
                salida.println(sb.toString()); 
                
                rta = Integer.parseInt(entrada.readLine());           
                if( rta == 1){ 
                    this.nombreDestinatario = nombreDest;
                    this.encendido = true;
                    this.avisar();
                }
                opRealizada = true;             
                 
            }  
            catch (IOException e)
            {
                this.directorioActual = ((this.directorioActual + 1) % this.directorios.size());
                System.out.println("cambio directorio a " + this.directorioActual);
            }
            finally
            {
                try {
                    if( entrada!= null)
                        entrada.close();
                    if(salida != null)
                        salida.close();
                    }
                catch (IOException e) {
                }
             }
        }
        return rta;
    }
   
    @Override
    public void apagar(){
        
        Socket socket = null;
        PrintWriter salida;
        BufferedReader entrada;
        boolean opRealizada = false;
        Directorio directorio;
        
        while (! opRealizada) /* pre-condicion: al menos un directorio activo en todo momento */
        {
            directorio = this.directorios.get(this.directorioActual);
            try
            {
                socket = new Socket(directorio.getIp(), directorio.getPuerto());
                salida = new PrintWriter(socket.getOutputStream(), true);
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                StringBuilder sb = new StringBuilder();
                sb.append(DESTINATARIO_OFFLINE + "\n");
                sb.append(this.nombreDestinatario);
                salida.println(sb.toString()); 
                opRealizada = true;
            }
            catch (IOException e)
            {
                this.directorioActual = ((this.directorioActual + 1) % this.directorios.size());
                System.out.println("cambio directorio a " + this.directorioActual);
            }
            finally
            {
                try {
                    if(socket != null)
                        socket.close();
                } catch (IOException e) {
                }
             }
        }
    }
    
    private void avisar(){
        
        Thread hilo = new Thread(){
            public void run(){
                
                boolean opRealizada;
                Directorio directorio;
                Socket socket = null;
                PrintWriter salida;
                BufferedReader entrada;  
                    
                        while(true){
                            
                            try {
                                Thread.sleep(FREC_AVISO * 1000);
                            } catch (InterruptedException e) {
                                System.out.println("Problema con sleep del hilo " + e.getMessage());
                            }
                            
                            System.out.println("HILO VIVO");
                            opRealizada = false;
                            
                            while (! opRealizada) 
                            {
                                directorio = directorios.get(directorioActual);
                                try
                                {    
                                    socket = new Socket(directorio.getIp(), directorio.getPuerto());
                                    salida = new PrintWriter(socket.getOutputStream(), true);
                                    entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                    
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(DESTINATARIO_ONLINE + "\n");
                                    sb.append(nombreDestinatario);
                                    salida.println(sb.toString());                  
                                    opRealizada = true;
                                    
                                }     
                         
                                catch (IOException e)
                                {
                                    directorioActual = ((directorioActual + 1) % directorios.size());
                                    System.out.println("cambio directorio a " + directorioActual);
                                }
                                finally
                                {
                                    try {
                                        if(socket != null)
                                            socket.close();
                                    } catch (IOException e) {
                                    }
                                 }
                            }
                }
            }
        };
        hilo.start();
    }
    
    
    
}

