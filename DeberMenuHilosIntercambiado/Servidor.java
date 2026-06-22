import java.net.*;
import java.io.*;
import java.util.*;

public class Servidor {
    public static final int PORT = 4445; // Constante que contiene el numero del puerto que se utilizara en este programa
    public static void main(String[] args) throws IOException {
        List<ServidorHilos> hilos = new ArrayList<>(); // Declaracion de lista de objetos tipo ServidorHilos
        // INICIAR EL SERVIDOR
        ServerSocket servidor = null; // Declaracion de objeto ServerSocket en null

        try{
            servidor = new ServerSocket(PORT); // Intenta iniciar el server en el puerto 4444
        } catch(IOException e) { // Captura la excepcion cuando el puerto no esta disponible
            System.out.println("El puerto " + PORT + " esta ocupado."); 
            System.exit(-1); // Finaliza el programa de forma anormal
        }
        System.out.println("Servidor iniciado y escuchando peticiones.");
        // INICIAR CONEXION CON LOS CLIENTES
        Socket cliente = null; // Declaracion de objeto Socket en null
        
        while(true) {
            cliente = servidor.accept(); // Bloquea la ejecucion del programa hasta que llegue una nueva peticion de un cliente
            hilos.add(new ServidorHilos(cliente)); // Crea un nuevo hilo con el nuevo cliente que se acaba de aceptar y lo agrega al final de la lista de hilos
            hilos.get(hilos.size() - 1).start(); // Se obtiene el ultimo hilo de la lista mediante el su indice y se manda a ejecutar el hilo
        }

    } //FIN DEL MAIN
} //FIN DE LA CLASE Servidor

//7
//69