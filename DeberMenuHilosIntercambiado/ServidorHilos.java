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
            while (control == 0) {

                String menu = "1. Consultar saldos/2. Retirar/3. Depositar/4. Salir";
                String eleccion;
                salida.println(menu);
                eleccion = entrada.readLine();
                System.out.println("El usuario ha elegido la opcion " + eleccion);
                if(eleccion.equals("4")){
                    control = 1;
                }
                salida.println(String.valueOf(control));
            }

            System.out.println("Conexion terminada con el usuario " + nombreUsuario + " del hilo " + numHilo);
        }catch (IOException e){
            System.out.println("error");
        }
  
    }// FIN DEL MAIN

} // FIN DE LA CLASE ServidorHilos
