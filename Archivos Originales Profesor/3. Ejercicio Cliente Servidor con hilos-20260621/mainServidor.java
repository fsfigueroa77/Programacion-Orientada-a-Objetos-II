import java.io.*;
import java.net.*;
import java.util.*;


public class mainServidor {
    public static final int PORT=4444;

    public static void main(String args[]) throws IOException {
        //para servidor
        ServerSocket SCK_Serv = null;

        try {
            SCK_Serv = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("No se pudo iniciar el servidor en el puerto " + PORT);
            System.exit(-1);
        }

        Socket SCK_cli = null;


        System.out.println("Servidor esta escuchando: " + SCK_Serv);

        List<ServidorHilos> objetos_hilos = new ArrayList<ServidorHilos>();

        while (true) {
            SCK_cli = SCK_Serv.accept();

            objetos_hilos.add(new ServidorHilos(SCK_cli));

            objetos_hilos.get(objetos_hilos.size() - 1).start();

        }
    }
}
