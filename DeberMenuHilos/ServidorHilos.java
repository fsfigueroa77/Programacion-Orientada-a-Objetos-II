import java.io.*;
import java.net.*;

class ServidorHilos extends Thread{
    private static int cantHilos=0;
    private String nombreUsuario;
    private int numHilo;
    
    Socket cliente;
    
    BufferedReader entrada = null;
    PrintWriter salida = null;
    
    public ServidorHilos(Socket cliente){
        cantHilos++;
        this.numHilo=cantHilos;
        this.cliente = cliente;
    }
    
    @Override
    public void run(){
        try {
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream())), true);

            salida.println("Enviame tu nombre: "); // Envio al cliente pidiendo su nombre
            nombreUsuario = entrada.readLine(); // Recepcion del nombre del cliente
            System.out.println("Se establecio conexion con " + nombreUsuario); 

            salida.println("Hola " + this.nombreUsuario + " el hilo numero " + this.numHilo + " te atendera...");
            
            int control = 0;
            String respuestaMenu;

            while(control != -1) {
                respuestaMenu = entrada.readLine();
                if(respuestaMenu.equals("1")) {
                    System.out.println("El hilo numero " + numHilo + " del usuario " + nombreUsuario +
                            " ha elejido la opcion 1 Iniciar");
                }else if(respuestaMenu.equals("2")) {
                    System.out.println("El hilo numero " + numHilo + " del usuario " + nombreUsuario +
                            " ha elejido la opcion 2 Editar");
                }else if(respuestaMenu.equals("3")) {
                    System.out.println("El hilo numero " + numHilo + " del usuario " + nombreUsuario +
                            " ha elejido la opcion 3 Borrar");
                }else if(respuestaMenu.equals("4")) {
                    System.out.println("El hilo numero " + numHilo + " del usuario " + nombreUsuario +
                            " ha elejido la opcion 4 Salir");
                    control = -1;
                }

            }
            System.out.println("Conexion terminada con el usuario " + nombreUsuario + " del hilo " + numHilo);
        }catch (IOException e){
            System.out.println("error");
        }
  
    }// FIN DEL MAIN

} // FIN DE LA CLASE ServidorHilos
