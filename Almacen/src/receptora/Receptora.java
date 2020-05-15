package receptora;

import almacen.Almacen;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import mensaje.Mensaje;

public class Receptora implements Runnable{
    
    private int puerto;
    
    public Receptora(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public void run() {
        final String SEPARADOR = "_###_"; 
        final String FINAL = "##FIN##";
        final String SEPARADOR_DEST = "_@@_";
        
        try{
            ServerSocket serverSocket = new ServerSocket(puerto);
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
                
                System.out.println("emisor: " + text[0]);
                System.out.println("asunto " + text[1]);
                System.out.println("cuerpo "+ text[2]);
                System.out.println("tipo " + text[3]);
                System.out.println("destinatarios " + text[4]);
                
                for(int i=0; i<dest.length; i++)
                    System.out.print(dest[i]);
                
                if(text.length == 7){
                    System.out.println("" + text[5]);
                    System.out.println(text[6]);    
                }
                
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
