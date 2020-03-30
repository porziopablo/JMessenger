package receptora;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;

import java.net.Socket;

import java.util.Observable;

import java.util.stream.Stream;

import mensaje.Mensaje;

import usuarios.Destinatario;

public class Receptora extends Observable{
    
    private Destinatario dest;
    
    public Receptora(Destinatario destinatario) {
        this.dest = destinatario;
    }
    
    public void recibirMensaje(){
        
        new Thread(){
            public void run(){
                Mensaje mensaje;
                final String SEPARADOR = "_###_"; /* regex */
                
                try{
                    ServerSocket serverSocket = new ServerSocket(Integer.parseInt(dest.getPuerto()));
                    while(true){
                        Socket socket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        
                        StringBuilder builder = new StringBuilder();
                        String aux = "";
                        aux = in.readLine();
                        while (aux != null) {
                            builder.append(aux);
                            aux = in.readLine();
                        }
                        socket.close();
                        
                        aux = builder.toString();
                        String text[];
                        text = aux.split(SEPARADOR);
                        
                        mensaje = new Mensaje(text[0], text[1], text[2], Integer.parseInt(text[3]));
                        
                        if(mensaje.getTipo() == 2){
                            enviarConfirmacion(text[4], text[5]);
                        }
                        setChanged();
                        notifyObservers(mensaje); 
                    }  
                }
                catch(Exception e){
                    e.printStackTrace();
                } 
            }
        }.start();
 
    }
    
    private void enviarConfirmacion(String ipEmisor, String puertoEmisor){
        
        Socket socket;
        PrintWriter confirmacion;
        
        try{
            socket = new Socket(ipEmisor, Integer.parseInt(puertoEmisor));
            confirmacion = new PrintWriter(socket.getOutputStream(), true);
            confirmacion.println(this.dest.getNombre());
            confirmacion.close();
            socket.close();
        }
        catch(Exception e){
            System.out.println("Error al enviar la confirmacion de llegada: " + e.getMessage());
        }
    }
}
