import java.io.*;
import java.net.*;
import java.util.*;

public class MainServidor {

    public static final int PORT = 4444;

    public static void main(String[] args) throws IOException {
        List<MultiConexiones> hilos = new ArrayList<>();
        ServerSocket servidor = null;

        try {
            servidor = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("El puerto " + PORT + " no esta disponible.");
            System.exit(-1);
        }

        Socket cliente = null;

        while (true) {
            cliente = servidor.accept();
            hilos.add(new MultiConexiones(cliente));
            hilos.get(hilos.size() - 1).start();
        }

    }
}
