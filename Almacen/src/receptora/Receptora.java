package receptora;

import almacen.Almacen;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import mensaje.Mensaje;

public class Receptora implements Runnable{
    
    private String puerto;
    
    public Receptora(String puerto) {
        this.puerto = puerto;
    }

    @Override
    public void run() {
        final String SEPARADOR = "_###_"; 
        final String FINAL = "##FIN##";
        final String SEPARADOR_DEST = "_||_";
        
        try{
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(puerto));
            while(true){
                Socket socket = serverSocket.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                StringBuilder builder = new StringBuilder();
                String aux = "";
                aux = in.readLine();
                while (!aux.equals(FINAL)) { 
                    builder.append(aux);
                    builder.append("\n");
                    aux = in.readLine();
                }
                if( builder.length() > 0 )
                    builder.setLength(builder.length()-1);
                
                aux = builder.toString();
                String text[];
                text = aux.split(SEPARADOR);
                String dest[] = text[4].split(SEPARADOR_DEST);               
                
                Almacen.getInstance().agregarMensaje(text[0],text[1],text[2],text[3],dest);
                
                if(Integer.parseInt(text[3]) == Mensaje.MENSAJE_RECEPCION && text.length == 7){
                    Almacen.getInstance().agregarEmisor(text[0], text[5], text[6]);
                }             
                
                socket.close();
            }  
        }
        catch(Exception e){
            e.printStackTrace();
        } 
    }
}
