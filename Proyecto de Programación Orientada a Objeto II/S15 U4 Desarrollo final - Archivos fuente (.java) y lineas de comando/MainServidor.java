import java.io.*;
import java.net.*;
import java.util.*;

public class MainServidor {
    public static final int PUERTO = 4321;
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = null;
        BufferedReader entrada = null;

        try{
            servidor = new ServerSocket(PUERTO);
        } catch(IOException e) {
            System.out.println("El puerto " + PUERTO + " no esta disponible.");
            System.exit(-1);
        }

        System.out.println("Servidor iniciado y escuchando peticiones.");

        Socket cliente = null;

        List<MultiClientes> servicioCliente = new ArrayList<MultiClientes>();
        List<MultiClientes> cajero = new ArrayList<MultiClientes>();

        while(true) {
            cliente = servidor.accept();
            String tipo = null;

            try {
                entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                tipo = entrada.readLine(); // 1 RECEPCION DE LINEA DE TIPO DE CLIENTE
            } catch(IOException e) {
                System.out.println("No se pudo xd");
                System.exit(-1);
            }

            // ============== CONEXION DE SERVICIOS O DE CAJERO ==============
            if(tipo.equals("servicios")) {
                servicioCliente.add(new MultiClientes(cliente, 1));
                servicioCliente.get(servicioCliente.size() - 1).start();
                System.out.println("Se ha iniciado conexion nueva con un agente de Servicio al Cliente.");
            }
            if(tipo.equals("cajero")) {
                cajero.add(new MultiClientes(cliente, 2));
                cajero.get(cajero.size() - 1).start();
                System.out.println("Se ha iniciado conexion nueva con un Cajero.");
            }
            // ===============================================================
        }

    } // FIN DEL MAIN
} // FIN DE LA CLASE