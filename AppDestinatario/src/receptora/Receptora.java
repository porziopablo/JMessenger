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
                final String FINAL = "##FIN##";
                
                try{
                    ServerSocket serverSocket = new ServerSocket(Integer.parseInt(dest.getPuerto()));
                    while(true){
                        Socket socket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        
                        StringBuilder builder = new StringBuilder();
                        String aux = "";
                        aux = in.readLine();
                        while (!aux.equals(FINAL)) { //leer mientras no sea fin ! no mientras sea null
                            builder.append(aux);
                            builder.append("\n");
                            aux = in.readLine();
                        }
                        if( builder.length() > 0 )
                            builder.setLength(builder.length()-1);
                        
                        aux = builder.toString();
                        String text[];
                        text = aux.split(SEPARADOR);
                        System.out.println(text[3]);
                        mensaje = new Mensaje(text[0], text[1], text[2], Integer.parseInt(text[3]));
                        
                        if(mensaje.getTipo() == Mensaje.MENSAJE_RECEPCION){
                            out.println(dest.getNombre());
                            
                        }
                        socket.close();
                        
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
}